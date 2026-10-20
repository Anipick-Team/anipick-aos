# 폴더 구조

계층의 책임은 [architecture.md](architecture.md), 화면 디렉터리 구성은 [screen-structure.md](conventions/screen-structure.md), 화면에서 뺀 컴포넌트 위치는 [component-placement.md](conventions/component-placement.md)를 본다.

## 1. 저장소 루트

```
AniPick/
├── CLAUDE.md                 # Claude Code 세션이 읽는 저장소 규칙
├── README.md                 # 서비스 소개, 기술 스택
├── docs/                     # 이 문서들 (conventions/, reference/, qa/)
├── app/                      # 앱 모듈
├── core/                     # 공용 모듈 (§2)
├── feature/                  # 기능 모듈 (§3)
├── build-logic/convention/   # convention 플러그인 (모듈 공통 Gradle 설정)
├── gradle/libs.versions.toml # 라이브러리·SDK·앱 버전
├── settings.gradle.kts       # 모듈 include
├── oss_sources/              # OSS 라이선스 표시용 소스
├── local.properties          # SDK 경로 + 로그인 키 (git 제외)
└── keystore.properties       # release 서명 정보 (git 제외)
```

패키지 루트는 모듈마다 `com.jparkbro.core.{module}`, `com.jparkbro.{feature}.api`, `com.jparkbro.{feature}.impl`, 앱은 `com.jparkbro.anipick`.

## 2. `core/`

| 모듈 | 플러그인 | 패키지 |
|---|---|---|
| `common` | `anipick.jvm.library` | `result/`(Result, DataError, toDisplayMessage), `auth/`(TokenProvider) |
| `model` | `anipick.jvm.library` | 도메인별(`anime/`, `review/`, ...), `pagination/`(CursorPage), `validation/`(PatternValidator) |
| `network` | `anipick.android.library` + `anipick.jvm.ktor` | 루트에 `HttpClientFactory`, `HttpClientExt`, `ApiResponse`. 도메인별 `{domain}/`에 `XxxNetworkDataSource` + `KtorXxxNetworkDataSource`, `{domain}/dto/`에 `*Response`/`*Request`와 매퍼. `common/`(Cursor, 메타데이터), `di/` |
| `datastore` | `anipick.android.library` | `XxxDataStore` 인터페이스 + `XxxDataStoreImpl`, `TokenDataStore`, `di/` |
| `database` | `anipick.android.library` + `anipick.android.room` | 비어 있음 |
| `data` | `anipick.android.library` | 도메인별 `{domain}/`에 `XxxRepository` + `XxxRepositoryImpl`, `util/`, `di/` |
| `designsystem` | `anipick.android.library.compose` | `theme/`, `component/`, `icon/`, `model/`(컴포넌트 파라미터 타입), `transformation/`, `extension/modifier/`, `extension/context/` |
| `ui` | `anipick.android.library.compose` | `component/`(`AniPick*`), `effect/`, `util/`, `validation/`, `GlobalSnackbarManager`, `di/` |
| `navigation` | `anipick.android.library.compose` | `Navigator`, `NavigationState` |

## 3. `feature/`

모듈: `auth`, `splash`, `home`, `ranking`, `explore`, `mypage`, `search`, `catalog`, `community`, `review`.

```
feature/{module}/api/src/main/java/com/jparkbro/{module}/api/
└── {Module}NavKey.kt

feature/{module}/impl/src/main/java/com/jparkbro/{module}/impl/
├── di/{Module}Module.kt
├── navigation/{Module}Navigation.kt
├── components/               # 모듈 안 여러 화면이 공유 (internal)
└── {screen}/                 # Action / State / ViewModel / Screen (+ Event, components/)
feature/{module}/impl/src/main/keepRules/rules.keep   # 모듈별 R8 keep 규칙
```

`api`는 `anipick.android.feature.api`(→ `core/navigation`), `impl`은 `anipick.android.feature.impl`(→ `core/data`, `core/model`, `core/ui`, `core/designsystem`) 플러그인을 쓴다.

## 4. 새 파일은 어디에 두나

| 만드는 것 | 위치 | 이름 |
|---|---|---|
| API 호출 | `core/network/{domain}/` — 인터페이스와 Ktor 구현 한 쌍 | `XxxNetworkDataSource`, `KtorXxxNetworkDataSource`. 함수 주석은 `/** 화면/동작 - METHOD /path */` |
| 응답·요청 DTO | `core/network/{domain}/dto/` | `XxxResponse`, `XxxRequest`. 필드 규칙은 [dto-model-nullability.md](conventions/dto-model-nullability.md) |
| DTO → 모델 매퍼 | 해당 DTO 파일 하단 | `fun XxxResponse.toXxx(): Xxx` |
| 도메인 모델 | `core/model/{domain}/` | 명사(`Anime`), 페이지 `~Page`, 묶음 `~Result`. 커서 목록은 `CursorPage<T>` 재사용 |
| 요청용 enum | `core/model/{domain}/` | `enum class Xxx(val apiValue: String)` |
| Repository | `core/data/{domain}/` | `XxxRepository` + `XxxRepositoryImpl`, `dataModule`에 `singleOf(::XxxRepositoryImpl) { bind<XxxRepository>() }` |
| 로컬 키-값 저장 | `core/datastore/` | `XxxDataStore` + `XxxDataStoreImpl`, `datastoreModule`에 등록 |
| 새 feature | `feature/{module}/api`, `impl` + `settings.gradle.kts` include + `app/build.gradle.kts` 의존 + `AniPickApplication` 모듈 등록 + `AppNavDisplay` entry | [screen-structure.md](conventions/screen-structure.md) |
| 새 화면 | `feature/{module}/impl/{screen}/` | `{Screen}Action/State/ViewModel/Screen.kt` |
| 화면에서 뺀 컴포넌트·이펙트·유틸 | [component-placement.md](conventions/component-placement.md) 표를 따른다 | |
| 라이브러리·버전 | `gradle/libs.versions.toml` | 사용자 승인 후 추가 |
| 모듈 공통 Gradle 설정 | `build-logic/convention/` | `*ConventionPlugin.kt` |
| QA 테스트케이스 | `docs/qa/testcases/{module}/{screen}.md` | [qa-testcase-format.md](conventions/qa-testcase-format.md) |

## 5. 이름 규칙

| 대상 | 규칙 | 예 |
|---|---|---|
| 화면 파일 | `{Module}{Screen}` + 역할 접미사 | `CatalogStudioScreen.kt`, `CatalogStudioViewModel.kt` |
| Root / Screen 컴포저블 | `XxxRoot`(`internal`), `XxxScreen`(`private`) | `CatalogStudioRoot` |
| Action | `On{무엇}{동사}` | `OnBackClick`, `OnLoadMore` |
| NavKey | `sealed interface {Module}NavKey : NavKey`, 케이스는 화면 이름 | `CatalogNavKey.Studio` |
| CONTENT_KEY 상수 | `{MODULE}_{SCREEN}_CONTENT_KEY` | `CATALOG_STUDIO_CONTENT_KEY` |
| Koin 모듈 | `{module}Module` | `catalogModule`, `dataModule` |
| 공개 공용 컴포저블 | `AniPick` 접두사 | `AniPickAnimeCard`, `AniPickTitleTopAppBar` |
| feature 내부 컴포넌트 | 접두사 없이 역할 | `StudioAnimeSectionGrid` |
| 상수 | `private const val` 대문자 스네이크, ViewModel은 `companion object` | `PAGE_SIZE` |
