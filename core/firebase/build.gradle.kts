plugins {
    alias(libs.plugins.anipick.android.library)
}

android {
    namespace = "com.jparkbro.firebase"
}

dependencies {
    // ─── v2 미사용 (v1 레거시) ───────────────────────────────
    // v1 모듈 제거 시 함께 삭제.
    // Firebase: v2 미사용. api로 노출하여 다른 모듈에서도 사용 가능
    api(platform("com.google.firebase:firebase-bom:34.6.0"))
    api("com.google.firebase:firebase-analytics")
    api("com.google.firebase:firebase-crashlytics")
    api("com.google.firebase:firebase-perf")
}
