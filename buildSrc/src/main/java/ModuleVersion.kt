@Suppress("unused", "MemberVisibilityCanBePrivate")
class ModuleVersion {

    val versionName: String
        get() = gitTag()?.removePrefix("v") ?: "1.0.0"

    val versionCode: Int
        get() = gitCommitCount()

    private fun gitTag(): String? {
        return try {
            val process = ProcessBuilder("git", "describe", "--tags", "--abbrev=0")
                .redirectErrorStream(true)
                .start()
            val result = process.inputStream.bufferedReader().readText().trim()
            process.waitFor()
            if (result.isNotEmpty() && result.startsWith("v")) result else null
        } catch (_: Exception) {
            null
        }
    }

    private fun gitCommitCount(): Int {
        return try {
            val process = ProcessBuilder("git", "rev-list", "--count", "HEAD")
                .redirectErrorStream(true)
                .start()
            val result = process.inputStream.bufferedReader().readText().trim()
            process.waitFor()
            result.toIntOrNull() ?: 1
        } catch (_: Exception) {
            1
        }
    }

}