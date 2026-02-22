package fyi.manpreet.moxie.routing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.browser.window
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.js.ExperimentalWasmJsInterop

@Stable
@OptIn(ExperimentalWasmJsInterop::class)
class WebRouteAdapter internal constructor(initialRoute: CmpRoute) {
    var currentRoute: CmpRoute by mutableStateOf(initialRoute)
        private set

    fun navigate(path: String) {
        if (window.location.pathname == path) return
        window.history.pushState(null, "", path)
        syncFromLocation()
    }

    fun syncFromLocation() {
        currentRoute = parseRoute(
            pathname = window.location.pathname,
            search = window.location.search,
        )
    }
}

@Composable
fun rememberWebRouteAdapter(): WebRouteAdapter {
    val adapter = remember {
        WebRouteAdapter(
            initialRoute = parseRoute(
                pathname = window.location.pathname,
                search = window.location.search,
            ),
        )
    }

    LaunchedEffect(adapter) {
        var lastPath = window.location.pathname
        var lastSearch = window.location.search
        while (isActive) {
            val nextPath = window.location.pathname
            val nextSearch = window.location.search
            if (nextPath != lastPath || nextSearch != lastSearch) {
                lastPath = nextPath
                lastSearch = nextSearch
                adapter.syncFromLocation()
            }
            delay(150)
        }
    }

    return adapter
}
