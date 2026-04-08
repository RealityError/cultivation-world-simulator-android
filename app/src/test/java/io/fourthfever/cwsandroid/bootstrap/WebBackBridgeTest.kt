package io.fourthfever.cwsandroid.bootstrap

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Assert.assertEquals
import org.junit.Test

class WebBackBridgeTest {
    @Test
    fun exposesExpectedJavascriptSnippets() {
        assertEquals(
            "(function(){return window.__cwsCanHandleBack ? String(window.__cwsCanHandleBack()) : 'false';})();",
            WebBackBridge.canHandleSnippet(),
        )
        assertEquals(
            "window.__cwsHandleBack && window.__cwsHandleBack();",
            WebBackBridge.handleSnippet(),
        )
    }

    @Test
    fun parsesJavascriptBooleanResult() {
        assertTrue(WebBackBridge.isTruthy("\"true\""))
        assertTrue(WebBackBridge.isTruthy("true"))
        assertFalse(WebBackBridge.isTruthy("\"false\""))
        assertFalse(WebBackBridge.isTruthy(null))
    }
}
