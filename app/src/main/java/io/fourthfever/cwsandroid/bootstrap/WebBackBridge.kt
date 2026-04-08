package io.fourthfever.cwsandroid.bootstrap

object WebBackBridge {
    fun canHandleSnippet(): String =
        "(function(){return window.__cwsCanHandleBack ? String(window.__cwsCanHandleBack()) : 'false';})();"

    fun handleSnippet(): String =
        "window.__cwsHandleBack && window.__cwsHandleBack();"

    fun isTruthy(result: String?): Boolean =
        result == "\"true\"" || result == "true"
}
