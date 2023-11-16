package pl.cyfrowypolsat.cpplayercore.events.stats

import pl.cyfrowypolsat.cpplayercore.configuration.OverlayInfo

data class PlaybackData(
        val currentPositionMs: Long,
        val durationMs: Long,
        val currentQuality: Int,
        val isPlaying: Boolean,
        val isPlayingAdvert: Boolean,
        val currentVolumeLevel: Float,
        val frameRate: Float,
        val dynamic: Boolean,
        val advertsCuePoints: List<Float>,
        val activeOverlays: List<OverlayInfo>
)