// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false

    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false

    id("com.google.dagger.hilt.android") version "2.57.2" apply false
    id("com.google.devtools.ksp") version "2.2.10-2.0.2" apply false
}

buildscript {
    val targetSdk = 36
    val minSdk = 26
    val versionCode = 21
    val versionName = "1.0.5"

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