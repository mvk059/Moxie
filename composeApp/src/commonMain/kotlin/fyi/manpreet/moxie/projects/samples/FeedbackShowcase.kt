package fyi.manpreet.moxie.projects.samples

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.composables.uikit.components.Card
import com.composables.uikit.components.LinearProgressIndicator
import com.composables.uikit.components.Slider
import com.composables.uikit.components.rememberSliderState

@Composable
fun FeedbackShowcase() {
    val sliderState = rememberSliderState(initialValue = 0.35f)
    val progress = sliderState.value

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Card(title = { Text("Feedback + Progress") }) {
            Text("Progress: ${(progress * 100).toInt()}%")
            Spacer(Modifier.height(12.dp))
            LinearProgressIndicator(progress = progress, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(16.dp))
            Slider(
                state = sliderState,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(8.dp))
            Text("Drag the slider to update progress.")
        }
    }
}
