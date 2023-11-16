package pl.cyfrowypolsat.cpplayercore.events.stats

import android.os.Handler
import com.google.ads.interactivemedia.v3.api.Ad
import com.google.ads.interactivemedia.v3.api.AdErrorEvent
import com.google.ads.interactivemedia.v3.api.AdEvent
import com.google.ads.interactivemedia.v3.api.AdPodInfo
import androidx.media3.common.C.TRACK_TYPE_AUDIO
import androidx.media3.common.C.TRACK_TYPE_VIDEO
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player.*
import androidx.media3.exoplayer.analytics.AnalyticsListener
import androidx.media3.exoplayer.analytics.AnalyticsListener.EventTime
import androidx.media3.exoplayer.drm.DrmSession
import androidx.media3.exoplayer.source.LoadEventInfo
import androidx.media3.exoplayer.source.MediaLoadData
import androidx.media3.common.VideoSize
import pl.cyfrowypolsat.cpplayercore.core.AdvertPlayerException
import pl.cyfrowypolsat.cpplayercore.core.PlayerController
import pl.cyfrowypolsat.cpplayercore.core.PlayerException
import timber.log.Timber

class AnalyticsListenerMapper constructor(private val playerAnalyticsListeners: List<PlayerAnalyticsListener>,
                              private val playerController: PlayerController,
                              private val startPositionMs: Long,
                              private val durationMs: Long,
                              private val isAutoplay: Boolean) : AnalyticsListener {

    companion object {
        val UPDATE_POSITION_INTERVAL_IN_MS: Long = 1000
    }

    private var hasContentPlaybackStarted = false
    private var isBuffering = false
    private var isPlaying: Boolean? = null
    private var finished = false
    private var currentAdBlock: AdPodInfo? = null
    private var isPlayingAd: Boolean = false
    private var userAction: Boolean = false

    private val seekProcessedHandler = Handler()
    private val bufferingEndHandler = Handler()
    private val updatePositionHandler = Handler()

    private val updatePositionRunnable = object : Runnable {
        override fun run() {
            if (playerController.isPlaying()) {
                playerAnalyticsListeners.forEach { it.onUpdatePosition(buildPlaybackData()) }
            }
            updatePositionHandler.postDelayed(this, UPDATE_POSITION_INTERVAL_IN_MS)
        }
    }

    init {
        Timber.d("startPositionMs: $startPositionMs durationMs: $durationMs")
        playerAnalyticsListeners.forEach { it.onPlayerInitialized(buildInitPlaybackData()) }
    }

    fun release() {
        updatePositionHandler.removeCallbacksAndMessages(null)
        playerAnalyticsListeners.forEach { it.onPlayerClosed(buildPlaybackData()) }
    }

    override fun onRenderedFirstFrame(eventTime: AnalyticsListener.EventTime,
                                      output: Any,
                                      renderTimeMs: Long) {
        Timber.d("onRenderedFirstFrame")
        if (hasContentPlaybackStarted || playerController.isPlayingAdvert()) {
            //we want to sent playbackStarted only once when we render first frame of real content
            return
        }

        Timber.d("onRenderedFirstFrame - first time real content")
        handlePlaybackStarted()
    }

    override fun onIsPlayingChanged(eventTime: AnalyticsListener.EventTime,
                                    isPlaying: Boolean) {
        if (!hasContentPlaybackStarted || finished || this.isPlaying == isPlaying) {
            return
        }

        Timber.d("onIsPlayingChanged - isPlaying: $isPlaying")
        this.isPlaying = isPlaying

        playerAnalyticsListeners.forEach { it.onIsPlayingChanged(buildPlaybackData(), autoplay = !userAction) }
        if (isPlaying) {
            userAction = false
        }
    }

    override fun onPositionDiscontinuity(eventTime: EventTime,
                                         oldPosition: PositionInfo,
                                         newPosition: PositionInfo,
                                         reason: @DiscontinuityReason Int) {

        Timber.d("onPositionDiscontinuity reason $reason " +
                "oldPosition.positionMs ${oldPosition.positionMs} " +
                "newPosition.positionMs ${newPosition.positionMs}")

        if (reason == DISCONTINUITY_REASON_SEEK) {
            seekProcessedHandler.removeCallbacksAndMessages(null)
            seekProcessedHandler.postDelayed(Runnable {
                if (!hasContentPlaybackStarted) {
                    return@Runnable
                }

                userAction = true
                playerAnalyticsListeners.forEach { it.onSeekProcessed(buildPlaybackData()) }
            }, 500)
        }
    }

    override fun onLoadCompleted(eventTime: AnalyticsListener.EventTime,
                                 loadEventInfo: LoadEventInfo,
                                 mediaLoadData: MediaLoadData) {
        when (mediaLoadData.trackType) {
            TRACK_TYPE_VIDEO -> playerAnalyticsListeners.forEach {
                it.onVideoChunkLoaded(buildPlaybackData(), loadEventInfo.bytesLoaded, loadEventInfo.uri)
            }
            TRACK_TYPE_AUDIO -> playerAnalyticsListeners.forEach {
                it.onAudioChunkLoaded(buildPlaybackData(), loadEventInfo.bytesLoaded, loadEventInfo.uri)
            }
        }
    }

    override fun onPlaybackStateChanged(eventTime: EventTime, playbackState: @State Int) {
        if (!hasContentPlaybackStarted) {
            return
        }

        Timber.d("onPlayerStateChanged - playbackState: $playbackState")
        when (playbackState) {
            STATE_IDLE -> {
            }
            STATE_BUFFERING -> {
                bufferingEndHandler.removeCallbacksAndMessages(null)
                if (!isBuffering) {
                    playerAnalyticsListeners.forEach { it.onBufferingStarted(buildPlaybackData()) }
                    isBuffering = true
                }
            }
            STATE_READY -> {
                if (isBuffering) {
                    bufferingEndHandler.removeCallbacksAndMessages(null)
                    bufferingEndHandler.postDelayed({
                        playerAnalyticsListeners.forEach { it.onBufferingEnded(buildPlaybackData()) }
                        isBuffering = false
                    }, 500)
                }
            }
            STATE_ENDED -> {
                finished = true
                playerAnalyticsListeners.forEach { it.onPlaybackFinished(buildPlaybackData()) }
            }
        }
    }

    override fun onPlayWhenReadyChanged(eventTime: AnalyticsListener.EventTime,
                                        playWhenReady: Boolean,
                                        reason: Int) {
        if (reason == PLAY_WHEN_READY_CHANGE_REASON_USER_REQUEST && !playWhenReady) {
            userAction = true
        }
        Timber.d("onPlayWhenReadyChanged: $playWhenReady reason: $reason")
    }

    override fun onVideoSizeChanged(eventTime: EventTime, videoSize: VideoSize) {
        if (!hasContentPlaybackStarted) {
            return
        }
        Timber.d("onVideoSizeChanged - width: ${videoSize.width}, height: $${videoSize.height}")
        playerAnalyticsListeners.forEach { it.onQualityChanged(buildPlaybackData()) }
    }

    override fun onVolumeChanged(eventTime: AnalyticsListener.EventTime,
                                 volume: Float) {
        Timber.d("onVolumeChanged - volume: $volume")
        playerAnalyticsListeners.forEach { it.onVolumeChanged(buildPlaybackData()) }
    }

    override fun onBandwidthEstimate(eventTime: AnalyticsListener.EventTime,
                                     totalLoadTimeMs: Int,
                                     totalBytesLoaded: Long,
                                     bitrateEstimate: Long) {
        playerAnalyticsListeners.forEach { it.onBandwidthEstimate(buildPlaybackData(), bitrateEstimate) }
    }

    override fun onDrmSessionAcquired(eventTime: AnalyticsListener.EventTime, state: @DrmSession.State Int) {
        Timber.d("onDrmKeysAcquired")
        playerAnalyticsListeners.forEach { it.onDrmSessionAcquired(buildPlaybackData()) }
    }

    override fun onDrmKeysLoaded(eventTime: AnalyticsListener.EventTime) {
        Timber.d("onDrmKeysLoaded")
        playerAnalyticsListeners.forEach { it.onDrmKeysLoaded(buildPlaybackData()) }
    }

    override fun onDrmSessionManagerError(eventTime: AnalyticsListener.EventTime,
                                          error: Exception) {
        Timber.d("onDrmSessionManagerError")
        playerAnalyticsListeners.forEach { it.onDrmSessionManagerError(buildPlaybackData(), error) }
    }

    override fun onDroppedVideoFrames(eventTime: AnalyticsListener.EventTime,
                                      droppedFrames: Int,
                                      elapsedMs: Long) {
        Timber.d("onDroppedVideoFrames - count: $droppedFrames")
        playerAnalyticsListeners.forEach { it.onVideoFramesDropped(buildPlaybackData(), droppedFrames.toLong()) }
    }

    override fun onPlayerError(eventTime: AnalyticsListener.EventTime,
                               error: PlaybackException) {
        Timber.e("onPlayerError")
        Timber.e(error)

        val playerException = PlayerException.create(error)

        if (playerException.code == PlaybackException.ERROR_CODE_BEHIND_LIVE_WINDOW) {
            playerAnalyticsListeners.forEach { it.onBehindLiveWindowError(buildPlaybackData(), playerException) }
        } else {
            playerAnalyticsListeners.forEach { it.onPlayerError(buildPlaybackData(), playerException) }
        }
    }

    fun onOverlayStateChangedEvent() {
        playerAnalyticsListeners.forEach { listener ->
            listener.onOverlayStateChanged(buildPlaybackData())
        }
    }

    fun onOverlayError(throwable: Throwable) {
        playerAnalyticsListeners.forEach { it.onPlayerError(buildPlaybackData(), throwable) }
    }

    fun onAdEvent(adEvent: AdEvent?) {
        adEvent?.let {
            if (it.type == AdEvent.AdEventType.AD_PROGRESS) {
                return
            }
            Timber.d("type: ${adEvent.type}, ad: ${adEvent.ad}")
            when (it.type) {
                AdEvent.AdEventType.CONTENT_PAUSE_REQUESTED -> {
                    isPlayingAd = true
                    currentAdBlock = it.ad?.adPodInfo
                    playerAnalyticsListeners.forEach { listener ->
                        listener.onContentPauseRequested(buildPlaybackData(), buildAdvertPlaybackData(it.ad))
                    }
                    playerAnalyticsListeners.forEach { listener ->
                        listener.onAdvertBlockStarted(buildPlaybackData(), buildAdvertPlaybackData(it.ad))
                    }
                }
                AdEvent.AdEventType.CONTENT_RESUME_REQUESTED -> {
                    if (!isPlayingAd) {
                        return
                    }
                    playerAnalyticsListeners.forEach { listener ->
                        listener.onAdvertBlockFinished(buildPlaybackData(), buildAdvertPlaybackData(it.ad))
                    }
                    playerAnalyticsListeners.forEach { listener ->
                        listener.onContentResumeRequested(buildPlaybackData(), buildAdvertPlaybackData(it.ad))
                    }
                    currentAdBlock = null
                    isPlayingAd = false
                    if (!hasContentPlaybackStarted) {
                        //weird bug, after preroll completed, sometimes we don't get proper onRenderedFirstFrame event
                        handlePlaybackStarted()
                    }
                }

                AdEvent.AdEventType.STARTED -> {
                    playerAnalyticsListeners.forEach { listener ->
                        listener.onAdvertInitialized(buildPlaybackData(), buildAdvertPlaybackData(it.ad))
                    }
                    playerAnalyticsListeners.forEach { listener ->
                        listener.onAdvertStarted(buildPlaybackData(), buildAdvertPlaybackData(it.ad), autoplay = true)
                    }
                }

                AdEvent.AdEventType.FIRST_QUARTILE -> playerAnalyticsListeners.forEach { listener ->
                    listener.onAdvertFirstQuartile(buildPlaybackData(), buildAdvertPlaybackData(it.ad))
                }

                AdEvent.AdEventType.MIDPOINT -> playerAnalyticsListeners.forEach { listener ->
                    listener.onAdvertMidPoint(buildPlaybackData(), buildAdvertPlaybackData(it.ad))
                }

                AdEvent.AdEventType.THIRD_QUARTILE -> playerAnalyticsListeners.forEach { listener ->
                    listener.onAdvertThirdQuartile(buildPlaybackData(), buildAdvertPlaybackData(it.ad))
                }

                AdEvent.AdEventType.COMPLETED -> playerAnalyticsListeners.forEach { listener ->
                    listener.onAdvertFinished(buildPlaybackData(), buildAdvertPlaybackData(it.ad))
                }

                AdEvent.AdEventType.PAUSED -> playerAnalyticsListeners.forEach { listener ->
                    listener.onAdvertPaused(buildPlaybackData(), buildAdvertPlaybackData(it.ad))
                }

                AdEvent.AdEventType.RESUMED -> playerAnalyticsListeners.forEach { listener ->
                    listener.onAdvertUnPaused(buildPlaybackData(), buildAdvertPlaybackData(it.ad), autoplay = false)
                }

                else -> return
            }
        }
    }

    fun onAdError(adErrorEvent: AdErrorEvent?) {
        Timber.d("type: ${adErrorEvent?.error?.errorType?.name}")
        adErrorEvent?.error?.let { adError ->
            val advertPlayerException = AdvertPlayerException.create(adError)
            playerAnalyticsListeners.forEach { it.onAdvertError(buildPlaybackData(), advertPlayerException) }
        }
    }

    private fun handlePlaybackStarted() {
        if (hasContentPlaybackStarted) {
            return
        }

        playerAnalyticsListeners.forEach { it.onPlaybackStarted(buildPlaybackData(), isAutoplay) }
        hasContentPlaybackStarted = true
        isPlaying = true
        updatePositionHandler.postDelayed(updatePositionRunnable, UPDATE_POSITION_INTERVAL_IN_MS)
    }

    private fun buildInitPlaybackData(): PlaybackData {
        return playerController.let {
            PlaybackData(currentPositionMs = if (startPositionMs < 0) 0 else startPositionMs,
                    durationMs = durationMs,
                    currentQuality = it.getCurrentQuality(),
                    isPlaying = isPlaying ?: false,
                    isPlayingAdvert = it.isPlayingAdvert(),
                    currentVolumeLevel = it.getCurrentVolumeLevel(),
                    frameRate = it.getFrameRate(),
                    dynamic = it.isDynamic(),
                    advertsCuePoints = it.getAdsCuePoints(),
                    activeOverlays = it.getActiveOverlays()
            )
        }
    }

    private fun buildPlaybackData(): PlaybackData {
        return playerController.let {
            PlaybackData(currentPositionMs = it.getCurrentPosition(),
                    durationMs = it.getDuration(),
                    currentQuality = it.getCurrentQuality(),
                    isPlaying = isPlaying ?: false,
                    isPlayingAdvert = it.isPlayingAdvert(),
                    currentVolumeLevel = it.getCurrentVolumeLevel(),
                    frameRate = it.getFrameRate(),
                    dynamic = it.isDynamic(),
                    advertsCuePoints = it.getAdsCuePoints(),
                    activeOverlays = it.getActiveOverlays()
            )
        }
    }

    private fun buildAdvertPlaybackData(advert: Ad?): AdvertPlaybackData {
        val adPodInfo = advert?.adPodInfo ?: currentAdBlock

        return AdvertPlaybackData(
                advertId = advert?.adId ?: "",
                advertTitle = advert?.title ?: "",
                advertDurationSeconds = advert?.duration?.toLong() ?: 0,
                advertIndex = adPodInfo?.adPosition ?: -99,
                blockIndex = adPodInfo?.podIndex ?: -99,
                blockTimeOffsetSeconds = adPodInfo?.timeOffset ?: -99.0,
                blockTotalAdsCount = adPodInfo?.totalAds ?: 0
        )
    }
}