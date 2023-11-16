package pl.cyfrowypolsat.cpplayer



import android.content.Context
import io.reactivex.rxjava3.core.Observable
import pl.cyfrowypolsat.cpdata.api.common.enums.Cpid
import pl.cyfrowypolsat.cpdata.api.common.model.DeviceId
import pl.cyfrowypolsat.cpdata.api.common.model.ImageSourceResult
import pl.cyfrowypolsat.cpdata.api.drm.DrmService
import pl.cyfrowypolsat.cpdata.api.drm.request.GetPseudoLicenseParams
import pl.cyfrowypolsat.cpdata.api.drm.response.GetPseudoLicenseResult
import pl.cyfrowypolsat.cpdata.api.navigation.NavigationService
import pl.cyfrowypolsat.cpdata.api.navigation.request.media.GetMediaRelatedContentParams
import pl.cyfrowypolsat.cpdata.api.navigation.request.preplaydata.PrePlayDataParams
import pl.cyfrowypolsat.cpdata.api.navigation.response.media.GetMediaRelatedContentResult
import pl.cyfrowypolsat.cpdata.api.navigation.response.preplaydata.MediaItem
import pl.cyfrowypolsat.cpdata.api.navigation.response.preplaydata.MediaSource
import pl.cyfrowypolsat.cpdata.api.navigation.response.preplaydata.MediaSource.Companion.DASH_ACCESS_METHOD
import pl.cyfrowypolsat.cpdata.api.navigation.response.preplaydata.MediaSource.Companion.DIRECT_ACCESS_METHOD
import pl.cyfrowypolsat.cpdata.api.navigation.response.preplaydata.MediaSource.Companion.HLS_ACCESS_METHOD
import pl.cyfrowypolsat.cpdata.api.navigation.response.preplaydata.MediaSource.Companion.HLS_TIMESHIFT_ACCESS_METHOD
import pl.cyfrowypolsat.cpdata.api.navigation.response.preplaydata.PlaybackQueue
import pl.cyfrowypolsat.cpdata.api.navigation.response.preplaydata.PrePlayDataResult
import pl.cyfrowypolsat.cpdata.api.system.SystemService
import pl.cyfrowypolsat.cpdata.common.manager.AppDataManager
import pl.cyfrowypolsat.cpdata.common.utils.DeviceIdGenerator
import pl.cyfrowypolsat.cpdata.repository.ConfigurationRepository
import pl.cyfrowypolsat.cpplayer.concurrentaccess.ConcurrentAccessListener
import pl.cyfrowypolsat.cpplayer.model.MediaSuccessor
import pl.cyfrowypolsat.cpplayer.model.PlayerData
import pl.cyfrowypolsat.cpplayer.model.PlayerImageSource
import pl.cyfrowypolsat.cpplayercore.configuration.PlayerStartPosition
import pl.cyfrowypolsat.cpstats.core.ApplicationDataProvider
import pl.cyfrowypolsat.cpstats.session.SessionExpiredManager
import timber.log.Timber

