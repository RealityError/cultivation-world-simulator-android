package io.fourthfever.cwsandroid.bootstrap

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.File

class RuntimeAssetInstallerTest {
    @Test
    fun installsAssetTreeAndWritesVersionMarker() {
        val tempRoot = createTempDir(prefix = "cws-runtime-test-")
        val assets = mapOf(
            "cws-runtime" to arrayOf("src", "static"),
            "cws-runtime/src" to arrayOf("server.py"),
            "cws-runtime/src/server.py" to "print('ok')".toByteArray(),
            "cws-runtime/static" to arrayOf("config.yml"),
            "cws-runtime/static/config.yml" to "system: {}".toByteArray(),
        )
        val runtimeRoot = File(tempRoot, "runtime")
        val installer = RuntimeAssetInstaller(
            targetRoot = runtimeRoot,
            assetVersion = "0.1.0",
            listAssets = { path: String ->
                when (val value = assets[path]) {
                    is Array<*> -> value.filterIsInstance<String>().toTypedArray()
                    else -> emptyArray()
                }
            },
            openAsset = { path: String ->
                val value = assets[path] as? ByteArray
                    ?: error("Missing asset bytes for $path")
                ByteArrayInputStream(value)
            },
        )

        installer.install()

        assertTrue(File(runtimeRoot, "src/server.py").exists())
        assertTrue(File(runtimeRoot, "static/config.yml").exists())
        assertEquals("0.1.0", File(runtimeRoot, ".runtime-version").readText())
    }
}
