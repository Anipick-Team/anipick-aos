# Firebase 사용 가이드

## 개요

AniPick 앱은 Firebase의 다음 서비스를 사용합니다:
- **Analytics**: 사용자 행동 및 화면 추적
- **Crashlytics**: 크래시 리포팅 및 비정상 종료 추적
- **Performance Monitoring**: 앱 성능 및 네트워크 요청 모니터링

## 자동 설정

### 디버그 vs 릴리즈 모드

Firebase는 빌드 타입에 따라 자동으로 활성화/비활성화됩니다:

- **디버그 모드**: 모든 Firebase 수집 **비활성화** (개발 중 노이즈 방지)
- **릴리즈 모드**: 모든 Firebase 수집 **활성화** (실제 사용자 데이터 수집)

### 앱 초기화

`AniPickApplication`에서 자동으로 Firebase가 초기화됩니다:

```kotlin
@HiltAndroidApp
class AniPickApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseManager.getInstance(this).initialize()
    }
}
```

## 1. Analytics - 화면 추적

### ✅ 자동 화면 추적 (설정 완료)

**별도 코드 추가 불필요!** MainActivity에서 Navigation 변경을 자동으로 추적합니다.

```kotlin
// MainActivity.kt - 이미 설정됨
TrackScreenNavigation(navController = navController)
```

모든 화면 전환이 자동으로 Firebase Analytics에 `screen_view` 이벤트로 기록됩니다:
- ✅ `home` → Home 화면
- ✅ `login` → Login 화면
- ✅ `anime_detail/{animeId}` → Anime Detail 화면
- ✅ `mypage` → MyPage 화면
- 등등...

### 사용자 정보 설정

로그인 성공 시 ViewModel에서 사용자 정보를 설정하세요:

```kotlin
// LoginMVVMViewModel.kt
class LoginViewModel @Inject constructor(
    private val context: Application
) : ViewModel() {

    fun onLoginSuccess(userId: String) {
        val firebaseManager = FirebaseManager.getInstance(context)

        // 사용자 ID 설정
        firebaseManager.setUserId(userId)

        // 사용자 속성 설정 (선택)
        firebaseManager.setUserProperty("user_type", "premium")
        firebaseManager.setUserProperty("signup_date", "2025-01-01")
    }
}
```

## 2. Crashlytics - 크래시 리포팅

### 자동 크래시 수집

모든 **처리되지 않은 예외**는 자동으로 Crashlytics에 리포팅됩니다. 추가 설정 불필요!

### Non-Fatal 에러 기록

예상된 에러나 처리한 예외를 기록하고 싶을 때:

```kotlin
// ViewModel 또는 Repository에서
try {
    val result = apiService.getData()
} catch (e: Exception) {
    // Non-fatal error 기록
    FirebaseManager.getInstance(context).logNonFatalError(e)

    // 사용자에게 에러 메시지 표시
    _uiState.value = UiState.Error(e.message)
}
```

### 커스텀 로그 추가

디버깅을 위해 Crashlytics에 로그를 남기세요:

```kotlin
class AnimeRepository @Inject constructor(
    private val context: Application
) {
    suspend fun getAnimeDetail(animeId: Int): Result<AnimeDetail> {
        val firebaseManager = FirebaseManager.getInstance(context)

        firebaseManager.logCrashlyticsMessage("Fetching anime detail: $animeId")

        return try {
            val response = apiService.getAnimeDetail(animeId)
            firebaseManager.logCrashlyticsMessage("Successfully fetched anime: $animeId")
            Result.success(response)
        } catch (e: Exception) {
            firebaseManager.logCrashlyticsMessage("Failed to fetch anime: $animeId - ${e.message}")
            firebaseManager.logNonFatalError(e)
            Result.failure(e)
        }
    }
}
```

### 사용자 정보 설정

크래시가 발생했을 때 어떤 사용자인지 식별하기 위해:

```kotlin
// 로그인 성공 시
FirebaseManager.getInstance(context).setUserId(userId)

// 사용자 정보 추가
FirebaseManager.getInstance(context).setUserProperty("email", "user@example.com")
FirebaseManager.getInstance(context).setUserProperty("plan", "premium")
```

## 3. Performance Monitoring

### 자동 성능 측정

다음 항목은 **자동으로 측정**됩니다:
- ✅ 앱 시작 시간
- ✅ 모든 HTTP/HTTPS 네트워크 요청
- ✅ 포그라운드/백그라운드 전환

### 커스텀 트레이스 - 함수 성능 측정

특정 작업의 성능을 측정하고 싶을 때:

```kotlin
import com.jparkbro.anipick.firebase.measurePerformance

class AnimeViewModel @Inject constructor() : ViewModel() {

    fun loadAnimeList() {
        viewModelScope.launch {
            // 성능 측정 with attributes
            measurePerformance(
                traceName = "load_anime_list",
                attributes = mapOf(
                    "category" to "popular",
                    "page" to "1"
                )
            ) {
                val animeList = repository.getAnimeList()
                _animeList.value = animeList
            }
        }
    }
}
```

### Suspend 함수 성능 측정

