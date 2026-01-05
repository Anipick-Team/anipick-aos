import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)

    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")

    // Firebase - Application 모듈에만 필요
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.firebase-perf")
}

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
keystoreProperties.load(FileInputStream(keystorePropertiesFile))

val localPropertiesFile = rootProject.file("local.properties")
val localProperties = Properties()
localProperties.load(FileInputStream(localPropertiesFile))

android {
    namespace = "com.jparkbro.anipick"
    compileSdk = rootProject.extra["targetSdk"] as Int

    defaultConfig {
        applicationId = "com.jparkbro.anipick"
        minSdk = rootProject.extra["minSdk"] as Int
        targetSdk = rootProject.extra["targetSdk"] as Int
        versionCode = rootProject.extra["versionCode"] as Int
        versionName = "${rootProject.extra["versionName"]}"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(
            "String",
            "APP_VERSION_NAME",
            "\"${rootProject.extra["versionName"]}\""
        )

        buildConfigField(
            "String",
            "KAKAO_APP_KEY",
            "\"${localProperties.getProperty("KAKAO_APP_KEY", "")}\""
        )

        manifestPlaceholders["KAKAO_APP_KEY"] = localProperties.getProperty("KAKAO_APP_KEY", "")
    }

    signingConfigs {
        create("release") {
            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
        }
    }

    buildTypes {
        release {
            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
        }
        debug {
            isDebuggable = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {

    // Module
    implementation(project(":core:data"))
    implementation(project(":core:datastore"))
    implementation(project(":core:domain"))
    implementation(project(":core:network"))
    implementation(project(":core:ui"))

    implementation(project(":feature:auth:login"))
    implementation(project(":feature:auth:email:login"))
    implementation(project(":feature:auth:email:register"))
    implementation(project(":feature:auth:findpassword:verification"))
    implementation(project(":feature:auth:findpassword:reset"))
    implementation(project(":feature:auth:preferencesetup"))

    implementation(project(":feature:main:shell:home:main"))
    implementation(project(":feature:main:shell:home:detail"))

    implementation(project(":feature:main:info:anime"))
    implementation(project(":feature:main:info:studio"))
    implementation(project(":feature:main:info:character"))
    implementation(project(":feature:main:info:series"))
    implementation(project(":feature:main:info:recommend"))
    implementation(project(":feature:main:actor"))

    implementation(project(":feature:main:detail"))
    implementation(project(":feature:main:explore"))
    implementation(project(":feature:main:mypage"))
    implementation(project(":feature:main:ranking"))
    implementation(project(":feature:main:review"))
    implementation(project(":feature:main:search"))
    implementation(project(":feature:main:setting"))

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    // Splash Screen
    implementation(libs.androidx.core.splashscreen)

    // oss-licenses
    implementation(libs.androidx.appcompat)
    implementation(libs.play.services.oss.licenses)

    // In App Update
    implementation("com.google.android.play:app-update:2.1.0")
    implementation("com.google.android.play:app-update-ktx:2.1.0")
}