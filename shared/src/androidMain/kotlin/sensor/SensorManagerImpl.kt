package sensor

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class SensorManagerImpl(context: Context) : SensorManager {
    private val sensorDataManager = SensorDataManager(context)
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    
    init {
        println("SensorManagerImpl: Initializing with context = $context")
        sensorDataManager.init()
        println("SensorManagerImpl: Initialization complete")
    }

    override fun registerListener(listener: Listener) {
        println("SensorManagerImpl: Listener registered")
        scope.launch {
            println("SensorManagerImpl: Starting to listen for sensor data...")
            for (data in sensorDataManager.data) {
                println("SensorManagerImpl: Received data from channel - roll: ${data.roll}, pitch: ${data.pitch}")
                listener.onUpdate(data)
            }
        }
    }
    
    fun cancel() {
        println("SensorManagerImpl: Cancelling")
        sensorDataManager.cancel()
        scope.cancel()
    }
}