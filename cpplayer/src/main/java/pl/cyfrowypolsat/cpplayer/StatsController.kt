package pl.cyfrowypolsat.cpplayer

import android.content.Context
import android.net.Uri
import pl.cyfrowypolsat.cpdata.api.common.jsonrpc.JsonRPCException
import pl.cyfrowypolsat.cpdata.api.navigation.response.preplaydata.*
import pl.cyfrowypolsat.cpdata.api.system.SystemService
import pl.cyfrowypolsat.cpplayer.drm.LicenseInfo
import pl.cyfrowypolsat.cpplayer.drm.LicenseListener
import pl.cyfrowypolsat.cpplayercore.core.AdvertPlayerException
import pl.cyfrowypolsat.cpplayercore.core.PlayerException
import pl.cyfrowypolsat.cpplayercore.core.teravolt.TeravoltException
import pl.cyfrowypolsat.cpplayercore.events.stats.AdvertPlaybackData
import pl.cyfrowypolsat.cpplayercore.events.stats.AnalyticsListenerMapper
import pl.cyfrowypolsat.cpplayercore.events.stats.PlaybackData
import pl.cyfrowypolsat.cpplayercore.events.stats.PlayerAnalyticsListener
import pl.cyfrowypolsat.cpstats.common.appsflyer.AppsFlyerConfig
import pl.cyfrowypolsat.cpstats.core.ApplicationDataProvider
import pl.cyfrowypolsat.cpstats.core.TraceIdGenerator
import pl.cyfrowypolsat.cpstats.core.model.*
import pl.cyfrowypolsat.cpstats.core.model.MediaId
import pl.cyfrowypolsat.cpstats.player.PlayerEventController
import pl.cyfrowypolsat.cpstats.player.PlayerEventFactory
import pl.cyfrowypolsat.cpstats.player.PlayerEventHandler
import pl.cyfrowypolsat.cpstats.player.appsflyer.AppsFlyerPlayerHandler
import pl.cyfrowypolsat.cpstats.player.cpplayerevents.CPPlayerEventsConfig
import pl.cyfrowypolsat.cpstats.player.cpplayerevents.CPPlayerEventsUnauthorizedCallback
import pl.cyfrowypolsat.cpstats.player.cpplayerevents.PlayerEventsHandler
import pl.cyfrowypolsat.cpstats.player.debug.DebugHandler
import pl.cyfrowypolsat.cpstats.player.firebase.FirebasePlayerHandler
import pl.cyfrowypolsat.cpstats.player.gemiusaudiencestream.GemiusAudienceStreamConfig
import pl.cyfrowypolsat.cpstats.player.gemiusaudiencestream.GemiusAudienceStreamHandler
import pl.cyfrowypolsat.cpstats.player.gemiusprism.GemiusPrismConfig
import pl.cyfrowypolsat.cpstats.player.gemiusprism.GemiusPrismHandler
import pl.cyfrowypolsat.cpstats.session.SessionExpiredManager
import retrofit2.HttpException
import timber.log.Timber

