package sharedelementtransaction

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.lerp

abstract class KeyframeBasedMotion : PathMotion(2) {

    private var start = Offset.Unspecified
    private var end = Offset.Unspecified
    private var keyframes: Pair<FloatArray, LongArray>? = null

    protected abstract fun getKeyframes(start: Offset, end: Offset): Pair<FloatArray, LongArray>

    private fun LongArray.getOffset(index: Int) =
        @Suppress("INVISIBLE_MEMBER") Offset(get(index))


     override fun invoke(start: Offset, end: Offset, fraction: Float): Offset {
        var frac = fraction
        if (start != this.start || end != this.end) {
            if (start == this.end && end == this.start) {
                frac = 1 - frac
            } else {
                keyframes = null
                this.start = start
                this.end = end
            }
        }
        val (fractions, offsets) = keyframes ?: getKeyframes(start, end).also { keyframes = it }
        val count = fractions.size

        return when {
            frac < 0f -> interpolateInRange(fractions, offsets, frac, 0, 1)
            frac > 1f -> interpolateInRange(fractions, offsets, frac, count - 2, count - 1)
            frac == 0f -> offsets.getOffset(0)
            frac == 1f -> offsets.getOffset(count - 1)
            else -> {
                // Binary search for the correct section
                var low = 0
                var high = count - 1
                while (low <= high) {
                    val mid = (low + high) / 2
                    val midFraction = fractions[mid]

                    when {
                        frac < midFraction -> high = mid - 1
                        frac > midFraction -> low = mid + 1
                        else -> return offsets.getOffset(mid)
                    }
                }

                // now high is below the fraction and low is above the fraction
                interpolateInRange(fractions, offsets, frac, high, low)
            }
        }
    }

    private fun interpolateInRange(
        fractions: FloatArray, offsets: LongArray,
        fraction: Float, startIndex: Int, endIndex: Int
    ): Offset {
        val startFraction = fractions[startIndex]
        val endFraction = fractions[endIndex]
        val intervalFraction = (fraction - startFraction) / (endFraction - startFraction)
        val start = offsets.getOffset(startIndex)
        val end = offsets.getOffset(endIndex)
        return lerp(start, end, intervalFraction)
    }

}
