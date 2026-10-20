# Git 규칙

이 저장소의 `git log`, `.gitignore`를 기준으로 한다.

## 1. 저장소

| 항목 | 값 |
|---|---|
| 원격 | `origin` = `https://github.com/Anipick-Team/anipick-aos.git` |
| 기본 브랜치 | `main` |
| 통합 브랜치 | `dev` (`main`에서 분기) |
| 작업 브랜치 | `v3` 등 버전 단위 브랜치 |

## 1-1. 브랜치 흐름

```text
작업 브랜치(v3) → dev → main
```

- 작업은 작업 브랜치에서 커밋한다. `main`, `dev`에 직접 커밋하지 않는다.
- 작업 브랜치는 먼저 `dev`에 머지해 확인하고, 확인이 끝나면 `dev`를 `main`에 머지한다.
- 머지 전에 대상 브랜치를 `git fetch`로 최신화하고, 충돌은 작업 브랜치에서 해결한다.
- 머지·Push는 사용자 요청이 있을 때만 한다.

## 2. 커밋 메시지

이력의 형식

```text
[Feat] 커뮤니티 상세 공유 링크 추가
```

- 제목은 `[Type] 요약` 한 줄. 이력에 있는 type은 `[Feat]`, `[Refactor]`이고, 다른 변경은 같은 형태로 `[Fix]`, `[Docs]`, `[Chore]`를 쓴다.
- 요약은 한국어로, 무엇을 바꿨는지 보이게 쓰고 마침표를 붙이지 않는다. 이력에는 `[Feat]`만 있는 커밋이 많지만 새 커밋은 요약을 붙인다.
- 본문이 필요하면 빈 줄 뒤에 `- ` 목록으로 바뀐 결과를 적는다. 이력·경위는 쓰지 않는다.

## 3. 커밋 범위

- 한 커밋은 한 목적만 담는다(기능, 수정, 리팩터링, 문서, 설정 중 하나). 기능 변경에 서식 정리나 무관한 정리를 섞지 않는다.
- `git add -A`, `git add .` 대신 `git add <경로>`로 담는다.
- 커밋 전에 `git status`와 `git diff`로 의도하지 않은 파일(`local.properties`, `keystore.properties`, `.idea/`, `build/`)이 없는지 본다.
- 코드 변경은 커밋 전에 `./gradlew compileDebugKotlin`이 통과해야 한다. 실행하지 못했으면 `NOT VERIFIED — <이유>`로 알린다.

## 4. 하지 않는 것

- 요청 없이 Commit, Push, Rebase, Reset, Clean 하지 않는다.
- `git push --force`, `git reset --hard`, 파괴적 `git clean`, 브랜치 삭제는 사용자 승인 없이 하지 않는다.
- 원격 변경 확인은 `git fetch` 후 `git status`, `git log`로 한다.

## 5. 커밋하지 않는 것

`.gitignore`가 제외하는 것

| 항목 | 설명 |
|---|---|
| `local.properties` | SDK 경로, `WEB_CLIENT_ID`, `KAKAO_APP_KEY` |
| `keystore.properties` | release 서명 정보 |
| `.gradle`, `/build`, `.kotlin`, `.cxx`, `/captures` | 빌드 산출물·캐시 |
| `.idea/*` (`codeStyles` 제외), `*.iml` | IDE 설정 |
| `.DS_Store` | macOS |

- 키스토어 파일, 비밀번호, API 키는 코드·문서·커밋 메시지·채팅에 넣지 않는다. 노출됐다면 폐기하고 새 값으로 바꾼다.
- QA 엑셀(`docs/qa/generate_xlsx.py` 산출물)은 저장소 밖에 저장하고 커밋하지 않는다([qa-testcase-format.md](conventions/qa-testcase-format.md)).
