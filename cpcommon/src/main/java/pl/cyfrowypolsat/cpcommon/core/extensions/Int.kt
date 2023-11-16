package pl.cyfrowypolsat.cpcommon.core.extensions

import android.content.res.Resources
import kotlin.math.roundToInt

val Int.dp
    get() = (Resources.getSystem().displayMetrics.density * this).toInt()

val Int.sp
    get() = (Resources.getSystem().displayMetrics.scaledDensity * this)

fun Int.percentRange(): Int {
    return this.coerceIn(0, 100)
}

fun Int.invertedPercentRange(): Int {
    return 100 - percentRange()
}

fun Int.asPercentOf(value: Int): Int {
    return ((this.percentRange() * value).toFloat() / 100).toInt()
}

fun Int.asInvertedPercentOf(value: Int): Int {
    return invertedPercentRange().asPercentOf(value)
}

fun percentFirstOfSecond(first: Float, second: Float): Int {
    return (first / second * 100).roundToInt()
}