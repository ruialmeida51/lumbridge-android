import dependencies.DIDependencies

plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    kotlin("kapt")
}

android {
    namespace = "com.eyther.lumbridge.shared"

    defaultConfig {
        minSdk = Config.MIN_SDK
        compileSdk = Config.TARGET_SDK

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        debug {
            isShrinkResources = false
            enableUnitTestCoverage = false

            matchingFallbacks.addAll(arrayOf("qa", "debug"))
        }

        register("qa") {
            enableUnitTestCoverage = true

            matchingFallbacks.add("qa")
            initWith(getByName("debug"))
        }

        register("beta") {
            enableUnitTestCoverage = true

            matchingFallbacks.add("release")
            initWith(getByName("release"))
        }

        release {
            isMinifyEnabled = true
            isShrinkResources = false

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

    sourceSets {
        getByName("debug") { java.srcDirs("src/main/java") }
        getByName("release") { java.srcDirs("src/main/java") }
        getByName("qa") { java.srcDirs("src/main/java") }
    }
}

dependencies {
    DIDependencies.getImplementation().map { implementation(it) }
    DIDependencies.getKapt().map { kapt(it) }
}
