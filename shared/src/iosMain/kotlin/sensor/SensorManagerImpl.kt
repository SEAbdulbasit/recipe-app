package sensor

class SensorManagerImpl : SensorManager {
    var listener: Listener? = null

    override fun registerListener(listener: Listener) {
        this.listener = listener
    }

}