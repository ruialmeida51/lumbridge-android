package dependencies

import versions.SdkVersions.DATA_STORE_VERSION
import versions.SdkVersions.GSON_VERSION
import versions.SdkVersions.RETROFIT_VERSION
import versions.SdkVersions.ROOM_VERSION
import versions.SharedVersions.HILT_VERSION

object DiDependencies {
    fun getImplementation() = listOf(
        // DataStore
        "androidx.datastore:datastore-preferences:$DATA_STORE_VERSION",

        // Retrofit
        "com.squareup.retrofit2:retrofit:$RETROFIT_VERSION",
        "com.squareup.retrofit2:converter-gson:$RETROFIT_VERSION",

        // Room
        "androidx.room:room-runtime:$ROOM_VERSION",
        "androidx.room:room-ktx:$ROOM_VERSION",

        // Hilt
        "com.google.dagger:hilt-android:$HILT_VERSION",

        // Gson
        "com.google.code.gson:gson:$GSON_VERSION"
    )

    fun getKapt() = listOf(
        // Hilt
        "com.google.dagger:hilt-compiler:$HILT_VERSION"
    )
}
