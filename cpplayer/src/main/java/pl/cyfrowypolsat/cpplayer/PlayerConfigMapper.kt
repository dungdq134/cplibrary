package pl.cyfrowypolsat.cpplayer

import android.content.Context
import android.os.Build
import android.provider.Settings
import androidx.media3.common.MimeTypes
import pl.cyfrowypolsat.cpcommon.core.extensions.isLater
import pl.cyfrowypolsat.cpcommon.core.extensions.isLaterOrNull
import pl.cyfrowypolsat.cpcommon.core.utils.DeviceUtils
import pl.cyfrowypolsat.cpcommon.domain.mapper.MediaBadgeMapper
import pl.cyfrowypolsat.cpcommon.domain.model.image.ImageSource
import pl.cyfrowypolsat.cpdata.api.common.enums.Cpid
import pl.cyfrowypolsat.cpdata.api.common.model.DeviceId
import pl.cyfrowypolsat.cpdata.api.common.model.ImageSourceResult
import pl.cyfrowypolsat.cpdata.api.common.model.cpidobject.CategoryResult
import pl.cyfrowypolsat.cpdata.api.common.model.cpidobject.MediaListItemResult
import pl.cyfrowypolsat.cpdata.api.concurrentaccess.request.DeviceInfo
import pl.cyfrowypolsat.cpdata.api.concurrentaccess.request.PlaybackMediaId
import pl.cyfrowypolsat.cpdata.api.drm.DrmService
import pl.cyfrowypolsat.cpdata.api.drm.request.GetWidevineLicenseParams
import pl.cyfrowypolsat.cpdata.api.drm.response.GetPseudoLicenseResult
import pl.cyfrowypolsat.cpdata.api.navigation.NavigationService
import pl.cyfrowypolsat.cpdata.api.navigation.response.media.GetMediaRelatedContentResult
import pl.cyfrowypolsat.cpdata.api.navigation.response.preplaydata.MediaSource
import pl.cyfrowypolsat.cpdata.api.navigation.response.preplaydata.PlayerOverlays
import pl.cyfrowypolsat.cpdata.api.navigation.response.preplaydata.PrePlayDataResult
import pl.cyfrowypolsat.cpdata.api.system.SystemService
import pl.cyfrowypolsat.cpdata.common.manager.AppDataManager
import pl.cyfrowypolsat.cpdata.repository.ConfigurationRepository
import pl.cyfrowypolsat.cpplayer.concurrentaccess.ConcurrentAccessController
import pl.cyfrowypolsat.cpplayer.concurrentaccess.ConcurrentAccessListener
import pl.cyfrowypolsat.cpplayer.drm.LicenseListener
import pl.cyfrowypolsat.cpplayer.drm.WidevineDrmCallback
import pl.cyfrowypolsat.cpplayer.model.PlayerQuality
import pl.cyfrowypolsat.cpplayer.startover.GetChannelStartOverDateUseCase
import pl.cyfrowypolsat.cpplayercore.configuration.*
import pl.cyfrowypolsat.cpplayercore.configuration.PlayerConfig.Companion.DEFAULT_START_POSITION
import pl.cyfrowypolsat.cpplayercore.core.seealso.ListElementTitlePosition
import pl.cyfrowypolsat.cpplayercore.core.seealso.SeeAlsoItem
import pl.cyfrowypolsat.cpplayercore.core.seealso.SeeAlsoType
import pl.cyfrowypolsat.cpplayercore.core.startover.StartOverProvider
import pl.cyfrowypolsat.cpstats.core.ApplicationDataProvider
import pl.cyfrowypolsat.cpstats.session.SessionExpiredManager

class PlayerConfigMapper {

    companion object {
        private const val DEFAULT_TAIL_DURATION_IN_SECONDS = 60
        private const val VTT_SUBTITLE_FORMAT = "vtt"
        private const val SRT_SUBTITLE_FORMAT = "srt"
        private const val MEDIA_TYPE_MOVIE = "movie"
    }

