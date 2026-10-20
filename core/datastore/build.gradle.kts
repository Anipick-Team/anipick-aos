plugins {
    alias(libs.plugins.anipick.android.library)
}

android {
    namespace = "com.jparkbro.datastore"
}

dependencies {
    implementation(libs.androidx.datastore)
    implementation(libs.androidx.datastore.preferences)

}
