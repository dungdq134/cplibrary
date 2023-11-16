package pl.cyfrowypolsat.cpplayercore.events.stats

import android.net.Uri

interface PlayerAnalyticsListener {

    fun onPlaybackStarted(playbackData: PlaybackData, autoplay: Boolean) {}
    fun onPlaybackFinished(playbackData: PlaybackData) {}

    fun onIsPlayingChanged(playbackData: PlaybackData, autoplay: Boolean) {}
    fun onBufferingStarted(playbackData: PlaybackData) {}
    fun onBufferingEnded(playbackData: PlaybackData) {}
    fun onQualityChanged(playbackData: PlaybackData) {}
    fun onVolumeChanged(playbackData: PlaybackData) {}
    fun onSeekProcessed(playbackData: PlaybackData) {}
    fun onUpdatePosition(playbackData: PlaybackData) {}

    fun onBandwidthEstimate(playbackData: PlaybackData, bitrateEstimate: Long) {}
    fun onVideoFramesDropped(playbackData: PlaybackData, droppedFrames: Long) {}

    fun onDrmSessionAcquired(playbackData: PlaybackData) {}
    fun onDrmKeysLoaded(playbackData: PlaybackData) {}
    fun onDrmSessionManagerError(playbackData: PlaybackData, throwable: Throwable) {}

    fun onBehindLiveWindowError(playbackData: PlaybackData, throwable: Throwable) {}
    fun onPlayerError(playbackData: PlaybackData, throwable: Throwable) {}

    fun onPlayerInitialized(playbackData: PlaybackData) {}
    fun onPlayerClosed(playbackData: PlaybackData) {}

    fun onOverlayStateChanged(playbackData: PlaybackData) {}

    fun onVideoChunkLoaded(playbackData: PlaybackData, loadedBytes: Long, uri: Uri) {}
    fun onAudioChunkLoaded(playbackData: PlaybackData, loadedBytes: Long, uri: Uri) {}

    fun onAdvertBlockStarted(playbackData: PlaybackData, advertPlaybackData: AdvertPlaybackData) {}
    fun onAdvertBlockFinished(playbackData: PlaybackData, advertPlaybackData: AdvertPlaybackData) {}

    fun onContentPauseRequested(playbackData: PlaybackData, advertPlaybackData: AdvertPlaybackData) {}
    fun onContentResumeRequested(playbackData: PlaybackData, advertPlaybackData: AdvertPlaybackData) {}

    fun onAdvertInitialized(playbackData: PlaybackData, advertPlaybackData: AdvertPlaybackData) {}
    fun onAdvertStarted(playbackData: PlaybackData, advertPlaybackData: AdvertPlaybackData, autoplay: Boolean) {}
    fun onAdvertFinished(playbackData: PlaybackData, advertPlaybackData: AdvertPlaybackData) {}
    fun onAdvertPaused(playbackData: PlaybackData, advertPlaybackData: AdvertPlaybackData) {}
    fun onAdvertUnPaused(playbackData: PlaybackData, advertPlaybackData: AdvertPlaybackData, autoplay: Boolean) {}
    fun onAdvertFirstQuartile(playbackData: PlaybackData, advertPlaybackData: AdvertPlaybackData) {}
    fun onAdvertMidPoint(playbackData: PlaybackData, advertPlaybackData: AdvertPlaybackData) {}
    fun onAdvertThirdQuartile(playbackData: PlaybackData, advertPlaybackData: AdvertPlaybackData) {}
    fun onAdvertError(playbackData: PlaybackData, throwable: Throwable) {}

}
