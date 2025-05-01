plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)

    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "jpark.bro.data"
    compileSdk = 35

    defaultConfig {
        minSdk = 32

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {

    // Module
    implementation(project(":domain"))

    implementation(libs.androidx.core.ktx)

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)

    // Google Login
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)

    // Kakao
    implementation(libs.v2.user)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
}