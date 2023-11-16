package pl.cyfrowypolsat.cpplayercore.core

import android.content.Context
import android.media.AudioManager
import android.net.Uri
import com.google.ads.interactivemedia.v3.api.AdErrorEvent
import com.google.ads.interactivemedia.v3.api.AdEvent
import com.google.ads.interactivemedia.v3.api.ImaSdkFactory
import androidx.media3.common.AudioAttributes
import androidx.media3.common.AdViewProvider
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.cronet.CronetUtil
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.RenderersFactory
import androidx.media3.exoplayer.drm.DefaultDrmSessionManager
import androidx.media3.exoplayer.drm.DefaultDrmSessionManagerProvider
import androidx.media3.exoplayer.drm.DrmSessionManagerProvider
import androidx.media3.exoplayer.drm.FrameworkMediaDrm
import androidx.media3.exoplayer.ima.ImaAdsLoader
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.exoplayer.source.ads.AdsLoader
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import androidx.media3.exoplayer.util.EventLogger
import pl.cyfrowypolsat.cpplayercore.BuildConfig
import pl.cyfrowypolsat.cpplayercore.configuration.MsStartPosition
import pl.cyfrowypolsat.cpplayercore.configuration.OverlayInfo
import pl.cyfrowypolsat.cpplayercore.configuration.OverlayType
import pl.cyfrowypolsat.cpplayercore.configuration.PlayerConfig
import pl.cyfrowypolsat.cpplayercore.core.audio.HdmiDeviceCallback
import pl.cyfrowypolsat.cpplayercore.core.droppedframes.DroppedFramesManager
import pl.cyfrowypolsat.cpplayercore.core.exo.CPDefaultRenderersFactory
import pl.cyfrowypolsat.cpplayercore.core.exo.CPMediaCodecSelector
import pl.cyfrowypolsat.cpplayercore.core.exo.datasource.cronet.CPCronetDataSourceFactory
import pl.cyfrowypolsat.cpplayercore.core.exo.datasource.defaulthttp.CPDefaultHttpDataSourceFactory
import pl.cyfrowypolsat.cpplayercore.core.extensions.getDefaultPosition
import pl.cyfrowypolsat.cpplayercore.core.extensions.getPositionForDate
import pl.cyfrowypolsat.cpplayercore.core.ima.ImaBugFixManager
import pl.cyfrowypolsat.cpplayercore.core.sharedprefs.PlayerSettingsSharedPrefs
import pl.cyfrowypolsat.cpplayercore.events.player.OnStartSeekToDateListener
import pl.cyfrowypolsat.cpplayercore.events.playerview.PlayerViewListener
import pl.cyfrowypolsat.cpplayercore.events.playerview.PlayerViewListenerMapper
import pl.cyfrowypolsat.cpplayercore.events.stats.AnalyticsListenerMapper
import java.util.*
import java.util.concurrent.Executors


