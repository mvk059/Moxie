package fyi.manpreet.moxie.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composables.uikit.components.PrimaryButton

@Composable
fun NotFoundScreen(
    attemptedPath: String,
    onGoHome: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(androidx.compose.material3.MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text("Page not found")
        Text("Path: $attemptedPath")
        androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(12.dp))
        PrimaryButton(onClick = onGoHome) {
            Text("Go to projects")
        }
    }
}
