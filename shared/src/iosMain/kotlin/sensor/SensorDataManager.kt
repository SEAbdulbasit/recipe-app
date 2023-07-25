package sensor

import kotlinx.coroutines.channels.Channel
import platform.CoreMotion.CMMotionManager
import platform.Foundation.NSOperationQueue


/**
 * Created by abdulbasit on 23/07/2023.
 */

class SensorDataManager() {
    var motion = CMMotionManager()
    val data: Channel<SensorData> = Channel(Channel.UNLIMITED)

    fun startGyros() {
        if (motion.isGyroAvailable()) {
            motion.gyroUpdateInterval = 1.0 / 50.0
            motion.startGyroUpdates()
            motion.startDeviceMotionUpdatesToQueue(NSOperationQueue.currentQueue!!) { motion, error ->
                if (motion != null) {
                    val attitude = motion.attitude
                    data.trySend(
                        SensorData(
                            roll = attitude.roll.toFloat(),
                            pitch = attitude.pitch.toFloat()
                        )
                    )
                }
            }
        }
    }

    fun stopGyros() {
        motion.stopGyroUpdates()
    }
}

