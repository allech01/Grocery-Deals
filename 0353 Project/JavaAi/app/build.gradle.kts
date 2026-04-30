plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.javaai"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.javaai"
        minSdk = 26
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

    buildFeatures {
        viewBinding = true
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
    // Firebase BOM for consistent Firebase library versions
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.firebase.storage)

    // Core Android libraries
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Navigation components
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)

    // Google Play Services
    implementation(libs.play.services.ads)
    implementation(libs.play.services.auth)

    // Testing libraries
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // RecyclerView and Generative AI
    implementation(libs.recyclerview)
    implementation(libs.generativeai)
    tasks.register("generateSigningReport") {
        doLast {
            val debugKeystorePath = "${System.getProperty("user.home")}/.android/debug.keystore"
            val keytoolCommand = listOf(
                "keytool",
                "-list",
                "-v",
                "-keystore", debugKeystorePath,
                "-alias", "androiddebugkey",
                "-storepass", "android"
            )

            try {
                val process = ProcessBuilder(keytoolCommand)
                    .redirectErrorStream(true)
                    .start()

                val result = process.inputStream.bufferedReader().use { it.readText() }
                println(result)

            } catch (e: Exception) {
                println("Error executing keytool: ${e.message}")
            }
        }
    }

}
