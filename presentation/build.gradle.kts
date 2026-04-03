plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.eyther.lumbridge.presentation"

    defaultConfig {
        minSdk = Config.MIN_SDK
        compileSdk = Config.TARGET_SDK

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
            isMinifyEnabled = false
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
    implementation(libs.accompanist.permissions)

    debugImplementation(libs.compose.ui.tooling)

    kapt(libs.hilt.compiler)
    kapt(libs.hilt.compiler.jetpack)
}
