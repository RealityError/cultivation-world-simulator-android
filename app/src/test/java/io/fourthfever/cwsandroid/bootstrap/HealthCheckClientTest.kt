package io.fourthfever.cwsandroid.bootstrap

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.net.HttpURLConnection
import java.net.URL

class HealthCheckClientTest {
    @Test
    fun returnsTrueWhenInitStatusResponds200() {
        val client = HealthCheckClient(
            openConnection = {
                object : HttpURLConnection(URL("http://localhost")) {
                    override fun disconnect() = Unit

                    override fun usingProxy(): Boolean = false

                    override fun connect() = Unit

                    override fun getResponseCode(): Int = 200
                }
            },
        )

        assertTrue(client.isReady("http://127.0.0.1:18002"))
    }

    @Test
    fun returnsFalseWhenConnectionThrows() {
        val client = HealthCheckClient(
            openConnection = { throw IllegalStateException("boom") },
        )

        assertFalse(client.isReady("http://127.0.0.1:18002"))
    }
}
