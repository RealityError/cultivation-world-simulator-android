package io.fourthfever.cwsandroid.bootstrap

import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Test

class RuntimeConfigTest {
    @Test
    fun buildsExpectedBaseUrlAndPaths() {
        val config = RuntimeConfig(
            host = "127.0.0.1",
            port = 18002,
            runtimeDir = File("runtime-root"),
            dataDir = File("data-root"),
        )

        assertEquals("http://127.0.0.1:18002", config.baseUrl)
        assertEquals(File("runtime-root"), config.runtimeDir)
        assertEquals(File("data-root"), config.dataDir)
    }
}
