package sharedelementtransaction

import androidx.compose.ui.geometry.Offset

class MaterialArcMotion : KeyframeBasedMotion() {

    override fun getKeyframes(start: Offset, end: Offset): Pair<FloatArray, LongArray> =
        QuadraticBezier.approximate(
            start,
            if (start.y > end.y) Offset(end.x, start.y) else Offset(start.x, end.y),
            end,
            0.5f
        )

}

val MaterialArcMotionFactory: PathMotionFactory = { MaterialArcMotion() }
