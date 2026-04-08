package io.fourthfever.cwsandroid

import android.os.Bundle
import android.util.Log
import android.webkit.ConsoleMessage
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import io.fourthfever.cwsandroid.bootstrap.HealthCheckClient
import io.fourthfever.cwsandroid.bootstrap.DataDirResolver
import io.fourthfever.cwsandroid.bootstrap.RuntimeAssetInstaller
import io.fourthfever.cwsandroid.bootstrap.RuntimeConfig
import io.fourthfever.cwsandroid.bootstrap.WebBackBridge
import java.io.File
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "CWSAndroid"
    }

    private lateinit var webView: WebView
    private lateinit var launchProgress: ProgressBar
    private lateinit var launchError: TextView

    private val runtimeConfig by lazy {
        val resolvedDataDir = DataDirResolver().resolve(filesDir)
        if (resolvedDataDir.name != "data") {
            Log.w(TAG, "Primary data dir is not writable, fallback to ${resolvedDataDir.absolutePath}")
        }
        RuntimeConfig(
            host = "127.0.0.1",
            port = 18002,
            runtimeDir = File(filesDir, "runtime"),
            dataDir = resolvedDataDir,
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webView = findViewById(R.id.web_view)
        launchProgress = findViewById(R.id.launch_progress)
        launchError = findViewById(R.id.launch_error)

        configureWebView()
        onBackPressedDispatcher.addCallback(this) { handleBackPressedIntoWeb() }

        lifecycleScope.launch {
            startEmbeddedRuntime()
        }
    }

    private fun configureWebView() {
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        webView.settings.databaseEnabled = true
        webView.webViewClient = WebViewClient()
        webView.webChromeClient =
            object : WebChromeClient() {
                override fun onConsoleMessage(consoleMessage: ConsoleMessage): Boolean {
                    val source = "${consoleMessage.sourceId()}:${consoleMessage.lineNumber()}"
                    val formattedMessage = "[WebView][$source] ${consoleMessage.message()}"
                    when (consoleMessage.messageLevel()) {
                        ConsoleMessage.MessageLevel.ERROR -> Log.e(TAG, formattedMessage)
                        ConsoleMessage.MessageLevel.WARNING -> Log.w(TAG, formattedMessage)
                        ConsoleMessage.MessageLevel.TIP -> Log.i(TAG, formattedMessage)
                        ConsoleMessage.MessageLevel.LOG -> Log.d(TAG, formattedMessage)
                        ConsoleMessage.MessageLevel.DEBUG -> Log.d(TAG, formattedMessage)
                    }
                    return super.onConsoleMessage(consoleMessage)
                }
            }
    }

    private suspend fun startEmbeddedRuntime() {
        val runtimeRoot = withContext(Dispatchers.IO) {
            RuntimeAssetInstaller(
                targetRoot = runtimeConfig.runtimeDir,
                assetVersion = BuildConfig.RUNTIME_ASSET_VERSION,
                listAssets = { path -> assets.list(path) ?: emptyArray() },
                openAsset = { path -> assets.open(path) },
            ).install()
        }

        withContext(Dispatchers.IO) {
            if (!Python.isStarted()) {
                Python.start(AndroidPlatform(applicationContext))
            }
            Python.getInstance()
                .getModule("embedded_launcher")
                .callAttr(
                    "start_server",
                    runtimeRoot.absolutePath,
                    runtimeConfig.dataDir.absolutePath,
                    runtimeConfig.host,
                    runtimeConfig.port,
                )
        }

        val ready = withContext(Dispatchers.IO) {
            val healthClient = HealthCheckClient()
            repeat(120) {
                if (healthClient.isReady(runtimeConfig.baseUrl)) {
                    return@withContext true
                }
                delay(250)
            }
            false
        }

        if (!ready) {
            showError("本机服务启动失败，请检查运行时资源和日志。")
            return
        }

        launchProgress.visibility = View.GONE
        launchError.visibility = View.GONE
        webView.visibility = View.VISIBLE
        webView.loadUrl(runtimeConfig.baseUrl)
    }

    private fun showError(message: String) {
        launchProgress.visibility = View.GONE
        webView.visibility = View.GONE
        launchError.visibility = View.VISIBLE
        launchError.text = message
    }

    private fun handleBackPressedIntoWeb() {
        webView.evaluateJavascript(WebBackBridge.canHandleSnippet()) { result ->
            if (WebBackBridge.isTruthy(result)) {
                webView.evaluateJavascript(WebBackBridge.handleSnippet(), null)
            } else {
                finish()
            }
        }
    }
}
