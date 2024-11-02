package dependencies

import versions.SharedVersions.HILT_VERSION

object SharedDependencies {
    fun getImplementation() = listOf(
        // Hilt
        "com.google.dagger:hilt-android:$HILT_VERSION",
    )

    fun getKapt() = listOf(
        // Hilt
        "com.google.dagger:hilt-compiler:$HILT_VERSION"
    )
}
