package fyi.manpreet.moxie.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composables.uikit.components.Card
import fyi.manpreet.moxie.projects.ProjectDefinition

@Composable
fun ProjectsIndexScreen(
    projects: List<ProjectDefinition>,
    onOpenProject: (slug: String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "Moxie CMP Projects",
            style = MaterialTheme.typography.headlineSmall,
        )
        Text(
            text = "Choose a project to open a standalone route that can be embedded in an iframe.",
            style = MaterialTheme.typography.bodyMedium,
        )

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

