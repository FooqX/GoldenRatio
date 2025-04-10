plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "lv.RIN4X.goldenratio"
    compileSdk = 36

    defaultConfig {
        applicationId = "lv.RIN4X.goldenratio"
        minSdk = 28
        targetSdk = 36
        versionCode = 3
        versionName = "1.1.0"
    }


    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true  // Removes unused resources
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
        freeCompilerArgs += listOf(
            "-Xopt-in=kotlin.RequiresOptIn",
            "-XXLanguage:+InlineClasses",
            "-XXLanguage:+JvmInlineValueClasses"
        )
        buildToolsVersion = "36.0.0"
    }
    buildToolsVersion = "36.0.0"
    splits {
        abi {
            isEnable = true
            reset()
            //noinspection ChromeOsAbiSupport
            include("arm64-v8a")
            isUniversalApk = false
        }
    }
    dependenciesInfo {
        // Disables dependency metadata when building APKs.
        includeInApk = false
            // Disables dependency metadata when building Android App Bundles.
        includeInBundle = false
    }
}

dependencies {
    
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
}