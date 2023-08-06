import androidx.compose.runtime.Composable
import sensor.SensorManager

@Composable
fun RecipeAppWeb() {
    val sensorManager = SensorManager()
    App(sensorManager, true)
}
