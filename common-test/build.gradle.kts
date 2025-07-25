plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.tiation.dnddiceroller.test"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
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
}

dependencies {
    // Core testing dependencies (exposed to all modules)
    api(libs.bundles.testing)
    api(libs.androidx.test.ext.junit)
    api(libs.androidx.test.espresso.core)

    // AndroidX Core
    implementation(libs.androidx.core.ktx)

    // Coroutines testing
    api("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
}
