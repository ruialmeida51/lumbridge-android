package dependencies

import versions.AppVersions.COMPOSE_ACTIVITY_EXTENSIONS_VERSION
import versions.AppVersions.COMPOSE_MATERIAL_3_CORE_VERSION
import versions.AppVersions.COMPOSE_PREVIEW_VERSION
import versions.AppVersions.HILT_VERSION
import versions.AppVersions.HILT_NAVIGATION_VERSION
import versions.AppVersions.KOTLIN_COROUTINES_VERSION
import versions.AppVersions.KTX_VERSION
import versions.AppVersions.NAVIGATION_VERSION
import versions.AppVersions.RETROFIT_VERSION
import versions.AppVersions.ROOM_VERSION
import versions.SharedVersions.KOTLIN_VERSION

object AppDependencies {
	
	fun getImplementation() = listOf(
		// Kotlin
		"org.jetbrains.kotlin:kotlin-stdlib:$KOTLIN_VERSION",
		"androidx.core:core-ktx:$KTX_VERSION",
		
		//Coroutines
		"org.jetbrains.kotlinx:kotlinx-coroutines-android:$KOTLIN_COROUTINES_VERSION",
		
		// Compose
		"androidx.compose.material3:material3:$COMPOSE_MATERIAL_3_CORE_VERSION",
		"androidx.activity:activity-compose:$COMPOSE_ACTIVITY_EXTENSIONS_VERSION",
		"androidx.compose.ui:ui-tooling-preview-android:$COMPOSE_PREVIEW_VERSION",
		
		// Room
		"androidx.room:room-runtime:$ROOM_VERSION",
		
		// Retrofit
		"com.squareup.retrofit2:retrofit:$RETROFIT_VERSION",
		
		// Navigation
		"androidx.navigation:navigation-runtime-ktx:$NAVIGATION_VERSION",
		"androidx.navigation:navigation-compose:$NAVIGATION_VERSION",
		
		// Hilt
		"com.google.dagger:hilt-android:$HILT_VERSION",
		"androidx.hilt:hilt-navigation-compose:$HILT_NAVIGATION_VERSION"
	)
	
	fun getKapt() = listOf(
		// Room
		"androidx.room:room-runtime:$ROOM_VERSION",
		
		// Hilt
		"com.google.dagger:hilt-compiler:$HILT_VERSION"
	)
	
	fun getAnnotationProcessor() = listOf(
		// Room
		"androidx.room:room-runtime:$ROOM_VERSION",
	)
}
