plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)

    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.jparkbro.anipick"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.jparkbro.anipick"
        minSdk = 32
        targetSdk = 36
        versionCode = 1
        versionName = "${rootProject.extra["versionName"]}"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
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
    implementation(project(":core:model"))
    implementation(project(":core:network"))
    implementation(project(":core:ui"))

    implementation(project(":feature:auth:email"))
    implementation(project(":feature:auth:findpassword"))
    implementation(project(":feature:auth:login"))
    implementation(project(":feature:auth:preferencesetup"))

    implementation(project(":feature:main:detail"))
    implementation(project(":feature:main:explore"))
    implementation(project(":feature:main:home"))
    implementation(project(":feature:main:mypage"))
    implementation(project(":feature:main:ranking"))
    implementation(project(":feature:main:review"))
    implementation(project(":feature:main:search"))
    implementation(project(":feature:main:setting"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Compose
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

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

    // Kakao
    implementation(libs.v2.user)

    // Splash Screen
    implementation(libs.androidx.core.splashscreen)
}