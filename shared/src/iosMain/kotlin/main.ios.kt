import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.window.ComposeUIViewController
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import sensor.SensorDataManager
import sensor.SensorManagerImpl


fun MainViewController() =
    ComposeUIViewController {
        val sensorManager = SensorManagerImpl()
        val scope = rememberCoroutineScope()

        DisposableEffect(Unit) {
            val dataManager = SensorDataManager()
            dataManager.startGyros()

            val job = scope.launch {
                dataManager.data
                    .receiveAsFlow()
                    .onEach { sensorManager.listener?.onUpdate(it) }
                    .collect()
            }

            onDispose {
                dataManager.stopGyros()
                job.cancel()
            }
        }
        App(sensorManager)
    }
