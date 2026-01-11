plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.features.movies.detail.navigation"
    compileSdk = 34
}

dependencies {
    implementation(project(":features:movies:detail:ui"))
    implementation(project(":features:movies:detail:presentation"))

    implementation(libs.androidx.lifecycle.viewmodel.compose)
}