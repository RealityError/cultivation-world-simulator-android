package io.fourthfever.cwsandroid.bootstrap

import java.io.File

class DataDirResolver(
    private val primaryDirName: String = "data",
    private val recoveryDirName: String = "data_v2",
    private val requiredRelativePaths: List<String> = listOf("", "saves", "logs", "cache", "incompatible"),
    internal val writabilityProbe: (File) -> Boolean = { dir -> probeWritableDirectory(dir) },
) {
    fun resolve(filesDir: File): File {
        val primaryDir = File(filesDir, primaryDirName)
        if (isUsableDataDir(primaryDir)) {
            return primaryDir
        }

        val recoveryDir = File(filesDir, recoveryDirName)
        if (isUsableDataDir(recoveryDir)) {
            return recoveryDir
        }

        throw IllegalStateException("No writable app data directory under ${filesDir.absolutePath}")
    }

    private fun isUsableDataDir(rootDir: File): Boolean =
        requiredRelativePaths.all { relativePath ->
            val candidate =
                if (relativePath.isEmpty()) {
                    rootDir
                } else {
                    File(rootDir, relativePath)
                }
            writabilityProbe(candidate)
        }

    companion object {
        private fun probeWritableDirectory(dir: File): Boolean {
            return try {
                if (dir.exists()) {
                    if (!dir.isDirectory) {
                        return false
                    }
                } else if (!dir.mkdirs()) {
                    return false
                }

                val probeFile = File(dir, ".cws-write-probe")
                probeFile.writeText("ok")
                if (!probeFile.delete()) {
                    probeFile.deleteOnExit()
                }
                true
            } catch (_: Exception) {
                false
            }
        }
    }
}
