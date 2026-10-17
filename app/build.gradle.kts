plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.multimodal_model_integration"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.multimodal_model_integration"
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

    // ✅ Add this block inside android
    aaptOptions {
        noCompress("tflite")
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // TensorFlow Lite
    implementation("org.tensorflow:tensorflow-lite:2.14.0")
    implementation("org.tensorflow:tensorflow-lite-task-audio:0.4.2")
    implementation("org.tensorflow:tensorflow-lite-gpu:2.14.0") // optional
    implementation("org.tensorflow:tensorflow-lite:2.13.0") // redundant but OK
}
