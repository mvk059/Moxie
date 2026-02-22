package fyi.manpreet.moxie.projects

import androidx.compose.runtime.Composable

data class ProjectDefinition(
    val slug: String,
    val title: String,
    val description: String,
    val content: @Composable () -> Unit,
)

