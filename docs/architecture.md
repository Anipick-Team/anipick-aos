# 아키텍처

단일 액티비티(`MainActivity`) + Compose + Navigation 3 멀티모듈 앱이다. 폴더 위치는 [folder-structure.md](folder-structure.md), 빌드 설정은 [system-configuration.md](system-configuration.md)를 본다.

## 1. 계층

의존은 위에서 아래로만 흐른다. 아래 계층은 위 계층을 import 하지 않는다.

| 계층 | 위치 | 책임 |
|---|---|---|
| 진입 | `app/` | `AniPickApplication`(Timber, Kakao SDK, Koin 모듈 등록), `MainActivity`, `AppNavDisplay`(모든 feature의 `xxxEntry` 연결), 바텀 네비게이션, 딥링크, 인앱 업데이트 |
| 화면 | `feature/{module}/impl/{screen}/` | Root(네비게이션) + Screen(stateless) + ViewModel + State + Action. 구성은 [screen-structure.md](conventions/screen-structure.md) |
| 화면 키 | `feature/{module}/api/` | `@Serializable` NavKey(일부 모듈은 `Navigator.navigateToXxx()` 확장도 둔다). 다른 feature는 이것만 안다 |
| 공용 UI | `core/ui`, `core/designsystem` | 도메인 모델을 아는 컴포넌트/이펙트(`core/ui`), 토큰·테마·원시 컴포넌트(`core/designsystem`). 경계는 [component-placement.md](conventions/component-placement.md) |
| 네비게이션 | `core/navigation` | `NavigationState`(탑레벨 스택 + 탭별 서브 스택), `Navigator`(`navigate`, `navigateAndClearStack` 등) |
| 데이터 | `core/data` | Repository 인터페이스 + Impl. 네트워크 결과를 모델로 넘기고, 여러 화면이 공유하는 값을 `Flow`로 노출한다 |
| 원격 | `core/network` | `HttpClientFactory`, `HttpClientExt`, `XxxNetworkDataSource`(인터페이스) + `KtorXxxNetworkDataSource`, 응답 DTO와 `toXxx()` 매퍼 |
| 로컬 | `core/datastore`, `core/database` | DataStore(토큰, 사용자 닉네임·이메일, 최근 검색·최근 본 작품). Room 모듈은 아직 비어 있다 |
| 기반 | `core/model`, `core/common` | 도메인 모델, `Result`/`DataError`/`toDisplayMessage`, `TokenProvider`. 둘 다 순수 JVM 모듈 |

경계는 Gradle 모듈 의존으로 지킨다. `feature/impl`은 다른 `feature/impl`을 의존에 넣지 않고, `core/designsystem`은 `core/model`을 쓰지 않는다.

## 2. 요청 흐름

```text
Screen → onAction → ViewModel → XxxRepository → XxxNetworkDataSource
   → HttpClient.get/post(...)  (HttpClientExt)
   → safeCall → responseToResult(ApiResponse<T>)
   → Result<Dto, DataError.Network> → Repository가 dto.toXxx() → Result<Model, ...>
   → ViewModel이 _state.update { ... } → Screen 재구성
```

1. ViewModel은 `viewModelScope.launch`에서 Repository의 `suspend` 함수를 부르고 `onSuccess`/`onFailure`로 State를 갱신한다.
2. `HttpClientExt`의 `get`/`post`/`patch`/`put`/`delete`/`postMultipart`가 `constructRoute`로 `BuildConfig.BASE_URL`을 붙이고 `safeCall`로 감싼다. 경로는 `/users/me/profile`처럼 `BASE_URL`을 뺀 값으로 쓴다.
3. 응답은 공통 봉투 `ApiResponse<T>`(`code`, `value`, `result`, `errorReason`, `errorValue`)다. HTTP 상태가 아니라 **`code == 200`이 성공**이다.
4. Repository는 DTO를 `toXxx()`로 모델로 바꾸기만 한다(`?:` 디폴팅 없음, [dto-model-nullability.md](conventions/dto-model-nullability.md)).
5. 서버 변경 뒤 재조회까지 한 묶음이어야 하는 호출은 `runUncancellable`(`core/data/util`)로 감싸 화면이 사라져도 끝까지 실행한다.

## 3. 오류 처리

| 실패 원인 | 결과 |
|---|---|
| `code != 200` | `DataError.Network.Api(code, message = errorValue, reason = errorReason)` |
| `UnresolvedAddressException` | `DataError.Network.NO_INTERNET` |
| `SerializationException` | `DataError.Network.SERIALIZATION` |
| 그 밖의 예외 (`CancellationException`은 다시 던짐) | `DataError.Network.UNKNOWN` |

