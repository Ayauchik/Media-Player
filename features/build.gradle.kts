import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp")
    alias(libs.plugins.kotlin.compose)

}

android {
    namespace = "kz.tz.features"
    compileSdk = 35

    buildFeatures {
        buildConfig = true
    }

//    defaultConfig {
//        minSdk = 24
//
//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        consumerProguardFiles("consumer-rules.pro")
//        buildConfigField(
//            "String",
//            "PEXELS_API_KEY",
//            "\"${properties["PEXELS_API_KEY"]}\""
//        )
//        buildConfigField(
//            "String",
//            "BASE_URL",
//            "\"${properties["BASE_URL"]}\""
//        )
//    }

    defaultConfig{
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        val localProperties = File(rootProject.projectDir, "local.properties")
        val properties = Properties()
        if (localProperties.exists()) {
            properties.load(localProperties.inputStream())
        }
        buildConfigField(
            "String",
            "PEXELS_API_KEY",
            "\"${properties["PEXELS_API_KEY"]}\""
        )
        buildConfigField(
            "String",
            "BASE_URL",
            "\"${properties["BASE_URL"]}\""
        )

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Retrofit for API Calls
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.gson)


    // Coroutines for background tasks
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Lifecycle ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // Koin for Dependency Injection
    implementation(libs.koin.android)
    // Used Hilt, not Koin (but can implement that too)
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.ui)

    implementation(libs.coil.compose)
    implementation(libs.accompanist.swiperefresh)
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.foundation.layout)
    implementation(libs.androidx.material)

    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)

}