    fun map(prePlayData: PrePlayDataResult,
            mediaSource: MediaSource,
            mediaUrl: String,
            deviceId: DeviceId,
            adsUrl: String?,
            drmService: DrmService,
            applicationDataProvider: ApplicationDataProvider,
            systemService: SystemService,
            navigationService: NavigationService,
            configurationRepository: ConfigurationRepository,
            sessionExpiredManager: SessionExpiredManager,
            context: Context,
            offline: Boolean,
            lockPlayerUI: Boolean,
            autoplay: Boolean,
            appDataManager: AppDataManager,
            pseudoLicenseResult: GetPseudoLicenseResult?,
            startPosition: PlayerStartPosition?,
            concurrentAccessListener: ConcurrentAccessListener?,
            mediaRelatedContentResult: GetMediaRelatedContentResult?,
            autoplayNextEpisodeTimerEnabled: Boolean,
            autostartTeravoltPlayerOverlay: Boolean): PlayerConfig {

        val statsController = StatsController(context = context,
                systemService = systemService,
                applicationDataProvider = applicationDataProvider,
                playerPrePlayData = prePlayData,
                selectedMediaSource = mediaSource,
                advertsRequestUrl = adsUrl,
                licenseId = pseudoLicenseResult?.id,
                sellModel = pseudoLicenseResult?.reporting?.redevents?.sellModel)
        sessionExpiredManager.addSessionExpiredListener(statsController)

        val concurrentAccessController = getConcurrentController(deviceId = deviceId,
                prePlayData = prePlayData,
                concurrentAccessListener = concurrentAccessListener,
                systemService = systemService,
                context = context)
        val drmCallback = getWidevineDrmCallback(prePlayData = prePlayData,
                mediaSource = mediaSource,
                deviceId = deviceId,
                drmService = drmService,
                licenseListeners = listOfNotNull(statsController, concurrentAccessController),
                offline = offline)

        val mediaBadges = MediaBadgeMapper().map(prePlayData.mediaItem.displayInfo.underageClassification,
                prePlayData.mediaItem.displayInfo.accessibilityFeatures)

        return PlayerConfig(id = prePlayData.mediaItem.id,
                url = mediaUrl,
                mediaType = MediaType.getFromString(prePlayData.mediaItem.playback.mediaType),
                title = prePlayData.mediaItem.displayInfo.title,
                ageGroup = prePlayData.mediaItem.displayInfo.ageGroup,
                mediaBadges = mediaBadges,
                userAgent = prePlayData.userAgent,
                mediaDrmCallback = drmCallback,
                subtitles = getSubtitleInfoList(prePlayData),
                adsUrl = adsUrl,
                startPosition = startPosition ?: getStartPosition(prePlayData),
                durationMs = getDuration(prePlayData),
                introTimeline = getIntroTimeline(prePlayData),
                creditsTimeline = getCreditsTimeline(prePlayData),
                startOverProvider = getStartOverProvider(prePlayData, navigationService, configurationRepository),
                playerAnalyticsListeners = listOfNotNull(statsController, concurrentAccessController),
                maxQuality = PlayerQuality.from(appDataManager.playerQuality).maxHeight,
                lockPlayerUI = lockPlayerUI,
                autoplay = getAutoplay(autoplay, prePlayData),
                seeAlsoList = mapRelatedMedia(mediaRelatedContentResult),
                hasSuccessor = prePlayData.playbackQueue?.successors?.isNotEmpty() ?: false,
                autoplayNextEpisodeTimerEnabled = autoplayNextEpisodeTimerEnabled,
                overlays = mapOverlaysInfo(prePlayData.mediaItem.displayInfo.playerOverlays,
                    prePlayData.mediaItem.cpid == Cpid.LIVE.type, autostartTeravoltPlayerOverlay))
    }

    private fun getWidevineDrmCallback(prePlayData: PrePlayDataResult,
                                       mediaSource: MediaSource,
                                       deviceId: DeviceId,
                                       drmService: DrmService,
                                       licenseListeners: List<LicenseListener>,
                                       offline: Boolean): WidevineDrmCallback? {
        val url = mediaSource.authorizationServices?.widevine?.getWidevineLicenseUrl
        return if (url != null) {
            WidevineDrmCallback(
                    drmService = drmService,
                    userAgent = prePlayData.userAgent,
                    getWidevineLicenseUrl = url,
                    getWidevineLicenseParams = getWidevineLicenseParams(prePlayData, mediaSource, deviceId, offline),
                    licenseListeners = licenseListeners
            )
        } else {
            null
        }
    }


