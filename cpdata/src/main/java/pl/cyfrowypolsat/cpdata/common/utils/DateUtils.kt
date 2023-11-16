package pl.cyfrowypolsat.cpdata.common.utils

import java.text.SimpleDateFormat
import java.util.*


fun getCurrentIsoDateTime(): String {
    return getIsoDateTime(Date())
}

fun getIsoDateTime(date: Date): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
    dateFormat.timeZone = TimeZone.getTimeZone("UTC")
    return dateFormat.format(date)
}

fun parseIsoDateTime(date: String?): Date? {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
    return date?.let { dateFormat.parse(it) }
}

fun getCurrentDate(outputFormat: String): String {
    return getDate(outputFormat)
}

fun getDate(outputFormat: String,
            date: Date = Date()): String {
    val dateFormat = SimpleDateFormat(outputFormat, Locale.US)
    return dateFormat.format(date)
}

fun endOfDay(date: Date): Date {
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    calendar.time = date
    calendar.set(Calendar.HOUR_OF_DAY, 23)
    calendar.set(Calendar.MINUTE, 59)
    calendar.set(Calendar.SECOND, 59)
    calendar.set(Calendar.MILLISECOND, 0)
    return calendar.time
}

fun midday(date: Date): Date {
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    calendar.time = date
    calendar.set(Calendar.HOUR_OF_DAY, 12)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 1)
    return calendar.time
}