class StatsController(private val context: Context,
                      private val systemService: SystemService,
                      private val applicationDataProvider: ApplicationDataProvider,
                      private val playerPrePlayData: PrePlayDataResult,
                      private val selectedMediaSource: MediaSource,
                      private val advertsRequestUrl: String? = null,
                      private var licenseId: String?,
                      private var sellModel: String?) : PlayerAnalyticsListener, CPPlayerEventsUnauthorizedCallback, LicenseListener, SessionExpiredManager.SessionExpiredListener {

    private val playerEventController: PlayerEventController
    private var playbackTraceId: String = ""

    private var playerEventsHandler: PlayerEventsHandler? = null

    private var playbackDurationMs: Long = 0
    private var droppedFramesCount: Long = 0
    private var lastBitrateEstimate: Long = 0

    private var videoBytesLoaded: Long = 0
    private var audioBytesLoaded: Long = 0

    private var lastPlaybackData: PlaybackData? = null
    private var playerCurrentPositionMs: Long = 0
    private var playerDurationMs: Long = 0

    init {
        playerEventController = PlayerEventController(getPlayerEventHandlerList())
        startNewPlaybackSession()
    }

    override fun onSessionExpired() {
        Timber.d("onPlaybackSessionExpired")
        startNewPlaybackSession()
    }

    override fun onUnauthorizedException() {
        try {
            Timber.d("onUnauthorizedException")
            playerPrePlayData.reporting?.playerEvents?.authTokenService?.firstVersion?.url?.let { url ->
                systemService.getPlayerEventsAuthToken(url).subscribe({
                    playerEventsHandler?.updateAuthToken(it)
                }) { t: Throwable -> Timber.e(t) }
            }
        } catch (t: Throwable) {
            t.printStackTrace()
        }

    }

    override fun onLicenseRequestStarted() {
        lastPlaybackData?.let {
            playerEventController.handleEvent(PlayerEventFactory.licenseRequestStartedEvent(buildPlayerData(it)))
        }
    }

    override fun onLicenseRequestCompleted(licenseInfo: LicenseInfo) {
        licenseId = licenseInfo.licenseId
        sellModel = licenseInfo.sellModel
        lastPlaybackData?.let {
            playerEventController.handleEvent(PlayerEventFactory.licenseRequestCompletedEvent(buildPlayerData(it)))
        }
    }

    override fun onLicenseRequestError(throwable: Throwable) {
        lastPlaybackData?.let {
            playerEventController.handleEvent(PlayerEventFactory.licenseRequestError(buildErrorData(throwable), buildPlayerData(it)))
        }
    }

    override fun onPlayerInitialized(playbackData: PlaybackData) {
        playerEventController.handleEvent(PlayerEventFactory.playerInitializedEvent(buildPlayerData(playbackData)))
    }

    override fun onPlayerClosed(playbackData: PlaybackData) {
        playerEventController.handleEvent(PlayerEventFactory.playerClosedEvent(buildPlayerData(playbackData)))
    }

    override fun onOverlayStateChanged(playbackData: PlaybackData) {
        playerEventController.handleEvent(PlayerEventFactory.overlayStateChangedEvent(buildPlayerData(playbackData)))
    }

    override fun onBandwidthEstimate(playbackData: PlaybackData,
                                     bitrateEstimate: Long) {
        lastBitrateEstimate = bitrateEstimate
    }

    override fun onPlaybackStarted(playbackData: PlaybackData,
                                   autoplay: Boolean) {
        playerEventController.handleEvent(PlayerEventFactory.playbackStartedEvent(buildPlayerData(playbackData), autoplay))
    }

    override fun onPlaybackFinished(playbackData: PlaybackData) {
        playerEventController.handleEvent(PlayerEventFactory.playbackFinishedEvent(buildPlayerData(playbackData)))
    }

    override fun onBufferingStarted(playbackData: PlaybackData) {
        playerEventController.handleEvent(PlayerEventFactory.bufferingStartedEvent(buildPlayerData(playbackData)))
    }

    override fun onBufferingEnded(playbackData: PlaybackData) {
        playerEventController.handleEvent(PlayerEventFactory.bufferingFinishedEvent(buildPlayerData(playbackData)))
    }

    override fun onIsPlayingChanged(playbackData: PlaybackData,
                                    autoplay: Boolean) {
        if (playbackData.isPlaying) {
            playerEventController.handleEvent(PlayerEventFactory.playbackUnPausedEvent(buildPlayerData(playbackData), autoplay))
        } else {
            playerEventController.handleEvent(PlayerEventFactory.playbackPausedEvent(buildPlayerData(playbackData)))
        }
    }

    override fun onQualityChanged(playbackData: PlaybackData) {
        playerEventController.handleEvent(PlayerEventFactory.qualityChangedEvent(buildPlayerData(playbackData)))
    }

    override fun onVolumeChanged(playbackData: PlaybackData) {
        playerEventController.handleEvent(PlayerEventFactory.volumeChangedEvent(buildPlayerData(playbackData)))
    }

    override fun onSeekProcessed(playbackData: PlaybackData) {
        playerEventController.handleEvent(PlayerEventFactory.seekProcessedEvent(buildPlayerData(playbackData)))
    }

    override fun onDrmSessionAcquired(playbackData: PlaybackData) {
        playerEventController.handleEvent(PlayerEventFactory.drmSessionInitializedEvent(buildPlayerData(playbackData)))
    }

    override fun onDrmKeysLoaded(playbackData: PlaybackData) {
        playerEventController.handleEvent(PlayerEventFactory.drmSessionStartedEvent(buildPlayerData(playbackData)))
    }

    override fun onDrmSessionManagerError(playbackData: PlaybackData,
                                          throwable: Throwable) {
        val errorData = buildErrorData(throwable)
        playerEventController.handleEvent(PlayerEventFactory.drmSessionErrorEvent(errorData, buildPlayerData(playbackData)))
    }

    override fun onBehindLiveWindowError(playbackData: PlaybackData,
                                         throwable: Throwable) {
        val errorData = buildErrorData(throwable)
        playerEventController.handleEvent(PlayerEventFactory.behindLiveWindowErrorEvent(errorData, buildPlayerData(playbackData)))
    }

    override fun onPlayerError(playbackData: PlaybackData,
                               throwable: Throwable) {
        val errorData = buildErrorData(throwable)
        playerEventController.handleEvent(PlayerEventFactory.interruptEvent(errorData, buildPlayerData(playbackData)))
    }

    override fun onUpdatePosition(playbackData: PlaybackData) {
        playbackDurationMs += AnalyticsListenerMapper.UPDATE_POSITION_INTERVAL_IN_MS
        playerEventController.handleEvent(PlayerEventFactory.playbackPositionUpdatedEvent(buildPlayerData(playbackData)))
    }

    override fun onVideoFramesDropped(playbackData: PlaybackData,
                                      droppedFrames: Long) {
        droppedFramesCount += droppedFrames
    }

    override fun onAudioChunkLoaded(playbackData: PlaybackData,
                                    loadedBytes: Long,
                                    uri: Uri) {
        audioBytesLoaded += loadedBytes
    }

    override fun onContentPauseRequested(playbackData: PlaybackData,
                                         advertPlaybackData: AdvertPlaybackData) {
        playerEventController.handleEvent(PlayerEventFactory.contentPauseRequestEvent(buildAdvertBlockData(advertPlaybackData), buildPlayerData(playbackData)))
    }

    override fun onContentResumeRequested(playbackData: PlaybackData,
                                          advertPlaybackData: AdvertPlaybackData) {
        playerEventController.handleEvent(PlayerEventFactory.contentResumeRequestedEvent(buildAdvertBlockData(advertPlaybackData), buildPlayerData(playbackData)))
    }

    override fun onAdvertBlockStarted(playbackData: PlaybackData,
                                      advertPlaybackData: AdvertPlaybackData) {
        playerEventController.handleEvent(PlayerEventFactory.advertBlockStartedEvent(buildAdvertData(advertPlaybackData), buildAdvertBlockData(advertPlaybackData), buildPlayerData(playbackData)))
    }

    override fun onAdvertBlockFinished(playbackData: PlaybackData,
                                       advertPlaybackData: AdvertPlaybackData) {
        playerEventController.handleEvent(PlayerEventFactory.advertBlockFinishedEvent(buildAdvertData(advertPlaybackData), buildAdvertBlockData(advertPlaybackData), buildPlayerData(playbackData)))
    }

    override fun onAdvertInitialized(playbackData: PlaybackData,
                                     advertPlaybackData: AdvertPlaybackData) {
        playerEventController.handleEvent(PlayerEventFactory.advertInitializedEvent(buildAdvertData(advertPlaybackData), buildAdvertBlockData(advertPlaybackData), buildPlayerData(playbackData)))
    }

    override fun onAdvertStarted(playbackData: PlaybackData,
                                 advertPlaybackData: AdvertPlaybackData,
                                 autoplay: Boolean) {
        playerEventController.handleEvent(PlayerEventFactory.advertStartedEvent(buildAdvertData(advertPlaybackData), buildAdvertBlockData(advertPlaybackData), buildPlayerData(playbackData), autoplay))
    }

    override fun onAdvertFinished(playbackData: PlaybackData,
                                  advertPlaybackData: AdvertPlaybackData) {
        playerEventController.handleEvent(PlayerEventFactory.advertFinishedEvent(buildAdvertData(advertPlaybackData), buildAdvertBlockData(advertPlaybackData), buildPlayerData(playbackData)))
    }

    override fun onAdvertPaused(playbackData: PlaybackData,
                                advertPlaybackData: AdvertPlaybackData) {
        playerEventController.handleEvent(PlayerEventFactory.advertPausedEvent(buildAdvertData(advertPlaybackData), buildAdvertBlockData(advertPlaybackData), buildPlayerData(playbackData)))
    }

    override fun onAdvertUnPaused(playbackData: PlaybackData,
                                  advertPlaybackData: AdvertPlaybackData,
                                  autoplay: Boolean) {
        playerEventController.handleEvent(PlayerEventFactory.advertUnPausedEvent(buildAdvertData(advertPlaybackData), buildAdvertBlockData(advertPlaybackData), buildPlayerData(playbackData), autoplay))
    }

    override fun onAdvertFirstQuartile(playbackData: PlaybackData,
                                       advertPlaybackData: AdvertPlaybackData) {
        playerEventController.handleEvent(PlayerEventFactory.advertFirstQuartileEvent(buildAdvertData(advertPlaybackData), buildAdvertBlockData(advertPlaybackData), buildPlayerData(playbackData)))
    }

    override fun onAdvertMidPoint(playbackData: PlaybackData,
                                  advertPlaybackData: AdvertPlaybackData) {
        playerEventController.handleEvent(PlayerEventFactory.advertMidPointEvent(buildAdvertData(advertPlaybackData), buildAdvertBlockData(advertPlaybackData), buildPlayerData(playbackData)))
    }

    override fun onAdvertThirdQuartile(playbackData: PlaybackData,
                                       advertPlaybackData: AdvertPlaybackData) {
        playerEventController.handleEvent(PlayerEventFactory.advertThirdQuartileEvent(buildAdvertData(advertPlaybackData), buildAdvertBlockData(advertPlaybackData), buildPlayerData(playbackData)))
    }

    override fun onAdvertError(playbackData: PlaybackData,
                               throwable: Throwable) {
        val errorData = buildErrorData(throwable)
        playerEventController.handleEvent(PlayerEventFactory.advertErrorEvent(errorData, buildPlayerData(playbackData)))
    }

    override fun onVideoChunkLoaded(playbackData: PlaybackData,
                                    loadedBytes: Long,
                                    uri: Uri) {
        videoBytesLoaded += loadedBytes
    }

    private fun startNewPlaybackSession() {
        playbackTraceId = TraceIdGenerator.generate()
        Timber.d("startNewPlaybackSession playbackTraceId: $playbackTraceId applicationTraceId: ${applicationDataProvider.applicationTraceId}")
    }

    private fun buildPlayerData(playbackData: PlaybackData): PlayerData {
        lastPlaybackData = playbackData

        if (!playbackData.isPlayingAdvert) {
            playerCurrentPositionMs = playbackData.currentPositionMs
            playerDurationMs = playbackData.durationMs
        }

        val mediaId = playerPrePlayData.mediaItem.let { MediaId(it.id, it.cpid, it.playback.mediaType) }
        val drmType: String? = if (selectedMediaSource.authorizationServices?.widevine?.getWidevineLicenseUrl != null) {
            "widevine"
        } else if (selectedMediaSource.authorizationServices?.pseudo?.getPseudoLicenseUrl != null) {
            "pseudo"
        } else {
            null
        }

        val sourceInfo = selectedMediaSource.let { SourceInfo(it.id, selectedMediaSource.accessMethod, selectedMediaSource.fileFormat, drmType) }
        val mediaTitle = playerPrePlayData.mediaItem.displayInfo.title

        val overlayData = playbackData.activeOverlays.map {
            OverlayData(type = OverlayType.getFromString(it.type.type),
                        autoStart = it.autostart)
        }

        return PlayerData(playbackTraceId = playbackTraceId,
                licenseId = licenseId,
                sellModel = sellModel,
                currentPositionMs = playerCurrentPositionMs,
                durationMs = playerDurationMs,
                currentQuality = playbackData.currentQuality,
                isPlaying = playbackData.isPlaying,
                isPlayingAdvert = playbackData.isPlayingAdvert,
                currentVolumeLevel = playbackData.currentVolumeLevel,
                streamingSpeed = lastBitrateEstimate,
                playbackDurationMs = playbackDurationMs,
                mediaId = mediaId,
                isLive = playbackData.dynamic,
                frameRate = playbackData.frameRate,
                sourceInfo = sourceInfo,
                droppedFramesCount = droppedFramesCount,
                audioBytesLoaded = audioBytesLoaded,
                videoBytesLoaded = videoBytesLoaded,
                advertsCuePoints = playbackData.advertsCuePoints,
                advertsRequestUrl = advertsRequestUrl,
                mediaTitle = mediaTitle,
                activeOverlays = overlayData
        )
    }

    private fun buildErrorData(throwable: Throwable): ErrorData {
        return when (throwable) {
            is PlayerException -> {
                ErrorData(throwable.code.toString(), throwable)
            }
            is JsonRPCException -> {
                val backendError = throwable.backendError
                val backendErrorData = BackendErrorData(backendError.type, backendError.serviceUrl, backendError.methodName)
                ErrorData(backendError.errorCode.toString(), throwable, backendErrorData)
            }
            is HttpException -> {
                ErrorData(throwable.code().toString(), throwable)
            }
            is AdvertPlayerException -> {
                ErrorData(throwable.code.toString(), throwable)
            }
            is TeravoltException -> {
                ErrorData("TERAVOLT_" + throwable.error.name, throwable)
            }
            else -> ErrorData(ErrorData.UNKNOWN_ERROR_CODE, throwable)
        }
    }

    private fun buildAdvertBlockData(advertPlaybackData: AdvertPlaybackData): AdvertBlockData {
        return AdvertBlockData(
                blockType = AdvertBlockType.fromIndex(advertPlaybackData.blockIndex),
                totalAdsCount = advertPlaybackData.blockTotalAdsCount,
                blockIndex = advertPlaybackData.blockIndex,
                timeOffsetSeconds = advertPlaybackData.blockTimeOffsetSeconds
        )
    }

    private fun buildAdvertData(advertPlaybackData: AdvertPlaybackData): AdvertData {
        return AdvertData(
                advertId = advertPlaybackData.advertId,
                advertDurationSeconds = advertPlaybackData.advertDurationSeconds,
                advertIndex = advertPlaybackData.advertIndex,
                advertTitle = advertPlaybackData.advertTitle
        )
    }

    private fun getPlayerEventHandlerList(): List<PlayerEventHandler> {
        val playerEventHandlerList: MutableList<PlayerEventHandler> = mutableListOf()
        val reporting = playerPrePlayData.reporting

        playerEventHandlerList.add(DebugHandler(applicationDataProvider))
        playerEventHandlerList.add(FirebasePlayerHandler(applicationDataProvider))

        reporting?.playerEvents?.let {
            playerEventsHandler = PlayerEventsHandler(getPlayerEventsConfig(it), applicationDataProvider)
            playerEventsHandler?.let { playerEventHandlerList.add(it) }
        }

        reporting?.gastream?.let {
            playerEventHandlerList.add(GemiusAudienceStreamHandler(context, getGemiusAudienceStreamConfig(it), applicationDataProvider))
        }

        reporting?.gprism?.let {
            playerEventHandlerList.add(GemiusPrismHandler(getGemiusPrismConfig(it), applicationDataProvider))
        }

        reporting?.appsFlyer?.let{
            playerEventHandlerList.add(AppsFlyerPlayerHandler(context, getAppsFlyerConfig(it)))
        }

        Timber.d(playerEventHandlerList.joinToString { it.javaClass.simpleName })
        return playerEventHandlerList
    }

    private fun getPlayerEventsConfig(config: PlayerEvents): CPPlayerEventsConfig {
        val service = CPPlayerEventsConfig.getServiceUrl(config.service, applicationDataProvider.isStaging)
        val serviceVersion = CPPlayerEventsConfig.getServiceVersion()
        val interval = CPPlayerEventsConfig.getIntervalInMilliseconds(config.interval, applicationDataProvider.isStaging)

        return CPPlayerEventsConfig(
                service = service,
                serviceVersion = serviceVersion,
                userAgent = applicationDataProvider.applicationUserAgent(),
                intervalMs = interval,
                originator = config.originator,
                authToken = config.authToken,
                unauthorizedCallback = this
        )
    }

    private fun getGemiusAudienceStreamConfig(config: GemiusAudienceStream): GemiusAudienceStreamConfig {
        val account = config.accounts[0]
        val duration = if (config.mediaId.cpid == 1) config.duration
                ?: 0 else -1    //for live we want duration -1
        val playerId = applicationDataProvider.gemiusPlayerId //todo change after https://jira.polsatc/browse/GM-4158

        return GemiusAudienceStreamConfig(
                playerId = playerId,
                serverHost = config.service,
                accountId = account,
                title = config.title,
                duration = duration
        )
    }

    private fun getGemiusPrismConfig(config: GemiusPrism): GemiusPrismConfig {
        val interval = GemiusPrismConfig.getIntervalInMilliseconds(applicationDataProvider.isStaging)

        return GemiusPrismConfig(
                userAgent = applicationDataProvider.applicationUserAgent(),
                service = config.service,
                account = config.accounts[0],
                viewCategory = config.categoryPath,
                mediaCategory = config.categoryPath,
                distributor = config.distributor,
                title = config.title,
                mediaType = config.mediaType,
                duration = config.duration?.toLong() ?: 0L,
                intervalMs = interval,
                mediaSourceName = "GetMedia"
        )
    }

    private fun getAppsFlyerConfig(config: AppsFlyer): AppsFlyerConfig {
        return AppsFlyerConfig(afDevKey = config.devKey)
    }
}