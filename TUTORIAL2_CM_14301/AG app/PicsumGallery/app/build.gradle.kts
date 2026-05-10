plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    // KSP — required for Room annotation processing
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.example.picsumgallery"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.picsumgallery"
        minSdk = 26           // API 26 as specified in the implementation plan
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    // Disable Jetpack Compose — this project uses XML Views only
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // AndroidX Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Retrofit + Gson converter
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)

    // OkHttp logging interceptor
    implementation(libs.okhttp.logging)

    // Glide — image loading
    implementation(libs.glide)

    // Room — local database
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // Coroutines
    implementation(libs.coroutines.android)

    // Lifecycle: ViewModel + LiveData
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.runtime.ktx)

    // SwipeRefreshLayout
    implementation(libs.swiperefreshlayout)

    // RecyclerView
    implementation(libs.recyclerview)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
