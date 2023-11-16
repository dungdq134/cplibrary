package pl.cyfrowypolsat.cpplayer.startover

import pl.cyfrowypolsat.cpcommon.core.extensions.findDateClosestToDate
import pl.cyfrowypolsat.cpdata.api.common.model.cpidobject.ChannelProgramItemResult
import java.util.*

fun List<ChannelProgramItemResult>.currentProgram(currentDate: Date?): ChannelProgramItemResult? {
    return if (currentDate != null) closestToDate(currentDate) else null
}

fun List<ChannelProgramItemResult>.closestToDate(date: Date): ChannelProgramItemResult? {
    val dates = this.map { it.startTime }
    val closestDate = findDateClosestToDate(dates, date)
    return firstOrNull { it.startTime == closestDate }
}