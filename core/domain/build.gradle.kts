plugins {
    alias(libs.plugins.anipick.android.library)
}

android {
    namespace = "com.jparkbro.domain"
}

dependencies {
    implementation(projects.core.data)

}