코루틴 함수의 성능을 측정할 때:

```kotlin
import com.jparkbro.anipick.firebase.measurePerformanceSuspend

class AnimeRepository @Inject constructor() {

    suspend fun getAnimeDetail(animeId: Int): Result<AnimeDetail> {
        return measurePerformanceSuspend(
            traceName = "fetch_anime_detail",
            attributes = mapOf("anime_id" to animeId.toString())
        ) {
            try {
                val response = apiService.getAnimeDetail(animeId)
                Result.success(response)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
```

### 수동 트레이스 제어

더 세밀한 제어가 필요할 때:

```kotlin
import com.jparkbro.anipick.firebase.PerformanceTracer

class ImageProcessor @Inject constructor() {

    fun processImage(imageUri: Uri) {
        val tracer = PerformanceTracer("process_image")

        // 시작
        tracer.start()

        // 속성 추가
        tracer.putAttribute("image_size", "${imageSize}kb")

        try {
            // 이미지 처리 작업
            val processed = compressImage(imageUri)

            // 메트릭 증가
            tracer.incrementMetric("images_processed", 1L)

        } catch (e: Exception) {
            tracer.putAttribute("error", e.message ?: "Unknown")
            throw e
        } finally {
            // 종료
            tracer.stop()
        }
    }
}
```

## 실전 예시

### LoginViewModel에서 전체 플로우

```kotlin
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val context: Application,
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    fun signInWithGoogle() {
        viewModelScope.launch {
            val firebaseManager = FirebaseManager.getInstance(context)

            // Performance 측정 시작
            val result = measurePerformanceSuspend(
                traceName = "google_sign_in",
                attributes = mapOf("method" to "google")
            ) {
                try {
                    loginUseCase.loginWithGoogle()
                } catch (e: Exception) {
                    // Non-fatal error 기록
                    firebaseManager.logNonFatalError(e)
                    Result.failure(e)
                }
            }

            result.fold(
                onSuccess = { user ->
                    // 로그인 성공 - 사용자 정보 설정
                    firebaseManager.setUserId(user.id)
                    firebaseManager.setUserProperty("login_method", "google")
                    firebaseManager.logCrashlyticsMessage("User logged in: ${user.id}")

                    _uiState.value = LoginUiState.Success
                },
                onFailure = { error ->
                    firebaseManager.logCrashlyticsMessage("Login failed: ${error.message}")
                    _uiState.value = LoginUiState.Error(error.message)
                }
            )
        }
    }
}
```

### Repository에서 API 호출 추적

```kotlin
@Singleton
class AnimeRepositoryImpl @Inject constructor(
    private val context: Application,
    private val apiService: AnimeApiService
) : AnimeRepository {

    override suspend fun getAnimeList(page: Int): Result<List<Anime>> {
        val firebaseManager = FirebaseManager.getInstance(context)

        return measurePerformanceSuspend(
            traceName = "api_get_anime_list",
            attributes = mapOf(
                "page" to page.toString(),
                "endpoint" to "/anime/list"
            )
        ) {
            try {
                firebaseManager.logCrashlyticsMessage("Fetching anime list: page=$page")

                val response = apiService.getAnimeList(page)

                firebaseManager.logCrashlyticsMessage("Successfully fetched ${response.size} animes")

                Result.success(response)
            } catch (e: Exception) {
                firebaseManager.logCrashlyticsMessage("Failed to fetch anime list: ${e.message}")
                firebaseManager.logNonFatalError(e)
                Result.failure(e)
            }
        }
    }
}
```

## 테스트 확인

### Firebase Console에서 확인

1. **Analytics**: [Firebase Console > Analytics > Events](https://console.firebase.google.com)
   - `screen_view` 이벤트 확인

2. **Crashlytics**: [Firebase Console > Crashlytics](https://console.firebase.google.com)
   - 크래시 리포트 및 Non-fatal errors 확인

3. **Performance**: [Firebase Console > Performance](https://console.firebase.google.com)
   - 네트워크 요청 및 커스텀 트레이스 확인

### 디버그 로그 확인

Android Studio Logcat에서 다음 태그 필터링:
- `FirebaseManager`: Firebase 초기화 로그
- `PerformanceTracer`: Performance 트레이스 로그

## 주의사항

1. **디버그 빌드에서는 데이터 수집 안 됨**: 릴리즈 빌드로 테스트하거나 `BuildConfig.DEBUG`를 false로 변경
2. **개인정보 보호**: 민감한 정보(비밀번호, 토큰 등)를 로그나 속성에 포함하지 마세요
3. **성능 영향 최소화**: 너무 많은 커스텀 트레이스는 성능에 영향을 줄 수 있습니다

## 요약

| 서비스 | 자동 수집 | 수동 추가 필요 |
|--------|----------|---------------|
| **Analytics** | 기본 이벤트 | 화면 추적 (`TrackScreen`) |
| **Crashlytics** | 모든 크래시 | Non-fatal errors, 커스텀 로그 |
| **Performance** | 앱 시작, 네트워크 | 커스텀 트레이스 |

**기본 설정만으로도 대부분의 데이터가 자동 수집됩니다!** 🎉
