package fyi.manpreet.moxie

import androidx.compose.runtime.Composable
import com.composables.uikit.styling.UiKitTheme
import fyi.manpreet.moxie.projects.ProjectRegistry
import fyi.manpreet.moxie.routing.CmpRoute
import fyi.manpreet.moxie.routing.projectPath
import fyi.manpreet.moxie.screens.NotFoundScreen
import fyi.manpreet.moxie.screens.ProjectDetailScreen
import fyi.manpreet.moxie.screens.ProjectsIndexScreen

@Composable
fun CmpPortalApp(
    route: CmpRoute,
    onNavigateToPath: (String) -> Unit,
) {
    UiKitTheme {
        when (val screen = route.screen) {
            CmpRoute.Screen.Index -> {
                ProjectsIndexScreen(
                    projects = ProjectRegistry.all,
                    onOpenProject = { slug -> onNavigateToPath(projectPath(slug)) },
                )
            }

            is CmpRoute.Screen.Project -> {
                val project = ProjectRegistry.findBySlug(screen.slug)
                if (project == null) {
                    NotFoundScreen(
                        attemptedPath = projectPath(screen.slug),
                    )
                } else {
                    ProjectDetailScreen(
                        project = project,
                        embedMode = route.embedMode,
                    )
                }
            }

            is CmpRoute.Screen.NotFound -> {
                NotFoundScreen(
                    attemptedPath = screen.path,
                )
            }
        }
    }
}