    private fun getWidevineLicenseParams(prePlayData: PrePlayDataResult,
                                         mediaSource: MediaSource,
                                         deviceId: DeviceId,
                                         offline: Boolean): GetWidevineLicenseParams {
        return GetWidevineLicenseParams(cpid = prePlayData.mediaItem.cpid,
                mediaId = prePlayData.mediaItem.id,
                sourceId = mediaSource.id,
                keyId = mediaSource.keyId!!,
                deviceId = deviceId,
                `object` = "",
                offline = offline)
    }

    private fun getSubtitleInfoList(prePlayData: PrePlayDataResult): List<SubtitleInfo> {
        val subtitles = prePlayData.mediaItem.displayInfo.subtitles ?: return listOf()
        val vttSubtitles = subtitles.filter { it.format == VTT_SUBTITLE_FORMAT }
        val srtSubtitles = subtitles.filter { it.format == SRT_SUBTITLE_FORMAT }
        return if (vttSubtitles.isNotEmpty()) {
            vttSubtitles.map {
                SubtitleInfo(url = it.src,
                        mimeType = MimeTypes.TEXT_VTT,
                        language = it.name)
            }
        } else {
            srtSubtitles.map {
                SubtitleInfo(url = it.src,
                        mimeType = MimeTypes.APPLICATION_SUBRIP,
                        language = it.name)
            }
        }
    }

    private fun getStartPosition(prePlayData: PrePlayDataResult): PlayerStartPosition {
        val lastDuration = prePlayData.watchedContentData?.lastDuration
        val duration = prePlayData.mediaItem.playback.duration

        return if (lastDuration != null && duration != null) {
            if (lastDuration > duration - DEFAULT_TAIL_DURATION_IN_SECONDS) {
                MsStartPosition(DEFAULT_START_POSITION)
            } else {
                MsStartPosition(lastDuration.toLong() * 1000)
            }
        } else {
            MsStartPosition(DEFAULT_START_POSITION)
        }
    }

    private fun getDuration(prePlayData: PrePlayDataResult): Long {
        val duration = prePlayData.mediaItem.playback.duration ?: 0
        return duration * 1000L
    }

    private fun getIntroTimeline(prePlayData: PrePlayDataResult): PlaybackTimeline? {
        val head = prePlayData.mediaItem.playback.timeline?.find { it.type == "head" }
                ?: return null
        return if (head.stop > head.start) {
            PlaybackTimeline(start = head.start,
                    stop = head.stop)
        } else {
            null
        }
    }

    private fun getCreditsTimeline(prePlayData: PrePlayDataResult): PlaybackTimeline? {
        val tail = prePlayData.mediaItem.playback.timeline?.find { it.type == "tail" }
                ?: return null
        val successor = prePlayData.playbackQueue?.successors?.firstOrNull()
        return if (successor != null) {
            PlaybackTimeline(start = tail.start, stop = tail.stop)
        } else {
            null
        }
    }

    private fun getStartOverProvider(prePlayData: PrePlayDataResult,
                                     navigationService: NavigationService,
                                     configurationRepository: ConfigurationRepository): StartOverProvider? {
        val mediaType = MediaType.getFromString(prePlayData.mediaItem.playback.mediaType)
        return if (mediaType == MediaType.CHANNEL) {
            val channelId = prePlayData.mediaItem.id
            GetChannelStartOverDateUseCase(navigationService, configurationRepository, channelId)
        } else {
            null
        }
    }

    private fun getConcurrentController(deviceId: DeviceId,
                                        prePlayData: PrePlayDataResult,
                                        concurrentAccessListener: ConcurrentAccessListener?,
                                        systemService: SystemService,
                                        context: Context): ConcurrentAccessController? {
        concurrentAccessListener ?: return null
        val deviceInfo = DeviceInfo(deviceId, getUserFriendlyDeviceName(context))
        return ConcurrentAccessController(systemService = systemService,
                deviceInfo = deviceInfo,
                mediaId = PlaybackMediaId(prePlayData.mediaItem.cpid, prePlayData.mediaItem.id),
                concurrentAccessListener = concurrentAccessListener)
    }

