package dependencies

import versions.SdkVersions.DATA_STORE_VERSION
import versions.SdkVersions.RETROFIT_VERSION
import versions.SdkVersions.ROOM_VERSION
import versions.SharedVersions.HILT_VERSION

object SdkDependencies {
    fun getImplementation() = listOf(
        // Data store
        "androidx.datastore:datastore-preferences:$DATA_STORE_VERSION",

        // Retrofit
        "com.squareup.retrofit2:retrofit:$RETROFIT_VERSION",
        "com.squareup.retrofit2:converter-gson:$RETROFIT_VERSION",
        "com.squareup.retrofit2:converter-scalars:$RETROFIT_VERSION",

        // Room
        "androidx.room:room-runtime:$ROOM_VERSION",

        // Hilt
        "com.google.dagger:hilt-android:$HILT_VERSION",
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
