package pl.cyfrowypolsat.cpplayercore.core.extensions

fun StringBuilder.formatPlayerTime(timeMs: Long) {
    setLength(0)
    val ms = Math.max(timeMs, 0)

    var seconds = ms / 1000
    var minutes = seconds / 60
    val hours = minutes / 60
    seconds -= minutes * 60
    minutes -= hours * 60

    if (hours < 10) {
        append('0')
    }

    append(hours).append(':')
    if (minutes < 10) {
        append('0')
    }

    append(minutes).append(':')
    if (seconds < 10) {
        append('0')
    }
    append(seconds)
}

fun StringBuilder.getPlayerTime(timeMs: Long): String {
    formatPlayerTime(timeMs)
    return toString()
}

fun StringBuilder.getLivePlayerTime(timeMs: Long): String {
    formatPlayerTime(timeMs)
    insert(0, "-")
    return toString()
}