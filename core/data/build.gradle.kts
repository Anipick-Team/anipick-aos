plugins {
    alias(libs.plugins.anipick.android.library)
}

android {
    namespace = "com.jparkbro.data"
}

dependencies {
    implementation(projects.core.datastore)
    implementation(projects.core.network)

    // Kakao
    implementation(libs.kakao.user)

    // Google Login
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)

    // ExifInterface
    implementation(libs.androidx.exifinterface)
}
