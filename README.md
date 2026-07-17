# AniPick (애니픽)

애니메이션 정보 탐색, 랭킹, 추천, 리뷰까지 한 곳에서 즐기는 안드로이드 애니메이션 커뮤니티 앱입니다.

![Platform](https://img.shields.io/badge/platform-Android-3DDC84?logo=android)
![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?logo=kotlin&logoColor=white)
![Min SDK](https://img.shields.io/badge/minSdk-28-blue)
![Version](https://img.shields.io/badge/version-1.2.9-informational)

## 소개

AniPick은 애니메이션 정보를 탐색하고, 랭킹과 추천을 통해 새로운 작품을 발견하며, 직접 리뷰를 남기고 다른 사용자와 소통할 수 있는 서비스입니다. 현재 정식 출시되어 실사용자를 대상으로 운영 및 유지보수하고 있습니다.

### 주요 기능
- 애니메이션/캐릭터/시리즈/성우/제작사 정보 탐색 및 검색
- 랭킹, 추천 기반 콘텐츠 발견
- 리뷰 작성 및 마이페이지를 통한 개인 활동 관리
- 이메일 회원가입/로그인, 카카오·구글 소셜 로그인 지원

## 기술 스택

**Language & UI**
- Kotlin
- Jetpack Compose, Material 3
- Navigation Compose
- Splash Screen API

**Architecture & DI**
- Multi-module 기반 Clean Architecture (Presentation - Domain - Data)
- Hilt, KSP

**비동기 처리**
- Kotlin Coroutines, Flow

**네트워크 & 데이터**
- Retrofit2, OkHttp
- Kotlinx Serialization
- DataStore (Preferences)
- Coil3 (이미지 로딩)

**인증**
- JWT 기반 자체 이메일 인증
- Google Identity Services (Credential Manager)
- Kakao Login SDK

**모니터링 & 배포**
- Firebase Crashlytics, Firebase Performance Monitoring
- Play In-App Update
- OSS Licenses Plugin

## 아키텍처 / 프로젝트 구조

`core` 모듈에 공통 기능을, `feature` 모듈에 화면 단위 기능을 분리한 멀티모듈 구조입니다.

```
AniPick
├── app                         # 앱 진입점, 모듈 조립
│
├── core
│   ├── model                   # 공통 도메인 모델
│   ├── network                 # Retrofit API, DataSource
│   ├── data                    # Repository 구현체
│   ├── domain                  # UseCase
│   ├── datastore               # 로컬 데이터 저장 (토큰 등)
│   ├── ui                      # 공통 Compose 컴포넌트, 테마
│   └── firebase                # Crashlytics, Performance 설정
│
└── feature
    ├── auth                    # 로그인/회원가입/비밀번호 찾기
    │   ├── login
    │   ├── email/login, email/register
    │   ├── findpassword/verification, findpassword/reset
    │   └── preferencesetup
    │
    └── main
        ├── shell               # 하단 네비게이션 (홈/랭킹/탐색/마이페이지)
        │   ├── home
        │   ├── ranking
        │   ├── explore
        │   └── mypage
        ├── info                # 애니메이션/캐릭터/시리즈/추천 상세
        ├── actor / studio       # 성우/제작사 정보
        ├── review              # 리뷰
        └── search              # 검색
```

각 feature 모듈은 `core:domain`, `core:data`를 통해 비즈니스 로직과 데이터에 접근하며, `core:ui`의 공통 컴포넌트와 테마를 공유합니다.
