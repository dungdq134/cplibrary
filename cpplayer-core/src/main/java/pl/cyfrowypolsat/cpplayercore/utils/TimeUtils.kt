package pl.cyfrowypolsat.cpplayercore.utils

fun formatTime(leftTime: Long): String {
    val ms = maxOf(leftTime, 0)
    val sb = StringBuilder()
    sb.setLength(0)

    var seconds: Long = ms / 1000
    val minutes = seconds / 60
    seconds -= minutes * 60

    if (minutes < 10) {
        sb.append('0')
    }

    sb.append(minutes).append(':')
    if (seconds < 10) {
        sb.append('0')
    }
    sb.append(seconds)

    return sb.toString()
}