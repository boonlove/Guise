import org.gradle.kotlin.dsl.provideDelegate
import java.io.File
import java.nio.file.Files
import kotlin.jvm.Throws

@Suppress("unused")
class SignatureInfo constructor(propertiesFile: File) : PropertiesFile(propertiesFile) {

    companion object {
        private const val STORE_FILE = "storeFile"
        private const val STORE_PASSWORD = "storePassword"
        private const val KEY_ALIAS = "keyAlias"
        private const val KEY_PASSWORD = "keyPassword"
    }

    val storeFile: File
        @Throws(RuntimeException::class)
        get() {
            val path = properties.getProperty(STORE_FILE)
                ?: throw RuntimeException(
                    "storeFile is not set in the properties file. Please set it. " +
                            "(${propertiesFile.absolutePath})"
                )
            return File(propertiesFile.parentFile, path)
        }

    val storePassword: String
        @Throws(RuntimeException::class)
        get() {
            return properties.getProperty(STORE_PASSWORD)
                ?: throw RuntimeException(
                    "storePassword is not set in the properties file. Please set it. " +
                            "(${propertiesFile.absolutePath})"
                )
        }

    val keyAlias: String
        @Throws(RuntimeException::class)
        get() {
            return properties.getProperty(KEY_ALIAS)
                ?: throw RuntimeException(
                    "keyAlias is not set in the properties file. Please set it. " +
                            "(${propertiesFile.absolutePath})"
                )
        }

    val keyPassword: String
        @Throws(RuntimeException::class)
        get() {
            return properties.getProperty(KEY_PASSWORD)
                ?: throw RuntimeException(
                    "keyPassword is not set in the properties file. Please set it. " +
                            "(${propertiesFile.absolutePath})"
                )
        }

}