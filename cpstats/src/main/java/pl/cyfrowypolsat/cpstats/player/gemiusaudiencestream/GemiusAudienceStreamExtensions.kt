package pl.cyfrowypolsat.cpstats.player.gemiusaudiencestream

import com.gemius.sdk.stream.*

internal fun EventProgramData.toDebugString(): String {
    return "{volume: $volume, autoPlay: $autoPlay, resolution: $resolution}"
}

internal fun EventAdData.toDebugString(): String {
    return "{volume: $volume, autoPlay: $autoPlay, resolution: $resolution}"
}

internal fun PlayerData.toDebugString(): String {
    return "{volume: $volume, resolution: $resolution}"
}

internal fun ProgramData.toDebugString(): String {
    return "{name: $name, duration: $duration, programType: $programType, transmissionType: $transmissionType, transmissionChannel(only for live): $transmissionChannel, transmissionStartTime (only for live): $transmissionStartTime, volume: $volume, resolution: $resolution}"
}

internal fun AdData.toDebugString(): String {
    return "{volume: $volume, resolution: $resolution}"
}