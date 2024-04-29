plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "nodomain.pacjo.wear.anotherapplist"
    compileSdk = 34

    defaultConfig {
        applicationId = "nodomain.pacjo.wear.anotherapplist"
        minSdk = 26
        targetSdk = 34
        versionCode = 12
        versionName = "1.2"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            // I'd love to change this, but then app crashes when opened through tile
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
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.0")
    implementation(platform("androidx.compose:compose-bom:2024.04.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.wear.compose:compose-material:1.3.1")
    implementation("androidx.wear.compose:compose-foundation:1.3.1")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("com.google.accompanist:accompanist-drawablepainter:0.34.0")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("androidx.glance:glance-wear-tiles:1.0.0-alpha05")
    implementation("androidx.profileinstaller:profileinstaller:1.3.1")
    implementation("androidx.wear.compose:compose-navigation:1.3.1")
    implementation("com.google.android.horologist:horologist-compose-material:0.6.9")

    "baselineProfile"(project(":app:baselineprofile"))
}