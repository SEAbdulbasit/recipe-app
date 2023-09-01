package sharedelementtransaction

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlin.jvm.JvmInline

@JvmInline
@Immutable
value class ProgressThresholds(private val packedValue: Long) {

    @Stable
    val start: Float
        get() = unpackFloat1(packedValue)

    @Stable
    val end: Float
        get() = unpackFloat2(packedValue)

    @Suppress("NOTHING_TO_INLINE")
    @Stable
    inline operator fun component1(): Float = start

    @Suppress("NOTHING_TO_INLINE")
    @Stable
    inline operator fun component2(): Float = end

}

/**
 * Unpacks the first Float value in [packFloats] from its returned Long.
 */
inline fun unpackFloat1(value: Long): Float {
    return Float.fromBits(value.shr(32).toInt())
}


/**
 * Unpacks the second Float value in [packFloats] from its returned Long.
 */
inline fun unpackFloat2(value: Long): Float {
    return Float.fromBits(value.and(0xFFFFFFFF).toInt())
}

/**
 * Packs two Float values into one Long value for use in inline classes.
 */
inline fun packFloats(val1: Float, val2: Float): Long {
    val v1 = val1.toBits().toLong()
    val v2 = val2.toBits().toLong()
    return v1.shl(32) or (v2 and 0xFFFFFFFF)
}

@Stable
fun ProgressThresholds(start: Float, end: Float) = ProgressThresholds(packFloats(start, end))

@Stable
internal fun ProgressThresholds.applyTo(fraction: Float): Float = when {
    fraction < start -> 0f
    fraction in start..end -> (fraction - start) / (end - start)
    else -> 1f
}
