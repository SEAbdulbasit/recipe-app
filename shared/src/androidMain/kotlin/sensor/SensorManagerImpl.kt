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
        sensorDataManager.init()
    }

    override fun registerListener(listener: Listener) {
        scope.launch {
            for (data in sensorDataManager.data) {
                listener.onUpdate(data)
            }
        }
    }
    
    fun cancel() {
        sensorDataManager.cancel()
        scope.cancel()
    }
}