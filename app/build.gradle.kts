import java.util.Properties

@Suppress("DSL_SCOPE_VIOLATION")

// local.properties 파일 읽기
val localProperties = Properties()
file(rootProject.file("local.properties")).inputStream().use { inputStream ->
    localProperties.load(inputStream)
}

plugins {
    id("com.android.application")
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ktlint)
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.sopt.clody"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.sopt.clody"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 16
        versionName = "1.0.4"

        val clodyBaseUrl: String = localProperties.getProperty("clody.base.url") ?: ""
        val anotherBaseUrl: String = localProperties.getProperty("another.base.url") ?: ""
        val kakaoApiKey: String = localProperties.getProperty("kakao.api.key") ?: ""

        buildConfigField("String", "CLODY_BASE_URL", "\"$clodyBaseUrl\"")
        buildConfigField("String", "ANOTHER_BASE_URL", "\"$anotherBaseUrl\"")
        buildConfigField("String", "KAKAO_API_KEY", "\"$kakaoApiKey\"")

        manifestPlaceholders["kakaoRedirectUri"] = "kakao$kakaoApiKey"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    signingConfigs {
        create("release") {
            storeFile = file(localProperties.getProperty("storeFile") ?: "path/to/your/keystore.jks")
            storePassword = localProperties.getProperty("storePassword") ?: ""
            keyAlias = localProperties.getProperty("keyAlias") ?: ""
            keyPassword = localProperties.getProperty("keyPassword") ?: ""
        }
    }

    buildTypes {
        val baseUrl = localProperties.getProperty("clody.base.url") ?: ""
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
            firebaseCrashlytics {
                mappingFileUploadEnabled = true
            }
            buildConfigField("String", "CLODY_BASE_URL", "\"$baseUrl\"")
        }
        debug {
            firebaseCrashlytics {
                mappingFileUploadEnabled = true
            }
            buildConfigField("String", "CLODY_BASE_URL", "\"${baseUrl}/test/\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = libs.versions.jvmTarget.get()
    }
    buildFeatures {
        compose = true
        buildConfig = true  // BuildConfig 기능 활성화
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}


dependencies {
    // Compose
    implementation(libs.compose.compiler)
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3.compose)
    implementation(libs.activity.compose)
    implementation(libs.navigation.compose)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.lifecycle.compose)
    implementation(libs.constraintlayout.compose)

    // Kotlin
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.test)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.collections)

    // AndroidX Core
    implementation(libs.core.ktx)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.splashscreen)
    implementation(libs.androidx.paging)
    implementation(libs.androidx.paging.common)
    implementation(libs.androidx.paging.compose)
    implementation(libs.accompanist.insets)

    // Hilt (Dependency Injection)
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.javax.inject)

    // Retrofit (Networking)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.okhttp.logging)

    // Image Loading
    implementation(libs.coil.compose)


    // Logging
    implementation(libs.timber)

    // DataStore (Local Storage)
    implementation(libs.androidx.datastore.core)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.encrypted.datastore.preference.ksp)
    implementation(libs.encrypted.datastore.preference.ksp.annotations)
    implementation(libs.encrypted.datastore.preference.security)

    // ViewPager Indicator
    implementation(libs.viewpager.indicator)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)

    // Accompanist System UI Controller
    implementation(libs.accompanist.systemuicontroller)
    // Kakao
    implementation(libs.kakao.user)
    implementation(libs.process.phoenix)
    implementation(libs.lottie.compose)

    //firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)
    //fcm
    implementation(libs.firebase.messaging.ktx)
}
