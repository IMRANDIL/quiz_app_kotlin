plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-parcelize")
    id("kotlin-android")
}

android {
    namespace = "com.example.quizapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.quizapp"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true // Add this to enable BuildConfig
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Existing dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.appcompat)

    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Existing custom dependencies
    implementation("androidx.constraintlayout:constraintlayout-compose:1.1.1")
    implementation("io.coil-kt:coil-compose:2.5.0")

    // ========== NEW DEPENDENCIES FOR MONGODB ATLAS ==========

    // 1. NETWORKING - These help us talk to MongoDB Atlas over the internet
    implementation("com.squareup.retrofit2:retrofit:2.9.0")              // Main networking library
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")        // Converts JSON to Kotlin objects
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")    // Logs network requests (helpful for debugging)

    // 2. NAVIGATION - Helps us move between screens
    implementation("androidx.navigation:navigation-compose:2.7.6")       // Navigation for Compose

    // 3. VIEWMODEL - Manages UI state and survives configuration changes
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0") // ViewModel for Compose

    // 4. COROUTINES - Handles background tasks (like network calls)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3") // Android coroutines

    // 5. GSON - Helps convert between JSON and Kotlin objects
    implementation("com.google.code.gson:gson:2.10.1")                  // JSON parsing library
}