plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.features.movies.detail.ui"
    compileSdk = 34
}

dependencies {
    implementation(project(":features:movies:detail:presentation"))
    implementation(project(":core"))
    implementation(project(":domain"))

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.coil.compose)
}