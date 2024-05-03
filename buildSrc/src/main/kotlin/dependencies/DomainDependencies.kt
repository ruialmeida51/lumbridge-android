package dependencies

import versions.SharedVersions.HILT_VERSION
import versions.DomainVersions.RSS_PARSER_VERSION

object DomainDependencies {
    fun getImplementation() = listOf(
        // Hilt
        "com.google.dagger:hilt-android:$HILT_VERSION",

        // RSS Parser
        "com.prof18.rssparser:rssparser:$RSS_PARSER_VERSION"
    )

    fun getKapt() = listOf(
        // Hilt
        "com.google.dagger:hilt-compiler:$HILT_VERSION"
    )
}
