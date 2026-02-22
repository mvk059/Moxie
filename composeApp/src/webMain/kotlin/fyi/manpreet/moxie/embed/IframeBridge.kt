package fyi.manpreet.moxie.embed

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.toJsString

private val embedOrigins = listOf(
    "https://manpreet.fyi",
    "https://www.manpreet.fyi",
    "http://localhost:3000",
)

@Composable
fun IframeAutoResizeBridge(slug: String?) {
    LaunchedEffect(slug) {
        while (isActive) {
            postResizeMessage(slug)
            delay(250)
        }
    }
}

private fun sanitizeSlug(slug: String?): String {
    val value = slug ?: ""
    return value.replace("\"", "")
}

private fun postResizeMessage(slug: String?) {
    if (window.parent == window) return

    val bodyHeight = document.body?.scrollHeight ?: 0
    val rootHeight = document.documentElement?.scrollHeight ?: 0
    val height = maxOf(bodyHeight, rootHeight)
    val safeSlug = sanitizeSlug(slug)
    val payload = """{"type":"moxie-cmp:resize","slug":"$safeSlug","height":$height}"""

    embedOrigins.forEach { origin ->
        postToParent(payload, origin)
    }
}

@OptIn(ExperimentalWasmJsInterop::class)
private fun postToParent(message: String, targetOrigin: String) {
    window.parent.postMessage(message.toJsString(), targetOrigin)
}
