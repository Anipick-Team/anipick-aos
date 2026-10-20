# 빌드·런타임 구성

앱이 연결되는 외부 구성 요소, 빌드 설정, 설정 키를 정리한다. 계층과 요청 흐름은 [architecture.md](architecture.md)를 본다.

## 1. 구성 요소

| 구성 요소 | 역할 | 비고 |
|---|---|---|
| 앱 | Compose 단일 액티비티 앱 | `applicationId` `com.jparkbro.anipick` |
| 백엔드 API | `https://anipick.p-e.kr/api` | 모든 응답이 `ApiResponse` 봉투. debug/release 같은 서버 |
| 딥링크 | `https://anipick.p-e.kr/app/...` | `AndroidManifest`의 intent-filter(`pathPrefix="/app"`), `DeepLinkParser` |
| Google 로그인 | Credential Manager + `googleid` | `WEB_CLIENT_ID` 필요 |
| Kakao 로그인 | Kakao SDK (`kakao${KAKAO_APP_KEY}://oauth`) | `KAKAO_APP_KEY` 필요. `AniPickApplication`에서 `KakaoSdk.init` |
| 인앱 업데이트 | Play In-App Update | `app/update/` |
| OSS 라이선스 | `play-services-oss-licenses` | `oss_sources/` |

## 2. 버전과 SDK

`gradle/libs.versions.toml`의 `projectXxx` 키가 정본이다. convention 플러그인이 읽어 모든 모듈에 적용한다.

| 키 | 값 | 쓰는 곳 |
|---|---|---|
| `projectApplicationId` | `com.jparkbro.anipick` | `AndroidApplicationConventionPlugin` |
| `projectVersionCode` / `projectVersionName` | `63` / `2.0.1` | `versionCode`/`versionName`, `BuildConfig.APP_VERSION` |
| `projectMinSdkVersion` | `30` | `KotlinAndroid.kt` |
| `projectTargetSdkVersion` / `projectCompileSdkVersion` | `37` / `37` | |

- Java/JVM 타깃 17, core library desugaring 켜짐.
- 릴리스할 때 버전은 `projectVersionCode`(+1)와 `projectVersionName`만 바꾼다. 다른 곳에 버전을 적지 않는다.

## 3. BuildConfig 필드

`build-logic/convention/.../BuildTypes.kt`가 debug·release에 같은 이름으로 넣는다.

| 필드 | 값 출처 | 쓰는 곳 |
|---|---|---|
| `BASE_URL` | `ANIPICK_HOST` 상수 + `/api` | `constructRoute` |
| `DEEP_LINK_BASE_URL` | `ANIPICK_HOST` 상수 + `/app` | 공유 링크 생성(`CatalogAnimeScreen`, `CommunityDetailScreen`) |
| `APP_VERSION` | `projectVersionName` | 설정 화면 앱 버전(`SettingAppSection`) |
| `WEB_CLIENT_ID` | `local.properties` | Google 로그인 |
| `KAKAO_APP_KEY` | `local.properties` (manifest placeholder로도 들어감) | Kakao SDK |

API 도메인을 바꿀 때는 `BuildTypes.kt`의 `ANIPICK_HOST` 하나와 `AndroidManifest`의 딥링크 `host`를 같이 바꾼다.

## 4. 로컬 설정 파일 (git 제외)

| 파일 | 키 | 없을 때 |
|---|---|---|
| `local.properties` | `sdk.dir`, `WEB_CLIENT_ID`, `KAKAO_APP_KEY` | 로그인 키는 빈 문자열로 빌드되고 해당 소셜 로그인이 실패한다 |
| `keystore.properties` | `storeFile`, `storePassword`, `keyAlias`, `keyPassword` | release가 서명 없이 빌드된다 |

- 값은 코드·문서·커밋·채팅에 옮기지 않는다. 이 문서에도 키 이름만 적는다.
- `app/google-services.json`은 git에 추적되지만 google-services Gradle 플러그인은 적용돼 있지 않다.

## 5. 빌드 타입

| 타입 | 설정 |
|---|---|
| `debug` | Ktor `Logging`(LogLevel.ALL, Timber `Ktor` 태그), `Timber.DebugTree` |
| `release` | `isMinifyEnabled`·`isShrinkResources` 켜짐, `proguard-android-optimize.txt` + `app/proguard-rules.pro` + 모듈별 `src/main/keepRules/rules.keep`. `keystore.properties`가 있으면 release 서명 |

R8로 깨지는 직렬화·리플렉션 클래스가 생기면 그 모듈의 `keepRules/rules.keep`에 규칙을 추가한다.

## 6. Gradle

- `gradle.properties`: `org.gradle.configuration-cache=true`, `kotlin.code.style=official`, JVM `-Xmx2048m`.
- `settings.gradle.kts`: `TYPESAFE_PROJECT_ACCESSORS`(`projects.core.data` 형태), `build-logic` includeBuild, Kakao Maven 저장소.
- convention 플러그인 ID는 `anipick.*`(`anipick.android.library`, `anipick.android.feature.impl` 등). 새 모듈은 `build.gradle.kts`에 플러그인 alias만 적고 공통 설정을 다시 쓰지 않는다.
