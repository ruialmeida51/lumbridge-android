package dependencies

import versions.AppVersions.ACCOMPANIST_VERSION
import versions.AppVersions.APP_COMPAT_VERSION
import versions.AppVersions.COIL_VERSION
import versions.AppVersions.COMPOSE_BOM_VERSION
import versions.AppVersions.COMPOSE_TOOLING_VERSION
import versions.AppVersions.FIREBASE_BOM_VERSION
import versions.AppVersions.HILT_NAVIGATION_VERSION
import versions.AppVersions.KOTLIN_COROUTINES_VERSION
import versions.AppVersions.KTX_SERIALIZATION_VERSION
import versions.AppVersions.KTX_VERSION
import versions.AppVersions.NAVIGATION_VERSION
import versions.SharedVersions.HILT_VERSION
import versions.SharedVersions.KOTLIN_VERSION

object AppDependencies {
    fun getImplementation() = listOf(
        // Android
        "androidx.appcompat:appcompat:$APP_COMPAT_VERSION",

        // Kotlin
        "org.jetbrains.kotlin:kotlin-stdlib:$KOTLIN_VERSION",
        "androidx.core:core-ktx:$KTX_VERSION",
        "org.jetbrains.kotlinx:kotlinx-serialization-json:$KTX_SERIALIZATION_VERSION",

        //Coroutines
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:$KOTLIN_COROUTINES_VERSION",

        // Compose
        "androidx.compose.material3:material3",
        "androidx.compose.ui:ui-tooling-preview",
        "androidx.lifecycle:lifecycle-runtime-compose",
        "androidx.compose.ui:ui-text-google-fonts",

        // Coil
        "io.coil-kt:coil:$COIL_VERSION",
        "io.coil-kt:coil-compose:$COIL_VERSION",

        // Navigation
        "androidx.navigation:navigation-runtime-ktx:$NAVIGATION_VERSION",
        "androidx.navigation:navigation-compose:$NAVIGATION_VERSION",

        // Hilt
        "androidx.hilt:hilt-navigation-compose:$HILT_NAVIGATION_VERSION",
        "com.google.dagger:hilt-android:$HILT_VERSION",

        // Firebase
        "com.google.firebase:firebase-inappmessaging-display",
        "com.google.firebase:firebase-messaging",
        "com.google.firebase:firebase-analytics",

        // Accompanist
        "com.google.accompanist:accompanist-permissions:$ACCOMPANIST_VERSION"
    )

    fun getPlatformImplementation() = listOf(
        // Firebase
        "com.google.firebase:firebase-bom:$FIREBASE_BOM_VERSION",

        // Compose
        "androidx.compose:compose-bom:$COMPOSE_BOM_VERSION",
    )

    fun debugImplementation() = listOf(
        "androidx.compose.ui:ui-tooling:$COMPOSE_TOOLING_VERSION"
    )

    fun getKapt() = listOf(
        // Hilt
        "com.google.dagger:hilt-compiler:$HILT_VERSION"
    )
}
