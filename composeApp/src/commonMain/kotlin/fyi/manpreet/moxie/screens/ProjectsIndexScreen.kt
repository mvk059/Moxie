package fyi.manpreet.moxie.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composables.uikit.components.Card
import com.composables.uikit.components.Text
import com.composables.uikit.scaffolds.ScreenScaffold
import fyi.manpreet.moxie.projects.ProjectDefinition

@Composable
fun ProjectsIndexScreen(
    projects: List<ProjectDefinition>,
    onOpenProject: (slug: String) -> Unit,
) {
    ScreenScaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(text = "Moxie CMP Projects")
            Text(text = "Choose a project to open a standalone route that can be embedded in an iframe.")

            projects.forEach { project ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    title = { Text(project.title) },
                    onClick = { onOpenProject(project.slug) },
                ) {
                    Text(project.description)
                }
            }
        }
    }
}

