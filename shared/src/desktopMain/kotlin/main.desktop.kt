import androidx.compose.runtime.Composable
import sensor.SensorManager


@Composable
fun MainView() {
    val sensorManager = SensorManager()
    App(sensorManager, true)
}