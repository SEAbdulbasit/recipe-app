plugins {
    kotlin("multiplatform")
    id("com.android.application")
    id("org.jetbrains.compose")
}

kotlin {
    android()
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(project(":shared"))
            }
        }
    }
}

android {
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    namespace = "com.myapplication"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")

    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
        targetSdk = (findProperty("android.targetSdk") as String).toInt()
        versionCode = 1
        versionName = "1.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        jvmToolchain(11)
    }

    dependencies {
        implementation("androidx.compose.ui:ui:1.7.0-SNAPSHOT")
        implementation("androidx.compose.animation:animation-core:1.7.0-SNAPSHOT")
        implementation("androidx.compose.animation:animation:1.7.0-SNAPSHOT")
        implementation("androidx.navigation:navigation-compose:2.7.7")

    }
}
dependencies {
    implementation("androidx.compose.foundation:foundation-android:1.6.5")
}
