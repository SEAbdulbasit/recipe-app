import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import sensor.SensorManagerImpl

@Composable
fun MainView(isLargeScreen: Boolean = false) {
    val context = LocalContext.current
    val sensorManager = remember { SensorManagerImpl(context) }

    DisposableEffect(Unit) {
        onDispose {
            sensorManager.cancel()
        }
    }

    App(sensorManager, isLargeScreen)
}
