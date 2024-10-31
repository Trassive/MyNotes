plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.ksp)
    id("androidx.navigation.safeargs.kotlin")
    alias(libs.plugins.google.gms.google.services)

}


android {
    namespace = "com.example.mynotes"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.mynotes"
        minSdk = 26
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
    room {
        schemaDirectory("$projectDir/schemas")
    }

}

dependencies {
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.firebase.auth)
    ksp(libs.androidx.room.compiler)

    implementation(libs.hilt.android)
    ksp(libs.ksp.hilt)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.play.services.auth)

}
