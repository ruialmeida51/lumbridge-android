import dependencies.AppDependencies

plugins {
    id("com.android.application")
    id("kotlin-parcelize")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    id("com.google.firebase.appdistribution")
    id("com.google.firebase.crashlytics")
    kotlin("kapt")
    kotlin("plugin.serialization")
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

            isDebuggable = true
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
    implementation(project(":shared"))

    AppDependencies.getPlatformImplementation().map { implementation(platform(it)) }
    AppDependencies.getImplementation().map { implementation(it) }
    AppDependencies.debugImplementation().map { debugImplementation(it) }
    AppDependencies.getKapt().map { kapt(it) }
}