    private fun getUserFriendlyDeviceName(context: Context): String {
        val modelName =  try {
            Settings.Secure.getString(context.contentResolver, "bluetooth_name")
        } catch (e: Exception) {
            Build.BRAND.capitalize() + " " + Build.MODEL
        }
        return if (DeviceUtils.isAmazonFireTv(context)) {
            context.resources.getString(R.string.cac_device_name_amazon_fire_tv)
        } else if (DeviceUtils.isAndroidTv(context)) {
            context.resources.getString(R.string.cac_device_name_android_tv, modelName)
        } else {
            context.resources.getString(R.string.cac_device_name_android, modelName)
        }
    }

    private fun getAutoplay(autoplay: Boolean, prePlayData: PrePlayDataResult): Boolean {
        if (MediaType.getFromString(prePlayData.mediaItem.playback.mediaType) == MediaType.VOD) {
            val startPosition = getStartPosition(prePlayData) as? MsStartPosition?
            val startFromFollowMeContent = startPosition?.startPositionMs?.let { it != DEFAULT_START_POSITION && it != 0L }
                    ?: false
            return autoplay && !startFromFollowMeContent
        }
        return autoplay
    }

    private fun mapRelatedMedia(mediaRelatedContentResult: GetMediaRelatedContentResult?): List<SeeAlsoItem> {
        if (mediaRelatedContentResult == null) {
            return listOf()
        }

        val mediaRelatedContentItems = ArrayList<SeeAlsoItem>()

        for (result in mediaRelatedContentResult.results) {
            when (result) {
                is CategoryResult -> mediaRelatedContentItems.add(mapRelatedCategoryItem(result))
                is MediaListItemResult -> mapRelatedMediaItem(result)?.let { mediaRelatedContentItems.add(it) }
            }
        }

        return mediaRelatedContentItems
    }

    private fun mapRelatedCategoryItem(categoryResult: CategoryResult): SeeAlsoItem {
        return SeeAlsoItem(id = categoryResult.id,
                type = SeeAlsoType.CATEGORY,
                title = categoryResult.name,
                titlePosition = ListElementTitlePosition.ON,
                ageGroup = null,
                mediaBadges = null,
                posters = listOf(),
                thumbnails = mapImages(categoryResult.thumbnails))
    }

    private fun mapRelatedMediaItem(mediaItem: MediaListItemResult): SeeAlsoItem? {
        if(mediaItem.publicationDate.isLaterOrNull()) return null
        val type = mapSeeAlsoTypeForMedia(mediaItem.mediaType)
        return SeeAlsoItem(id = mediaItem.id,
                type = type,
                title = mediaItem.title,
                titlePosition = if (type == SeeAlsoType.MOVIE) {
                    ListElementTitlePosition.HIDDEN
                } else {
                    ListElementTitlePosition.ON
                },
                ageGroup = mediaItem.ageGroup,
                mediaBadges = MediaBadgeMapper().map(mediaItem.underageClassification, mediaItem.accessibilityFeatures),
                posters = mapImages(mediaItem.posters),
                thumbnails = mapImages(mediaItem.thumbnails))
    }

    private fun mapSeeAlsoTypeForMedia(mediaType: String): SeeAlsoType {
        return if (mediaType == "movie") {
            SeeAlsoType.MOVIE
        } else {
            SeeAlsoType.VOD
        }
    }

    private fun mapImages(values: List<ImageSourceResult>): List<ImageSource> {
        return values.map { mapImage(it) }
    }

    private fun mapImage(value: ImageSourceResult): ImageSource {
        return ImageSource(value.size.width, value.size.height, value.src)
    }

    private fun mapOverlaysInfo(playerOverlays: PlayerOverlays?,
                                isLiveMediaType: Boolean,
                                autostartTeravoltPlayerOverlay: Boolean): List<OverlayInfo> {
        val overlayInfoList = mutableListOf<OverlayInfo>()

        val teravoltOverlay = playerOverlays?.teravoltPlayerOverlay
        teravoltOverlay?.let {
            overlayInfoList.add(
                    OverlayInfo(
                            type = OverlayType.TERAVOLT,
                            enabled = teravoltOverlay.isEnabled,
                            autostart = (teravoltOverlay.autoStart || autostartTeravoltPlayerOverlay)))
        }
        return overlayInfoList
    }

}