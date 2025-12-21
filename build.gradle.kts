// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false

    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false

    id("com.google.dagger.hilt.android") version "2.57.2" apply false
    id("com.google.devtools.ksp") version "2.2.21-2.0.4" apply false

    // Firebase
    id("com.google.gms.google-services") version "4.4.4" apply false
    id("com.google.firebase.crashlytics") version "3.0.6" apply false
    id("com.google.firebase.firebase-perf") version "2.0.2" apply false
}

buildscript {
    val targetSdk = 36
    val minSdk = 28
    val versionCode = 26
    val versionName = "1.0.10"

    extra.apply {
        set("targetSdk", targetSdk)
        set("minSdk", minSdk)
        set("versionCode", versionCode)
        set("versionName", versionName)
    }

    dependencies {
        classpath(libs.oss.licenses.plugin)
    }
}