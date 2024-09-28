package sensor

fun interface SensorManager {
    fun registerListener(listener: Listener)
}


interface Listener {
    fun onUpdate(sensorData: SensorData)
}

data class SensorData(
    val roll: Float,
    val pitch: Float
)