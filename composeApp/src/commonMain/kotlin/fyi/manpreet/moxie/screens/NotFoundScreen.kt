package fyi.manpreet.moxie.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composables.uikit.components.Text
import com.composables.uikit.scaffolds.ScreenScaffold

@Composable
fun NotFoundScreen(
    attemptedPath: String,
) {
    ScreenScaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text("Page not found")
            Text("Path: $attemptedPath")
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
