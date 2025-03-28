plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.ksp)

}

android {
    namespace = "francisco.simon.vknewsclient"
    compileSdk = 35

    defaultConfig {
        applicationId = "francisco.simon.vknewsclient"
        minSdk = 24
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
    buildFeatures {
        compose = true
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
    //Compose LiveData
    implementation(libs.compose.livedata)
    //Compose navigation
    implementation(libs.compose.navigation)


    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //Gson
    implementation(libs.gson)

    //Coil compose
    implementation(libs.coil.compose)
    //Coil сеть
    implementation(libs.coil.network)

    //vk
    implementation(libs.vk.core)
    implementation(libs.vk.api)

    //OkHttpClient
    implementation(libs.okHttpClient)
    //HttpLoggingInterceptor
    implementation(libs.httpLoggingInterceptor)

    //Retrofit
    implementation(libs.retrofit)

    //Dagger2
    implementation(libs.dagger2)
    //Dagger2 кодогенератор
    ksp(libs.dagger2.compiler)
    //Dagger2 аннотации
    ksp(libs.dagger2.android.processor)
}