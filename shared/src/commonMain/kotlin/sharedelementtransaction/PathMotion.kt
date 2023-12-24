package sharedelementtransaction

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.lerp

 open class PathMotion(val a:Int) {
    open operator fun invoke(start: Offset, end: Offset, fraction: Float) = lerp(start, end, fraction)
}
typealias PathMotionFactory = () -> PathMotion

val LinearMotion: PathMotion = PathMotion(2)

val LinearMotionFactory: PathMotionFactory = { LinearMotion }
