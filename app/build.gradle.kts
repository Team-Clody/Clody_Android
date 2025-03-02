import java.util.Properties

val properties = Properties().apply {
    load(project.rootProject.file("local.properties").inputStream())
}

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics.plugin)
}

android {
    namespace = "com.sopt.clody"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.sopt.clody"
        minSdk = 28
        targetSdk = 35
        versionCode = 18
        versionName = "1.0.6"
        val kakaoApiKey: String = properties.getProperty("kakao.api.key")
        val amplitudeApiKey: String = properties.getProperty("amplitude.api.key")
        buildConfigField("String", "KAKAO_API_KEY", "\"$kakaoApiKey\"")
        buildConfigField("String","APLITUDE_API_KEY","\"$amplitudeApiKey\"")
        manifestPlaceholders["kakaoRedirectUri"] = "kakao$kakaoApiKey"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            val localProperties = Properties().apply {
                load(project.rootProject.file("local.properties").inputStream())
            }
            storeFile = project.rootProject.file(localProperties.getProperty("storeFile"))
            storePassword = localProperties.getProperty("storePassword") ?: ""
            keyAlias = localProperties.getProperty("keyAlias") ?: ""
            keyPassword = localProperties.getProperty("keyPassword") ?: ""
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            buildConfigField("String", "CLODY_BASE_URL", properties["clody.base.url"].toString())
        }

        release {
            isMinifyEnabled = true
            isShrinkResources = true
            buildConfigField("String", "CLODY_BASE_URL", properties["clody.base.url"].toString())
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
        buildConfig = true
        compose = true
    }
}

dependencies {

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.bundles.test)

    // Debug
    debugImplementation(libs.bundles.debug)

    // Androidx
    implementation(libs.bundles.androidx)
    implementation(platform(libs.compose.bom))

    // Coroutine
    implementation(libs.bundles.coroutines)

    // DI
    implementation(libs.bundles.hilt)
    ksp(libs.hilt.compiler)

    // Third-Party
    implementation(platform(libs.okhttp.bom))
    implementation(libs.bundles.okhttp)
    implementation(libs.bundles.retrofit)
    implementation(libs.kotlinx.serialization.json)

    // FireBase
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase)

    // Amplitude
    implementation(libs.amplitude)

    // Google Admob
//    implementation(libs.admob)

    // UI Enhance
    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.accompanist.insets)

    // ETC
    implementation(libs.timber)
    implementation(libs.lottie.compose)
    implementation(libs.coil)
    implementation(libs.kakao.user)
    implementation(libs.kotlinx.datetime)
}
