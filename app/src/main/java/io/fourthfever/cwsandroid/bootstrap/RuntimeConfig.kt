package io.fourthfever.cwsandroid.bootstrap

import java.io.File

data class RuntimeConfig(
    val host: String,
    val port: Int,
    val runtimeDir: File,
    val dataDir: File,
) {
    val baseUrl: String
        get() = "http://$host:$port"
}
