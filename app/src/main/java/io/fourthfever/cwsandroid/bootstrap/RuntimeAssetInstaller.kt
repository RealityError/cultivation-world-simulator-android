package io.fourthfever.cwsandroid.bootstrap

import java.io.File
import java.io.InputStream

class RuntimeAssetInstaller(
    private val targetRoot: File,
    private val assetVersion: String,
    private val listAssets: (String) -> Array<String>,
    private val openAsset: (String) -> InputStream,
) {
    fun install(assetRoot: String = "cws-runtime"): File {
        val versionFile = File(targetRoot, ".runtime-version")
        if (versionFile.exists() && versionFile.readText() == assetVersion) {
            return targetRoot
        }

        if (targetRoot.exists()) {
            targetRoot.deleteRecursively()
        }
        targetRoot.mkdirs()

        copyTree(assetRoot, targetRoot)
        versionFile.writeText(assetVersion)
        return targetRoot
    }

    private fun copyTree(assetPath: String, outputDir: File) {
        val children = listAssets(assetPath)
        if (children.isEmpty()) {
            outputDir.parentFile?.mkdirs()
            openAsset(assetPath).use { input ->
                outputDir.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            return
        }

        outputDir.mkdirs()
        children.forEach { child ->
            copyTree("$assetPath/$child", File(outputDir, child))
        }
    }
}
