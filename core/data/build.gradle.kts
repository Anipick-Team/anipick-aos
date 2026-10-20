plugins {
    alias(libs.plugins.anipick.android.library)
}

android {
    namespace = "com.jparkbro.data"
}

dependencies {
    implementation(projects.core.datastore)
    implementation(projects.core.network)

    // ExifInterface
    implementation(libs.androidx.exifinterface)
}
