plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.appdistribution)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = Config.NAME
    compileSdk = Config.TARGET_SDK

    defaultConfig {
        applicationId = Config.NAME
        minSdk = Config.MIN_SDK
        targetSdk = Config.TARGET_SDK
        versionCode = Config.VERSION
        versionName = Config.VERSION_NAME

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            resValue ("string", "app_name", "@string/app_name_debug")

            isDebuggable = true
            isShrinkResources = false
            enableUnitTestCoverage = false

            firebaseAppDistribution {
                artifactType = "APK"
                groups = "internal-testers"
                serviceCredentialsFile = "$rootDir/app/lumbridge-firebase-service-account.json"
            }

            matchingFallbacks.addAll(arrayOf("qa", "debug"))
        }

        register("qa") {
            isDebuggable = true
            enableUnitTestCoverage = true

            matchingFallbacks.add("qa")
            initWith(getByName("debug"))
        }

        register("beta") {
            initWith(getByName("release"))

            applicationIdSuffix = ".beta"
            versionNameSuffix = "-beta"
            resValue ("string", "app_name", "@string/app_name_beta")

            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true

            firebaseAppDistribution {
                artifactType = "APK"
                groups = "internal-testers"
                serviceCredentialsFile = "$rootDir/app/lumbridge-firebase-service-account.json"
            }

            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks.add("release")
        }

        release {
            resValue ("string", "app_name", "@string/app_name_release")

            isDebuggable = false
            isMinifyEnabled = true
            isShrinkResources = true

            matchingFallbacks.add("release")
        }
    }

    compileOptions {
        sourceCompatibility = Config.JAVA_TARGET
        targetCompatibility = Config.JAVA_TARGET
    }

    kotlinOptions {
        jvmTarget = Config.JAVA_VERSION
    }

    kapt {
        correctErrorTypes = true
    }

    hilt {
        enableAggregatingTask = true
    }

    buildFeatures {
        compose = true
    }

    sourceSets {
        getByName("debug") { java.srcDirs("src/main/java") }
        getByName("release") { java.srcDirs("src/main/java") }
        getByName("qa") { java.srcDirs("src/main/java") }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":di"))
    implementation(project(":shared"))
    implementation(project(":presentation"))

    implementation(platform(libs.firebase.bom))
    implementation(platform(libs.compose.bom))

    implementation(libs.androidx.appcompat)
    implementation(libs.kotlin.stdlib)
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.compose.material3)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.compose.ui.text.google.fonts)
    implementation(libs.coil)
    implementation(libs.coil.compose)
    implementation(libs.navigation.runtime.ktx)
    implementation(libs.navigation.compose)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.hilt.android)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.hilt.work)
    implementation(libs.firebase.inappmessaging.display)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.analytics)
    implementation(libs.accompanist.permissions)

    debugImplementation(libs.compose.ui.tooling)

    kapt(libs.hilt.compiler)
    kapt(libs.hilt.compiler.jetpack)
}