- 화면에 보일 문구는 `DataError.Network.toDisplayMessage()` 하나로 만든다. `Api`면 서버 메시지, 없으면 "알 수 없는 오류가 발생했습니다.", `NO_INTERNET`이면 "네트워크 연결을 확인해주세요.".
- `code`별로 다른 동작(다이얼로그, 특정 필드 인라인 에러)이 필요하면 ViewModel에서 `is DataError.Network.Api` 분기를 직접 두고 나머지만 `toDisplayMessage()`에 넘긴다. code 목록은 [error-codes.md](reference/error-codes.md).
- 일회성 알림은 `GlobalSnackbarManager`(앱 전역 큐, `MainActivity`가 그림), 화면에 남는 오류는 State의 `error` 필드로 표시한다.
- 실패는 `safeCall`/`responseToResult`가 `Timber.e`로 남긴다.

## 4. 인증

- 토큰은 `TokenDataStore`(`TokenProvider` 구현)가 DataStore에 Android Keystore(AES/GCM)로 암호화해 저장한다. ViewModel·화면은 토큰을 읽지 않는다.
- DataStore 파일 이름과 키는 v1과 같다(`jwt_tokens_secure`, `anipick_preferences`, `AnipickDataStores.kt`). 바꾸면 업데이트한 사용자의 로그인·검색어가 사라진다.
- `HttpClientFactory`의 Ktor `Auth`(bearer) 플러그인이 모든 요청에 액세스 토큰을 붙인다.
- 다음 응답이면 리프레시 토큰으로 재발급 후 원 요청을 재시도한다: HTTP 401/403, 또는 2xx인데 `code`가 `119`(토큰 무효)·`121`(토큰 만료).
- 재발급은 `Auth` 플러그인이 없는 전용 클라이언트(`refreshHttpClient`)로 `/tokens/refresh`를 부른다. Ktor 3.1.3+의 `markAsRefreshTokenRequest()`가 Authorization 헤더를 지우기 때문이다. 실패하면 토큰을 지운다.
- 로그인 여부는 `TokenProvider.isLoggedIn`(`Flow<Boolean>`)으로 판단한다.

## 5. 네비게이션

- 화면은 `NavKey`로 식별한다. 각 feature의 `navigation/{Module}Navigation.kt`가 `EntryProviderScope<NavKey>.xxxEntry(navigator, ...)`를 제공하고 `AppNavDisplay`가 모은다.
- 탑레벨 키(스플래시·로그인·홈·랭킹·탐색·마이페이지, `TopLevelNavItem`)마다 서브 백스택을 따로 둔다. 바텀 탭은 그중 홈·랭킹·탐색·마이페이지 4개다. `Navigator.navigate(key)`: 현재 탭 재선택이면 서브 스택 초기화, 다른 탭이면 탭 전환, 그 외는 현재 서브 스택에 push.
- 탭 루트끼리, Search Main↔Detail은 fade, 그 외는 slide 전환이다(`AppNavDisplay`, `CONTENT_KEY` 상수로 구분).
- 딥링크는 `DeepLinkViewModel`이 보관하고, Splash 완료 콜백(`AppNavDisplay`)이 홈으로 전환한 뒤 `parseDeepLink` 결과를 push한다(뒤로가기 시 홈으로 돌아오게).
- 화면 안 네비게이션 규칙(Root 콜백 vs Event, `Navigation` 액션)은 [screen-structure.md](conventions/screen-structure.md).

## 6. 화면 간 공유 상태

- 여러 화면이 같이 보여주는 값(닉네임, 이메일, 마이페이지 프로필 등)은 Repository가 `Flow`/`StateFlow`로 노출하고 ViewModel이 구독한다. 값을 바꾸는 Repository 함수가 그 Flow의 소스(DataStore 또는 내부 `MutableStateFlow`)도 같이 갱신한다.
- Repository는 Koin `single`이라 이 상태가 앱 전체에서 하나다.

## 7. DI

- Koin. 모듈은 `AniPickApplication.startKoin`에서 등록한다: `appModule`, `networkModule`, `datastoreModule`, `dataModule`, `uiModule`, feature별 `xxxModule`.
- core는 `singleOf(::Impl) { bind<Interface>() }` 형태, feature는 `viewModel { params -> ... }`로 등록하고 Root에서 `koinViewModel(parameters = { parametersOf(...) })`로 받는다.

## 8. 설계 결정

| 결정 | 이유 |
|---|---|
| feature를 `api`/`impl`로 나눈다 | 다른 feature는 목적지 키만 알면 된다. `impl`끼리 의존하지 않아 빌드·변경 범위가 모듈 안에 머문다 |
| `coerceInputValues`를 켜지 않고 DTO를 nullable로 둔다 | `null`과 빈 값을 구분해 화면이 숨김/`-`를 고를 수 있게 한다 |
| 성공 판단을 `ApiResponse.code`로 한다 | 백엔드가 비즈니스 실패도 HTTP 2xx로 내려준다 |
| 오류 문구 변환을 `toDisplayMessage()` 한 곳에 둔다 | 서버 메시지 우선 규칙과 기본 문구를 화면마다 반복하지 않는다 |
| 스낵바를 전역 매니저로 띄운다 | 화면이 닫히며 Event가 유실되는 문제 없이 큐로 표시한다 |
| 문구를 Compose 리터럴로 쓴다 | 다국어 계획이 없다 |
