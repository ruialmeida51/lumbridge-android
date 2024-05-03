package dependencies

import versions.AppVersions.COMPOSE_BOM_VERSION
import versions.AppVersions.COMPOSE_MATERIAL_3_CORE_VERSION
import versions.AppVersions.COMPOSE_TOOLING_VERSION
import versions.AppVersions.FIREBASE_BOM_VERSION
import versions.AppVersions.HILT_NAVIGATION_VERSION
import versions.AppVersions.KOTLIN_COROUTINES_VERSION
import versions.AppVersions.KTX_VERSION
import versions.AppVersions.NAVIGATION_VERSION
import versions.AppVersions.COIL_VERSION
import versions.SharedVersions.HILT_VERSION
import versions.SharedVersions.KOTLIN_VERSION

object AppDependencies {

    fun getImplementation() = listOf(
        // Kotlin
        "org.jetbrains.kotlin:kotlin-stdlib:$KOTLIN_VERSION",
        "androidx.core:core-ktx:$KTX_VERSION",

        //Coroutines
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:$KOTLIN_COROUTINES_VERSION",

        // Compose
        "androidx.compose:compose-bom:$COMPOSE_BOM_VERSION",
        "androidx.compose.material3:material3:$COMPOSE_MATERIAL_3_CORE_VERSION",
        "androidx.compose.ui:ui-tooling-preview:$COMPOSE_TOOLING_VERSION",

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
        "com.google.firebase:firebase-bom:$FIREBASE_BOM_VERSION",
    )

    fun debugImplementation() = listOf(
        "androidx.compose.ui:ui-tooling:$COMPOSE_TOOLING_VERSION"
    )

    fun getKapt() = listOf(
        // Hilt
        "com.google.dagger:hilt-compiler:$HILT_VERSION"
    )
}
