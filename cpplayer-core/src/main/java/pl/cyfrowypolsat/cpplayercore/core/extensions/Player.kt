package pl.cyfrowypolsat.cpplayercore.core.extensions

import androidx.media3.common.C
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import java.util.*

fun Player.getDefaultPosition(): Long? {
    val timeline = currentTimeline
    if (timeline.isEmpty) return null
    val window = timeline.getWindow(currentMediaItemIndex, Timeline.Window())
    return window.defaultPositionMs
}

fun Player.getDisplayedDuration(): Long {
    val defaultPosition = getDefaultPosition()
    if (isCurrentMediaItemDynamic && defaultPosition != null) {
        return defaultPosition
    }
    val durationMs = duration
    return if (durationMs == C.TIME_UNSET) -1 else durationMs
}

fun Player.getPositionForDate(date: Date?): Long {
    date?.takeIf { it.time >= 0 } ?: return 0
    val now = Date().time
    var positionMs = duration - (now - date.time)
    getDefaultPosition()?.let { defaultPosition ->
        if (positionMs > defaultPosition) positionMs = defaultPosition
    }
    return positionMs
}

fun Player.seekToDate(date: Date?) {
    seekTo(getPositionForDate(date))
}