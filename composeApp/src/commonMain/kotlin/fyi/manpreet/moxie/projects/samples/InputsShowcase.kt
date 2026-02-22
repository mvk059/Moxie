package fyi.manpreet.moxie.projects.samples

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import com.composables.uikit.components.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composables.uikit.components.Card
import com.composables.uikit.components.Checkbox
import com.composables.uikit.components.TextField

@Composable
fun InputsShowcase() {
    var name by remember { mutableStateOf("") }
    var checked by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Card(title = { Text("Inputs") }) {
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                placeholder = { Text("Type your name") },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(12.dp))
            Checkbox(
                checked = checked,
                onCheckedChange = { checked = it },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Receive product updates") },
            )
            Spacer(Modifier.height(8.dp))
            Text("Current input: ${if (name.isBlank()) "empty" else name}")
        }
    }
}