open class PlayerController(protected val playerConfig: PlayerConfig,
                            private val adViewProvider: AdViewProvider,
                            protected val context: Context,
                            private val playerViewListener: PlayerViewListener? = null) : AdEvent.AdEventListener,
        AdErrorEvent.AdErrorListener,
        AdsLoader.Provider {

    companion object {
        private const val LOCALE_PL = "pl"
    }

    private lateinit var dataSourceFactory: DefaultDataSource.Factory
    protected var mediaItem: MediaItem? = null
    private var adsLoader: ImaAdsLoader? = null
    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as? AudioManager
    private var hdmiDeviceCallback: HdmiDeviceCallback? = null

    private val playerSettingsSharedPrefs = PlayerSettingsSharedPrefs(context)
    private var analyticsListenerMapper: AnalyticsListenerMapper? = null
    private var playerViewListenerMapper: PlayerViewListenerMapper? = null
    private var imaBugFixManager: ImaBugFixManager? = null

    private var startPosition = playerConfig.startPosition
        set(value) {
            field = value
            clearOnStartSeekToDateListener()
        }

    private var duration = playerConfig.durationMs

    var trackSelector: DefaultTrackSelector? = null
    var player: ExoPlayer? = null
    private var onStartSeekToDateListener: OnStartSeekToDateListener? = null

    private val activeOverlays = playerConfig.overlays.associateWith { it.autostart }.toMutableMap()

    init {
        val startPositionDate = startPosition.startPositionDate()
        startPositionDate?.let { date ->
            onStartSeekToDateListener = OnStartSeekToDateListener(date) {
                seekToDate(it)
                clearOnStartSeekToDateListener()
            }
        }
    }

    private fun clearOnStartSeekToDateListener() {
        onStartSeekToDateListener?.let { player?.removeListener(it) }
        onStartSeekToDateListener = null
    }


    fun initializePlayer() {
        dataSourceFactory = createDataSourceFactory()
        trackSelector = createTrackSelector()

        player = ExoPlayer.Builder(context, createRenderersFactory())
                .setMediaSourceFactory(createMediaSourceFactory())
                .setTrackSelector(trackSelector!!)
                .setReleaseTimeoutMs(5000)
                .build()
        player?.setAudioAttributes(createAudioAttributes(), true)
        player?.playWhenReady = true

        playerViewListenerMapper = createPlayerViewListenerMapper()
        playerViewListenerMapper?.let { player?.addListener(it) }

        analyticsListenerMapper = createAnalyticsListenerMapper()
        analyticsListenerMapper?.let { player?.addAnalyticsListener(it) }

        if (BuildConfig.DEBUG) player?.addAnalyticsListener(EventLogger())

        player?.addAnalyticsListener(DroppedFramesManager(trackSelector!!, playerConfig.maxQuality))
        onStartSeekToDateListener?.let { player?.addListener(it) }

        mediaItem = createMediaItem()
        mediaItem?.let { player?.addMediaItem(it) }

        val startPositionMs = startPosition.startPositionMs()
        startPositionMs?.let { player?.seekTo(it) }
        player?.prepare()

        hdmiDeviceCallback = HdmiDeviceCallback(playerViewListenerMapper)
        audioManager?.registerAudioDeviceCallback(hdmiDeviceCallback, null)
    }

    fun releasePlayer() {
        updateStartPosition()
        analyticsListenerMapper?.release()
        playerViewListenerMapper?.release()
        player?.release()
        player = null
        mediaItem = null
        trackSelector = null
        adsLoader?.setPlayer(null)
        hdmiDeviceCallback?.let { audioManager?.unregisterAudioDeviceCallback(it) }
    }

    fun releaseAdsLoader() {
        adsLoader?.setPlayer(null)
        adsLoader?.release()
        adsLoader = null
        adViewProvider.adViewGroup?.removeAllViews()
    }

    fun isReleased(): Boolean {
        return player == null
    }

    protected open fun createDataSourceFactory(): DefaultDataSource.Factory {
        val cronetEngine = CronetUtil.buildCronetEngine(context)
        val dataSourceFactory = if (cronetEngine != null) {
            CPCronetDataSourceFactory(playerConfig.userAgent, cronetEngine, Executors.newSingleThreadExecutor())
        } else {
            CPDefaultHttpDataSourceFactory(playerConfig.userAgent)
        }
        return DefaultDataSource.Factory(context, dataSourceFactory)
    }

    protected open fun createMediaItem(): MediaItem? {
        val adsConfiguration = playerConfig.adsUrl?.let { url ->
            MediaItem.AdsConfiguration.Builder(Uri.parse(url))
                    .build()
        }
        return MediaItem.Builder()
                .setUri(playerConfig.url)
                .setAdsConfiguration(adsConfiguration)
                .setSubtitleConfigurations(createSubtitleConfigurations())
                .build()
    }

    private fun createTrackSelector(): DefaultTrackSelector {
        val trackSelector = DefaultTrackSelector(context)
        val builder = DefaultTrackSelector.Parameters.Builder(context)
        builder.setPreferredAudioLanguage(playerSettingsSharedPrefs.audioLanguage)
        builder.setMaxVideoSize(Int.MAX_VALUE, playerConfig.maxQuality)
        trackSelector.setParameters(builder)
        return trackSelector
    }

    private fun createRenderersFactory(): RenderersFactory {
        return CPDefaultRenderersFactory(context)
                .setMediaCodecSelector(CPMediaCodecSelector())
    }

    private fun createMediaSourceFactory(): MediaSource.Factory {
        return DefaultMediaSourceFactory(dataSourceFactory)
                .setLocalAdInsertionComponents(this, adViewProvider)
                .setDrmSessionManagerProvider(createDrmSessionManagerProvider())
    }

    protected open fun createDrmSessionManagerProvider(): DrmSessionManagerProvider {
        return playerConfig.mediaDrmCallback?.let { mediaDrmCallback ->
            DrmSessionManagerProvider {
                DefaultDrmSessionManager.Builder()
                        .setUuidAndExoMediaDrmProvider(C.WIDEVINE_UUID, FrameworkMediaDrm.DEFAULT_PROVIDER)
                        .build(mediaDrmCallback)
            }
        } ?: DefaultDrmSessionManagerProvider()
    }

    private fun createPlayerViewListenerMapper(): PlayerViewListenerMapper? {
        return playerViewListener?.let { PlayerViewListenerMapper(it, playerConfig, this) }
    }

    private fun createAnalyticsListenerMapper(): AnalyticsListenerMapper? {
        val startPosition = startPosition.startPositionMs() ?: 0
        return if (playerConfig.playerAnalyticsListeners.isNotEmpty()) {
            AnalyticsListenerMapper(playerConfig.playerAnalyticsListeners, this, startPosition, duration, playerConfig.autoplay)
        } else {
            null
        }
    }

    protected fun createSubtitleConfigurations(): List<MediaItem.SubtitleConfiguration> {
        return playerConfig.subtitles.map {
            val selectionFlags = if (playerSettingsSharedPrefs.subtitleLanguage.equals(it.language, true)) {
                C.SELECTION_FLAG_DEFAULT
            } else {
                0
            }
            MediaItem.SubtitleConfiguration.Builder(Uri.parse(it.url))
                    .setMimeType(it.mimeType)
                    .setLanguage(it.language)
                    .setSelectionFlags(selectionFlags)
                    .build()
        }
    }

    private fun createAudioAttributes(): AudioAttributes {
        return AudioAttributes.Builder()
                .setUsage(C.USAGE_MEDIA)
                .setContentType(C.AUDIO_CONTENT_TYPE_MOVIE)
                .build()
    }

    private fun updateStartPosition() {
        player?.let {
            startPosition = MsStartPosition(Math.max(0, it.contentPosition))
        }
    }

    fun notifyOnPositionUpdate() {
        playerViewListenerMapper?.notifyOnPositionUpdate()
    }

    fun maybeNotifyOnSeeAlsoStarted() {
        playerViewListenerMapper?.maybeNotifyOnSeeAlsoStarted()
    }

    fun clearStartPosition() {
        startPosition = MsStartPosition(C.TIME_UNSET)
    }

    fun seekToDate(date: Date) {
        player?.seekTo(getPositionForDate(date))
    }

    fun getDefaultPosition(): Long? = player?.getDefaultPosition()

    fun getCurrentPosition() = player?.currentPosition ?: 0

    fun getPositionForDate(date: Date?) = player?.getPositionForDate(date) ?: 0

    fun getDuration() = player?.duration ?: 0

    fun getCurrentQuality() = player?.videoFormat?.height ?: 0

    fun isPlaying() = player?.isPlaying ?: false

    fun isPlayingAdvert() = player?.isPlayingAd ?: false

    fun getCurrentVolumeLevel() = player?.volume ?: 0f

    fun getFrameRate() = player?.videoFormat?.frameRate ?: 0f

    fun isDynamic() = player?.isCurrentMediaItemDynamic ?: false

    fun isPlaybackEnded() = player?.playbackState == Player.STATE_ENDED

    fun getActiveOverlays(): List<OverlayInfo> {
        return activeOverlays.filter { it.value }.keys.toList()
    }

    fun getAdsCuePoints(): List<Float> {
        val player = this.player ?: return listOf()
        player.currentTimeline.takeUnless { it.isEmpty }?.let {
            val period = Timeline.Period()
            it.getPeriod(player.currentPeriodIndex, period)
            val adGroupTimesSeconds = FloatArray(period.adGroupCount)
            for (i in adGroupTimesSeconds.indices) {
                val timeUs = period.getAdGroupTimeUs(i)
                if (timeUs < 0) {
                    adGroupTimesSeconds[i] = -1f
                } else {
                    adGroupTimesSeconds[i] = (timeUs / 1000 / 1000).toFloat()
                }
            }
            return adGroupTimesSeconds.asList()
        }
        return listOf()
    }


    // DefaultMediaSourceFactory.AdsLoaderProvider
    override fun getAdsLoader(adsConfiguration: MediaItem.AdsConfiguration): AdsLoader? {
        if (adsLoader == null) {
            val imaSdkSettings = ImaSdkFactory.getInstance().createImaSdkSettings()
            imaSdkSettings.language = LOCALE_PL
            imaSdkSettings.isDebugMode = BuildConfig.DEBUG
            imaBugFixManager = ImaBugFixManager(this)
            adsLoader = ImaAdsLoader.Builder(context)
                    .setDebugModeEnabled(BuildConfig.DEBUG)
                    .setImaSdkSettings(imaSdkSettings)
                    .setAdEventListener(this)
                    .setAdErrorListener(this)
                    .setFocusSkipButtonWhenAvailable(false)
                    .setVideoAdPlayerCallback(imaBugFixManager!!)
                    .build()
        }
        adsLoader?.setPlayer(player)
        return adsLoader
    }

    // AdEvent.AdEventListener
    override fun onAdEvent(adEvent: AdEvent?) {
        playerViewListenerMapper?.onAdEvent(adEvent)
        analyticsListenerMapper?.onAdEvent(adEvent)
        imaBugFixManager?.onAdEvent(adEvent)
    }


    // AdErrorEvent.AdErrorListener
    override fun onAdError(adError: AdErrorEvent?) {
        analyticsListenerMapper?.onAdError(adError)
    }

    fun setOverlayVisibility(overlayType: OverlayType, visible: Boolean) {
        activeOverlays[OverlayInfo(overlayType)] = visible
        analyticsListenerMapper?.onOverlayStateChangedEvent()
    }

    fun onOverlayError(throwable: Throwable) {
        analyticsListenerMapper?.onOverlayError(throwable)
    }
}