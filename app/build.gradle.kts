plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
//    id("com.autonomousapps.dependency-analysis")
}

android {
    namespace = "nodomain.pacjo.wear.anotherapplist"
    compileSdk = 34

    defaultConfig {
        applicationId = "nodomain.pacjo.wear.anotherapplist"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
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
    implementation("androidx.core:core-ktx:1.12.0")
    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.wear.compose:compose-material:1.2.1")
    implementation("androidx.wear.compose:compose-foundation:1.2.1")
    implementation("androidx.activity:activity-compose:1.8.2")
//    implementation("androidx.wear.watchface:watchface-complications-data-source-ktx:1.2.0")
//    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
    implementation("com.google.accompanist:accompanist-drawablepainter:0.33.2-alpha")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("androidx.glance:glance-wear-tiles:1.0.0-alpha05")
//    implementation("androidx.wear.watchface:watchface-complications-data-source-ktx:1.1.1")
}