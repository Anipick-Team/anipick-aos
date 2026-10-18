# AniPick

애니메이션 탐색·랭킹·추천·리뷰·커뮤니티 안드로이드 앱. 서비스 소개는 [README.md](./README.md)를 본다.

## 저장소

- 원격: `origin` = `https://github.com/Anipick-Team/anipick-aos.git`, 기본 브랜치 `main`, 통합 브랜치 `dev`
- Kotlin 2.4 / AGP 9.3 / Jetpack Compose (Material 3) / Navigation 3 / Koin 4 / Ktor 3 / kotlinx.serialization / DataStore / Coil 3
- `minSdk 30`, `targetSdk`·`compileSdk 37`, Java 17. 버전은 `gradle/libs.versions.toml`, 모듈 공통 설정은 `build-logic/convention`.
- 테스트 소스는 없다. 검증은 컴파일과 Compose Preview다.

## 명령

```bash
./gradlew compileDebugKotlin                        # 전체 컴파일 (기본 검증)
./gradlew :feature:catalog:impl:compileDebugKotlin  # 모듈 단위 컴파일
./gradlew assembleDebug                             # 디버그 APK
```

UI 변경은 **컴파일 + Compose Preview로 확인한다.** 에뮬레이터/실기기를 띄워 직접 눌러보지 않는다 — 실기기 확인이 필요하면 사용자가 직접 한다.
확인하지 못한 항목은 완료로 쓰지 않고 `NOT VERIFIED — <이유>`로 적는다.

## 어디에 무엇이 있나

```
app/                      # Application(Koin 시작), MainActivity, AppNavDisplay, 바텀 네비게이션, 딥링크, 인앱 업데이트
core/
├── common/               # Result, DataError, toDisplayMessage, TokenProvider (JVM)
├── model/                # 도메인 모델 (JVM)
├── network/              # Ktor DataSource + 응답 DTO + DTO→모델 매퍼
├── datastore/            # 토큰·사용자·최근 검색 DataStore
├── database/             # Room (아직 비어 있음)
├── data/                 # Repository (인터페이스 + Impl)
├── designsystem/         # 테마·토큰·원시 컴포넌트·Modifier 확장 (도메인 모델 모름)
├── ui/                   # 도메인 모델을 아는 공용 컴포넌트/이펙트/유틸, GlobalSnackbarManager
└── navigation/           # Navigator, NavigationState
feature/{module}/api/     # NavKey만 — 다른 feature는 여기에만 의존한다
feature/{module}/impl/    # 화면 구현 — impl끼리는 서로 참조하지 않는다
```

의존 방향: `app → feature/impl → feature/api, core/*`. `core/data → core/network, core/datastore`. 모든 안드로이드 라이브러리 모듈은 convention 플러그인으로 `core/model`·`core/common`을 받는다. 역방향 의존은 없다.
계층과 요청 흐름은 [docs/architecture.md](./docs/architecture.md), 새 파일 위치는 [docs/folder-structure.md](./docs/folder-structure.md).

## 작업 전에 읽을 것

작업 종류에 해당하는 문서를 **코드를 고치기 전에** 확인한다. 여기 있는 규칙이 기존 코드와 어긋나 보이면, 문서가 기준이고 그 코드가 아직 정리되지 않은 것이다.

| 무슨 작업인가 | 문서 |
|---|---|
| 새 화면 추가, ViewModel/State/Action 설계, 네비게이션 연결 | [docs/conventions/screen-structure.md](./docs/conventions/screen-structure.md) |
| 커진 컴포넌트를 어디로 뺄지, `core/ui` vs `core/designsystem` 판단 | [docs/conventions/component-placement.md](./docs/conventions/component-placement.md) |
| 새 API 응답 DTO/모델 추가, 매퍼 작성 | [docs/conventions/dto-model-nullability.md](./docs/conventions/dto-model-nullability.md) |
| 주석 작성 | [docs/conventions/comment-style.md](./docs/conventions/comment-style.md) |
| 화면 개발 완료 후 QA 테스트케이스 작성 | [docs/conventions/qa-testcase-format.md](./docs/conventions/qa-testcase-format.md) |
| 서버 비즈니스 에러 code별 노출 메시지 확인 | [docs/reference/error-codes.md](./docs/reference/error-codes.md) |
| 새 엔드포인트·Repository 추가, 에러/토큰 처리 흐름 파악 | [docs/architecture.md](./docs/architecture.md) |
| 새 파일·패키지 위치, 이름 규칙 | [docs/folder-structure.md](./docs/folder-structure.md) |
| BuildConfig, `local.properties` 키, 서명, 버전 올리기 | [docs/system-configuration.md](./docs/system-configuration.md) |
| 커밋, 푸시 | [docs/git-rules.md](./docs/git-rules.md) |

## 지켜야 할 규칙

- **에러 문구는 `toDisplayMessage()`를 거친다.** `error.toString()`을 State에 넣으면 `Api(code=500, ...)`이 그대로 화면에 렌더된다.
- **응답 DTO 필드는 전부 `Type? = null`.** 서버가 명시적 `null`을 내려주고 `coerceInputValues`를 껐기 때문에, non-null로 두면 그 필드가 null인 응답에서 역직렬화가 터진다.
- **문구는 Compose에 한국어 리터럴로 직접 쓴다.** 다국어 계획이 없어 `strings.xml`을 쓰지 않는다.
- **빈 `XxxEvent.kt`를 미리 만들지 않는다.** Event는 ViewModel이 비동기 결과로 목적지를 정할 때만 만든다.
- **Root가 처리하는 액션은 `XxxAction.Navigation` 하위로 선언한다.** Root의 안쪽 `when`에 `else`를 넣으면 새 네비게이션 액션이 조용히 무반응이 되는 걸 컴파일러가 못 잡는다.
- 모든 API 호출은 `core/network`의 `HttpClientExt`(`get`/`post`/...)를 거쳐 `Result<T, DataError.Network>`로 받는다. ViewModel은 Repository만 부른다.
- 일회성 스낵바는 `GlobalSnackbarManager.showSnackbar`로 띄운다. Event로 쏘지 않는다.
- 로그는 `Timber`를 쓴다. `Log`/`println`을 쓰지 않는다. `DebugTree`는 debug 빌드에서만 심긴다.
- 토큰 재발급(`119`/`121`, 401/403)은 `HttpClientFactory`가 전역으로 처리한다. 화면·Repository에서 토큰을 직접 다루지 않는다.
- 최소 변경만 한다. 관련 없는 리팩터링, 서식 일괄 변경, 의존성 추가를 하지 않는다. 새 라이브러리는 사용자 승인 후 `libs.versions.toml`에 추가한다.
- `local.properties`, `keystore.properties`, 키스토어 파일의 값을 출력하거나 코드·문서·커밋에 넣지 않는다.

## Git

- 요청 없이 Commit / Push / Rebase / Reset / Clean 하지 않는다. force push는 사용자 승인 없이 하지 않는다.
- 작업 브랜치(`v3` 등) → `dev` → `main` 순서로 머지한다. `main`, `dev`에 직접 커밋하지 않는다.
- 커밋 메시지 형식은 `[Type] 요약`(`[Feat]`, `[Refactor]` 등). 자세한 규칙은 [docs/git-rules.md](./docs/git-rules.md).
- 커밋은 `git add <경로>`로 담는다. 작업 전후 `git status`, 커밋 전 `git diff`를 본다.
