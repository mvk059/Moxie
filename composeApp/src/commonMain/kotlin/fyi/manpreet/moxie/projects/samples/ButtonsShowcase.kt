package fyi.manpreet.moxie.projects.samples

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composables.uikit.components.Card
import com.composables.uikit.components.DestructiveButton
import com.composables.uikit.components.GhostButton
import com.composables.uikit.components.PrimaryButton
import com.composables.uikit.components.SecondaryButton
import com.composables.uikit.components.Text

@Composable
fun ButtonsShowcase() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Card(title = { Text("Buttons") }) {
            Text("Using components from com.composables.uikit.components")
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                PrimaryButton(onClick = {}) { Text("Primary") }
                SecondaryButton(onClick = {}) { Text("Secondary") }
                GhostButton(onClick = {}) { Text("Ghost") }
                DestructiveButton(onClick = {}) { Text("Delete") }
            }
        }
    }
}

