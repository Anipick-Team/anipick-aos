plugins {
    alias(libs.plugins.anipick.android.feature.impl)
}

android {
    namespace = "com.jparkbro.auth.impl"
}

dependencies {
    // Kakao
    implementation(libs.kakao.user)

    // Google Login: v1 core:domain의 GoogleLoginUseCase를 그대로 쓴다 (v2 구글 로그인 경로는 동작하지 않음)
    implementation(projects.core.domain)

    implementation(projects.feature.auth.api)
    implementation(projects.feature.home.api)
}