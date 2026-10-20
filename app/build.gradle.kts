plugins {
    alias(libs.plugins.anipick.android.application.compose)
    alias(libs.plugins.kotlin.serialization)

    // ─── v2 미사용 (v1 레거시) ───────────────────────────────
    // Firebase - Application 모듈에만 필요
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.perf)
}

android {
    namespace = "com.jparkbro.anipick"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Splash Screen
    implementation(libs.androidx.core.splashscreen)

    // oss-licenses
    implementation(libs.androidx.appcompat)
    implementation(libs.play.services.oss.licenses)

    // Kakao (KakaoSdk.init 호출용)
    implementation(libs.kakao.user)

    // In App Update
    implementation(libs.app.update)
    implementation(libs.app.update.ktx)

    // Module
    implementation(projects.core.common)
    implementation(projects.core.data)
    implementation(projects.core.datastore)
    implementation(projects.core.designsystem)
    implementation(projects.core.firebase)
    implementation(projects.core.model)
    implementation(projects.core.navigation)
    implementation(projects.core.network)
    implementation(projects.core.ui)

    implementation(projects.feature.splash.api)
    implementation(projects.feature.splash.impl)
    implementation(projects.feature.auth.api)
    implementation(projects.feature.auth.impl)
    implementation(projects.feature.home.api)
    implementation(projects.feature.home.impl)
    implementation(projects.feature.ranking.api)
    implementation(projects.feature.ranking.impl)
    implementation(projects.feature.explore.api)
    implementation(projects.feature.explore.impl)
    implementation(projects.feature.mypage.api)
    implementation(projects.feature.mypage.impl)
    implementation(projects.feature.review.api)
    implementation(projects.feature.review.impl)
    implementation(projects.feature.search.api)
    implementation(projects.feature.search.impl)
    implementation(projects.feature.catalog.api)
    implementation(projects.feature.catalog.impl)
    implementation(projects.feature.community.api)
    implementation(projects.feature.community.impl)

}
