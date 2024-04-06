import org.gradle.api.JavaVersion

object Config {
    const val NAME = "com.eyther.lumbridge"

    const val MIN_SDK = 31
    const val TARGET_SDK = 34
    const val VERSION = 1
    const val VERSION_NAME = "1.0"

    const val JAVA_VERSION = "17"
    val JAVA_TARGET = JavaVersion.VERSION_17
}