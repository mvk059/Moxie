package fyi.manpreet.moxie.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composables.uikit.components.GhostButton
import fyi.manpreet.moxie.projects.ProjectDefinition

@Composable
fun ProjectDetailScreen(
    project: ProjectDefinition,
    embedMode: Boolean,
    onBackHome: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(androidx.compose.material3.MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
    ) {
        if (!embedMode) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = project.title,
                    style = androidx.compose.material3.MaterialTheme.typography.titleLarge,
                )
                GhostButton(onClick = onBackHome) {
                    Text("Back to projects")
                }
            }
        }
        project.content()
    }
}

