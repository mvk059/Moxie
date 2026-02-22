package fyi.manpreet.moxie

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import fyi.manpreet.moxie.embed.IframeAutoResizeBridge
import fyi.manpreet.moxie.routing.CmpRoute
import fyi.manpreet.moxie.routing.rememberWebRouteAdapter

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport {
        val routeAdapter = rememberWebRouteAdapter()
        val projectSlug = (routeAdapter.currentRoute.screen as? CmpRoute.Screen.Project)?.slug

        if (routeAdapter.currentRoute.embedMode) {
            IframeAutoResizeBridge(slug = projectSlug)
        }

        CmpPortalApp(
            route = routeAdapter.currentRoute,
            onNavigateToPath = routeAdapter::navigate,
        )
    }
}
