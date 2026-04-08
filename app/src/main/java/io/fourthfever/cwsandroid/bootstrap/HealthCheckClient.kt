package io.fourthfever.cwsandroid.bootstrap

import java.net.HttpURLConnection
import java.net.URL

class HealthCheckClient(
    private val openConnection: (String) -> HttpURLConnection = { url ->
        URL(url).openConnection() as HttpURLConnection
    },
) {
    fun isReady(baseUrl: String): Boolean {
        val connection = try {
            openConnection("$baseUrl/api/init-status")
        } catch (_: Exception) {
            return false
        }

        return try {
            connection.connectTimeout = 1_000
            connection.readTimeout = 1_000
            connection.requestMethod = "GET"
            connection.responseCode == 200
        } catch (_: Exception) {
            false
        } finally {
            connection.disconnect()
        }
    }
}
