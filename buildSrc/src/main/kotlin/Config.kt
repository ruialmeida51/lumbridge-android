import org.gradle.api.JavaVersion

object Config {
    const val NAME = "com.eyther.lumbridge"

    const val MIN_SDK = 31
    const val TARGET_SDK = 35
    const val VERSION = 9
    const val VERSION_NAME = "1.5"

    const val JAVA_VERSION = "17"
    val JAVA_TARGET = JavaVersion.VERSION_17
}
