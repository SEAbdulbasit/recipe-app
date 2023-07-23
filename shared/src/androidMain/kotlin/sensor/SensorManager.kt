package sensor

actual class SensorManager {
    var listener: Listener? = null
    actual fun registerListener(listener: Listener) {
        this.listener = listener
    }

}