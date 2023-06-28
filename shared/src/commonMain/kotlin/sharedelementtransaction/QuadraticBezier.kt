package sharedelementtransaction

import androidx.compose.ui.geometry.Offset

internal object QuadraticBezier {

    private class PointEntry(
        val t: Float,
        val point: Offset
    ) {
        var next: PointEntry? = null
    }

    private fun calculate(t: Float, p0: Float, p1: Float, p2: Float): Float {
        val oneMinusT = 1 - t
        return oneMinusT * (oneMinusT * p0 + t * p1) + t * (oneMinusT * p1 + t * p2)
    }

    private fun coordinate(t: Float, p0: Offset, p1: Offset, p2: Offset): Offset =
        Offset(
            calculate(t, p0.x, p1.x, p2.x),
            calculate(t, p0.y, p1.y, p2.y)
        )

    fun approximate(
        p0: Offset, p1: Offset, p2: Offset,
        acceptableError: Float
    ): Pair<FloatArray, LongArray> {
        val errorSquared = acceptableError * acceptableError

        val start = PointEntry(0f, coordinate(0f, p0, p1, p2))
        var cur = start
        var next = PointEntry(1f, coordinate(1f, p0, p1, p2))
        start.next = next
        var count = 2
        while (true) {
            var needsSubdivision: Boolean
            do {
                val midT = (cur.t + next.t) / 2
                val midX = (cur.point.x + next.point.x) / 2
                val midY = (cur.point.y + next.point.y) / 2

                val midPoint = coordinate(midT, p0, p1, p2)
                val xError = midPoint.x - midX
                val yError = midPoint.y - midY
                val midErrorSquared = (xError * xError) + (yError * yError)
                needsSubdivision = midErrorSquared > errorSquared

                if (needsSubdivision) {
                    val new = PointEntry(midT, midPoint)
                    cur.next = new
                    new.next = next
                    next = new
                    count++
                }
            } while (needsSubdivision)
            cur = next
            next = cur.next ?: break
        }

        cur = start
        var length = 0f
        var last = Offset.Unspecified
        val result = LongArray(count)
        val lengths = FloatArray(count)
        for (i in result.indices) {
            val point = cur.point
            @Suppress("INVISIBLE_MEMBER")
            result[i] = point.packedValue
            if (i > 0) {
                val distance = (point - last).getDistance()
                length += distance
                lengths[i] = length
            }
            cur = cur.next ?: break
            last = point
        }

        if (length > 0) {
            for (index in lengths.indices) {
                lengths[index] /= length
            }
        }

        return lengths to result
    }

}
