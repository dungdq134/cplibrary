package pl.cyfrowypolsat.cpcommon.core.extensions

import android.text.format.DateUtils
import java.util.*

fun Date?.isToday(): Boolean {
    this ?: return false
    return DateUtils.isToday(this.time)
}

fun Date?.isNotToday(): Boolean {
    this ?: return false
    return !isToday()
}

fun Date.isYesterday(): Boolean {
    return DateUtils.isToday(this.time + DateUtils.DAY_IN_MILLIS)
}

fun Date.isNotYesterday(): Boolean {
    return !isYesterday()
}

fun Date.isTomorrow(): Boolean {
    return DateUtils.isToday(this.time - DateUtils.DAY_IN_MILLIS)
}

fun Date.isNotTomorrow(): Boolean {
    return !isTomorrow()
}

fun Date.isWithin24h(): Boolean {
    return this.time <= System.currentTimeMillis() + DateUtils.DAY_IN_MILLIS
}

fun Date.isWithin12h(): Boolean {
    return this.time <= System.currentTimeMillis() + 12 * DateUtils.HOUR_IN_MILLIS
}

fun Date.isBefore(): Boolean {
    val now = Calendar.getInstance()
    return now.time.time >= time
}

fun Date.isLater(): Boolean {
    return !isBefore()
}

fun Date?.isLaterOrNull(): Boolean {
    this ?: return true
    return !isBefore()
}

fun findDateClosestToDate(dates: List<Date>, date: Date): Date? {
    val sortedDates = dates.sortedBy { it.time }
    var nearestDate = sortedDates.firstOrNull() ?: return null

    if (date.time < nearestDate.time) {
        return nearestDate
    }

    for (i in sortedDates.indices) {
        if (sortedDates[i].time <= date.time) {
            nearestDate = sortedDates[i]
        } else {
            break
        }
    }
    return nearestDate
}