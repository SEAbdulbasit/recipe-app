import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import sensor.SensorManager


@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    CanvasBasedWindow("RecipeApp") {
        val sensorManager = SensorManager()
        App(sensorManager,true)
    }
}