class GetPlayerDataUseCase(private val navigationService: NavigationService,
                           private val drmService: DrmService,
                           private val systemService: SystemService,
                           private val playerConfigMapper: PlayerConfigMapper,
                           private val deviceIdGenerator: DeviceIdGenerator,
                           private val context: Context,
                           private val applicationDataProvider: ApplicationDataProvider,
                           private val buildAdsUrlUseCase: BuildAdsUrlUseCase,
                           private val sessionExpiredManager: SessionExpiredManager,
                           private val configurationRepository: ConfigurationRepository,
                           private val appDataManager: AppDataManager) {

    fun getPlayerData(mediaId: String,
                      cpid: Int,
                      concurrentAccessListener: ConcurrentAccessListener?,
                      offline: Boolean = false,
                      startPosition: PlayerStartPosition? = null,
                      autoplay: Boolean = false,
                      lockPlayerUI: Boolean = false,
                      autoplayNextEpisodeTimerEnabled: Boolean = false,
                      autostartTeravoltPlayerOverlay: Boolean = false): Observable<PlayerData> {
        val prePlayDataParams = PrePlayDataParams(mediaId, cpid)
        return navigationService.prePlayData(prePlayDataParams)
                .flatMap { prePlayData ->
                    val mediaItem = prePlayData.mediaItem
                    val deviceId = deviceIdGenerator.generateDeviceId()
                    val mediaSource = findBestMediaSource(mediaItem.playback.mediaSources)
                    if (mediaSource != null) {
                        if (mediaSource.url == null) {
                            getMediaUrl(mediaSource = mediaSource,
                                    mediaItem = mediaItem,
                                    prePlayData = prePlayData,
                                    deviceId = deviceId,
                                    offline = offline,
                                    concurrentAccessListener = concurrentAccessListener,
                                    startPosition = startPosition,
                                    lockPlayerUI = lockPlayerUI,
                                    autoplay = autoplay,
                                    autoplayNextEpisodeTimerEnabled = autoplayNextEpisodeTimerEnabled,
                                    autostartTeravoltPlayerOverlay = autostartTeravoltPlayerOverlay)
                        } else {
                            getConfiguration(mediaSource = mediaSource,
                                    mediaUrl = mediaSource.url!!,
                                    prePlayData = prePlayData,
                                    deviceId = deviceId,
                                    offline = offline,
                                    pseudoLicenseResult = null,
                                    concurrentAccessListener = concurrentAccessListener,
                                    startPosition = startPosition,
                                    lockPlayerUI = lockPlayerUI,
                                    autoplay = autoplay,
                                    autoplayNextEpisodeTimerEnabled = autoplayNextEpisodeTimerEnabled,
                                    autostartTeravoltPlayerOverlay = autostartTeravoltPlayerOverlay)
                        }
                    } else {
                        throw NoSupportedSourcesException(mediaId, cpid)
                    }
                }
    }

    private fun getMediaUrl(mediaSource: MediaSource,
                            mediaItem: MediaItem,
                            prePlayData: PrePlayDataResult,
                            deviceId: DeviceId,
                            offline: Boolean,
                            startPosition: PlayerStartPosition?,
                            concurrentAccessListener: ConcurrentAccessListener?,
                            lockPlayerUI: Boolean,
                            autoplay: Boolean,
                            autoplayNextEpisodeTimerEnabled: Boolean,
                            autostartTeravoltPlayerOverlay: Boolean): Observable<PlayerData> {
        val pseudoUrl = mediaSource.authorizationServices?.pseudo?.getPseudoLicenseUrl
                ?: throw NoPseudoLicenseServiceException(mediaItem.id, mediaItem.cpid)

        val params = GetPseudoLicenseParams(cpid = mediaItem.cpid,
                mediaId = mediaItem.id,
                deviceId = deviceId,
                sourceId = mediaSource.id,
                offline = offline)
        return drmService.getPseudoLicense(pseudoUrl, params)
                .flatMap { result ->
                    getConfiguration(mediaSource = mediaSource,
                            mediaUrl = result.url,
                            prePlayData = prePlayData,
                            deviceId = deviceId,
                            offline = offline,
                            pseudoLicenseResult = result,
                            startPosition = startPosition,
                            concurrentAccessListener = concurrentAccessListener,
                            lockPlayerUI = lockPlayerUI,
                            autoplay = autoplay,
                            autoplayNextEpisodeTimerEnabled = autoplayNextEpisodeTimerEnabled,
                            autostartTeravoltPlayerOverlay = autostartTeravoltPlayerOverlay)
                }
    }

    private fun findBestMediaSource(sources: List<MediaSource>): MediaSource? {
        val dashSources = sources.filter { it.accessMethod == DASH_ACCESS_METHOD }
        if (dashSources.isNotEmpty()) {
            return dashSources.last()
        }

        val directSources = sources.filter { it.accessMethod == DIRECT_ACCESS_METHOD }
        if (directSources.isNotEmpty()) {
            return directSources.last()
        }

        val hlsSources = sources.filter { it.accessMethod == HLS_ACCESS_METHOD || it.accessMethod == HLS_TIMESHIFT_ACCESS_METHOD }
        if (hlsSources.isNotEmpty()) {
            return hlsSources.last()
        }

        return sources.lastOrNull()
    }

    private fun getMediaSuccessor(playbackQueue: PlaybackQueue?): MediaSuccessor? {
        val successor = playbackQueue?.successors?.firstOrNull() ?: return null
        return MediaSuccessor(successor.id, successor.cpid)
    }


    private fun getPlayerThumbnailList(images: List<ImageSourceResult>?): List<PlayerImageSource> {
        return images?.map { PlayerImageSource(it.size.width, it.size.height, it.src) } ?: listOf()
    }

    private fun getConfiguration(mediaSource: MediaSource,
                                 mediaUrl: String,
                                 prePlayData: PrePlayDataResult,
                                 deviceId: DeviceId,
                                 offline: Boolean,
                                 pseudoLicenseResult: GetPseudoLicenseResult?,
                                 startPosition: PlayerStartPosition?,
                                 concurrentAccessListener: ConcurrentAccessListener?,
                                 lockPlayerUI: Boolean,
                                 autoplay: Boolean,
                                 autoplayNextEpisodeTimerEnabled: Boolean,
                                 autostartTeravoltPlayerOverlay: Boolean): Observable<PlayerData> {
        return configurationRepository.getConfiguration()
                .flatMap { configuration ->
                    getRelatedMedia(mediaSource = mediaSource,
                            mediaUrl = mediaUrl,
                            prePlayData = prePlayData,
                            deviceId = deviceId,
                            offline = offline,
                            pseudoLicenseResult = pseudoLicenseResult,
                            startPosition = startPosition,
                            concurrentAccessListener = concurrentAccessListener,
                            lockPlayerUI = lockPlayerUI,
                            autoplay = autoplay,
                            autoplayNextEpisodeTimerEnabled = autoplayNextEpisodeTimerEnabled,
                            autostartTeravoltPlayerOverlay = autostartTeravoltPlayerOverlay)
                }
    }

    private fun getRelatedMedia(mediaSource: MediaSource,
                                mediaUrl: String,
                                prePlayData: PrePlayDataResult,
                                deviceId: DeviceId,
                                offline: Boolean,
                                pseudoLicenseResult: GetPseudoLicenseResult?,
                                startPosition: PlayerStartPosition?,
                                concurrentAccessListener: ConcurrentAccessListener?,
                                lockPlayerUI: Boolean,
                                autoplay: Boolean,
                                autoplayNextEpisodeTimerEnabled: Boolean,
                                autostartTeravoltPlayerOverlay: Boolean): Observable<PlayerData> {
        val cpid = Cpid.getFromInt(prePlayData.mediaItem.cpid)
        return if (cpid == Cpid.VOD) {
            val params = GetMediaRelatedContentParams(prePlayData.mediaItem.id, prePlayData.mediaItem.cpid, 0, 20)
            navigationService.getMediaRelatedContent(params)
                    .onErrorReturn { GetMediaRelatedContentResult(0, 0, listOf()) }
                    .flatMap { getMediaRelatedContentResult ->
                        mapToPlayerData(mediaSource = mediaSource,
                                mediaUrl = mediaUrl,
                                prePlayData = prePlayData,
                                deviceId = deviceId,
                                offline = offline,
                                pseudoLicenseResult = pseudoLicenseResult,
                                mediaRelatedContentResult = getMediaRelatedContentResult,
                                startPosition = startPosition,
                                concurrentAccessListener = concurrentAccessListener,
                                lockPlayerUI = lockPlayerUI,
                                autoplay = autoplay,
                                autoplayNextEpisodeTimerEnabled = autoplayNextEpisodeTimerEnabled,
                                autostartTeravoltPlayerOverlay = autostartTeravoltPlayerOverlay)
                    }
        } else {
            mapToPlayerData(mediaSource = mediaSource,
                    mediaUrl = mediaUrl,
                    prePlayData = prePlayData,
                    deviceId = deviceId,
                    offline = offline,
                    pseudoLicenseResult = pseudoLicenseResult,
                    mediaRelatedContentResult = null,
                    startPosition = startPosition,
                    concurrentAccessListener = concurrentAccessListener,
                    lockPlayerUI = lockPlayerUI,
                    autoplay = autoplay,
                    autoplayNextEpisodeTimerEnabled = autoplayNextEpisodeTimerEnabled,
                    autostartTeravoltPlayerOverlay = autostartTeravoltPlayerOverlay)
        }
    }

    private fun mapToPlayerData(mediaSource: MediaSource,
                                mediaUrl: String,
                                prePlayData: PrePlayDataResult,
                                deviceId: DeviceId,
                                offline: Boolean,
                                pseudoLicenseResult: GetPseudoLicenseResult?,
                                startPosition: PlayerStartPosition?,
                                concurrentAccessListener: ConcurrentAccessListener?,
                                mediaRelatedContentResult: GetMediaRelatedContentResult?,
                                lockPlayerUI: Boolean,
                                autoplay: Boolean,
                                autoplayNextEpisodeTimerEnabled: Boolean,
                                autostartTeravoltPlayerOverlay: Boolean): Observable<PlayerData> {
        val adsUrl = buildAdsUrlUseCase.buildAdsUrl(ads = prePlayData.ads,
                seenPercent = prePlayData.watchedContentData?.seenPercent?.toInt())
        Timber.tag("AdsUrl").d(adsUrl)
        val playerConfig = playerConfigMapper.map(prePlayData = prePlayData,
                mediaSource = mediaSource,
                mediaUrl = mediaUrl,
                deviceId = deviceId,
                adsUrl = adsUrl,
                drmService = drmService,
                systemService = systemService,
                navigationService = navigationService,
                configurationRepository = configurationRepository,
                applicationDataProvider = applicationDataProvider,
                context = context,
                sessionExpiredManager = sessionExpiredManager,
                offline = offline,
                appDataManager = appDataManager,
                pseudoLicenseResult = pseudoLicenseResult,
                startPosition = startPosition,
                concurrentAccessListener = concurrentAccessListener,
                mediaRelatedContentResult = mediaRelatedContentResult,
                lockPlayerUI = lockPlayerUI,
                autoplay = autoplay,
                autoplayNextEpisodeTimerEnabled = autoplayNextEpisodeTimerEnabled,
                autostartTeravoltPlayerOverlay = autostartTeravoltPlayerOverlay)

        return Observable.just(PlayerData(playerConfig = playerConfig,
                successor = getMediaSuccessor(prePlayData.playbackQueue),
                images = getPlayerThumbnailList(prePlayData.mediaItem.displayInfo.thumbnails)))
    }

}
