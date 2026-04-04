plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.room)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.eyther.lumbridge.data"

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

    room {
        schemaDirectory("$projectDir/schemas")
    }

    sourceSets {
        getByName("debug") { java.srcDirs("src/main/java") }
        getByName("release") { java.srcDirs("src/main/java") }
        getByName("qa") { java.srcDirs("src/main/java") }
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":shared"))

    implementation(libs.datastore.preferences)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.retrofit.converter.scalars)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.hilt.android)
    implementation(libs.gson)
    implementation(libs.rssparser)

    ksp(libs.room.compiler)
    ksp(libs.hilt.compiler)
}
