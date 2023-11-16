package pl.cyfrowypolsat.cpstats.player.cpplayerevents

import pl.cyfrowypolsat.cpstats.core.ApplicationDataProvider
import pl.cyfrowypolsat.cpstats.core.extensions.round
import pl.cyfrowypolsat.cpstats.core.model.AdvertBlockData
import pl.cyfrowypolsat.cpstats.core.model.AdvertBlockType
import pl.cyfrowypolsat.cpstats.core.model.AdvertData
import pl.cyfrowypolsat.cpstats.core.model.DeviceData
import pl.cyfrowypolsat.cpstats.core.model.DeviceType
import pl.cyfrowypolsat.cpstats.core.model.ErrorData
import pl.cyfrowypolsat.cpstats.core.model.IpData
import pl.cyfrowypolsat.cpstats.core.model.MediaId
import pl.cyfrowypolsat.cpstats.core.model.OverlayData
import pl.cyfrowypolsat.cpstats.core.model.OverlayType
import pl.cyfrowypolsat.cpstats.core.model.PlayerContext
import pl.cyfrowypolsat.cpstats.core.model.PlayerData
import pl.cyfrowypolsat.cpstats.core.model.SourceInfo
import pl.cyfrowypolsat.cpstats.core.model.UserAgentData
import pl.cyfrowypolsat.cpstats.player.EventType
import pl.cyfrowypolsat.cpstats.player.PlayerAdvertErrorEvent
import pl.cyfrowypolsat.cpstats.player.PlayerAdvertEvent
import pl.cyfrowypolsat.cpstats.player.PlayerContentPauseRequestEvent
import pl.cyfrowypolsat.cpstats.player.PlayerContentResumeRequestEvent
import pl.cyfrowypolsat.cpstats.player.PlayerErrorEvent
import pl.cyfrowypolsat.cpstats.player.PlayerEvent

internal class CPPlayerEventsMapper(private val config: CPPlayerEventsConfig,
                                    private val applicationDataProvider: ApplicationDataProvider) {
    var authToken = config.authToken

    fun map(event: PlayerEvent): CPPlayerEventsHit? {
        return when (event) {
            is PlayerErrorEvent -> mapErrorEvent(event)
            is PlayerAdvertEvent -> mapAdvertEvent(event)
            is PlayerAdvertErrorEvent -> buildAdvertErrorHit(event)
            is PlayerContentPauseRequestEvent -> buildContentPauseRequestedHit(event)
            is PlayerContentResumeRequestEvent -> buildContentResumeRequestedHit(event)
            else -> mapDefaultEvent(event)
        }
    }

    private fun mapDefaultEvent(event: PlayerEvent): CPPlayerEventsHit? {
        val data = buildDefaultHitData(CPPlayerEventsHit.Status.SUCCESS, event.playerData)
        return when (event.eventType) {
            EventType.PLAYER_INITIALIZED -> buildHit(CPPlayerEventsHit.EventType.INITIALIZED, event, data)
            EventType.PLAYER_CLOSED -> buildHit(CPPlayerEventsHit.EventType.CLOSED, event, data)
            EventType.PLAYBACK_STARTED -> buildHit(CPPlayerEventsHit.EventType.BEGAN_PLAY, event, data)
            EventType.PLAYBACK_FINISHED -> buildHit(CPPlayerEventsHit.EventType.ENDED_PLAY, event, data)
            EventType.LICENSE_REQUEST_STARTED -> buildHit(CPPlayerEventsHit.EventType.LICENSE_REQUEST_STARTED, event, data)
            EventType.LICENSE_REQUEST_COMPLETED -> buildHit(CPPlayerEventsHit.EventType.LICENSE_REQUEST_COMPLETED, event, data)
            EventType.DRM_SESSION_STARTED -> buildHit(CPPlayerEventsHit.EventType.BEGAN_DRM, event, data)
            EventType.PLAYBACK_PAUSED -> buildHit(CPPlayerEventsHit.EventType.PAUSED, event, data)
            EventType.PLAYBACK_POSITION_UPDATED -> buildHit(CPPlayerEventsHit.EventType.CYCLE, event, data)
            EventType.PLAYBACK_UNPAUSED -> buildHit(CPPlayerEventsHit.EventType.UNPAUSED, event, data)
            EventType.SEEK_PROCESSED -> buildHit(CPPlayerEventsHit.EventType.SEEK, event, data)
            EventType.QUALITY_CHANGED -> buildHit(CPPlayerEventsHit.EventType.QUALITY_CHANGED, event, data)
            EventType.BUFFERING_STARTED -> buildHit(CPPlayerEventsHit.EventType.BUFFERING_STARTED, event, data)
            EventType.BUFFERING_FINISHED -> buildHit(CPPlayerEventsHit.EventType.BUFFERING_STOPPED, event, data)
            EventType.OVERLAY_STATE_CHANGED -> buildHit(CPPlayerEventsHit.EventType.OVERLAY_STATE_CHANGED, event, data)
            else -> null
        }
    }

    private fun mapErrorEvent(event: PlayerErrorEvent): CPPlayerEventsHit? {
        val data = buildDefaultHitData(CPPlayerEventsHit.Status.FAILED, event.playerData, errorData = event.errorData)

        return when (event.eventType) {
            EventType.PLAYBACK_ERROR -> buildHit(CPPlayerEventsHit.EventType.INTERRUPTED, event, data)
            EventType.DRM_SESSION_ERROR -> buildHit(CPPlayerEventsHit.EventType.GENERIC_ERROR, event, data)
            EventType.LICENSE_REQUEST_ERROR -> buildHit(CPPlayerEventsHit.EventType.LICENSE_REQUEST_COMPLETED, event, data)
            else -> null
        }
    }

    private fun mapAdvertEvent(event: PlayerAdvertEvent): CPPlayerEventsHit? {
        val data = buildAdvertHitData(CPPlayerEventsHit.Status.SUCCESS, event.playerData, event.advertData, event.advertBlockData)

        return when (event.eventType) {
            EventType.ADVERT_BLOCK_STARTED -> buildHit(CPPlayerEventsHit.EventType.BEGAN_AD_BLOCK, event, data)
            EventType.ADVERT_STARTED -> buildHit(CPPlayerEventsHit.EventType.BEGAN_AD, event, data)
            EventType.ADVERT_FINISHED -> buildHit(CPPlayerEventsHit.EventType.ENDED_AD, event, data)
            EventType.ADVERT_BLOCK_FINISHED -> buildHit(CPPlayerEventsHit.EventType.ENDED_AD_BLOCK, event, data)
            EventType.ADVERT_PAUSED -> buildHit(CPPlayerEventsHit.EventType.AD_PAUSED, event, data)
            EventType.ADVERT_UNPAUSED -> buildHit(CPPlayerEventsHit.EventType.AD_UNPAUSED, event, data)
            EventType.ADVERT_FIRST_QUARTILE -> buildHit(CPPlayerEventsHit.EventType.AD_FIRST_QUARTILE, event, data)
            EventType.ADVERT_MID_POINT -> buildHit(CPPlayerEventsHit.EventType.AD_MID_POINT, event, data)
            EventType.ADVERT_THIRD_QUARTILE -> buildHit(CPPlayerEventsHit.EventType.AD_THIRD_QUARTILE, event, data)
            else -> null
        }
    }

    private fun buildContentPauseRequestedHit(event: PlayerContentPauseRequestEvent): CPPlayerEventsHit? {
        val data = buildDefaultHitData(CPPlayerEventsHit.Status.SUCCESS, event.playerData, event.advertBlockData)
        return buildHit(CPPlayerEventsHit.EventType.CONTENT_PAUSE_REQUESTED, event, data)
    }

    private fun buildContentResumeRequestedHit(event: PlayerContentResumeRequestEvent): CPPlayerEventsHit? {
        val data = buildDefaultHitData(CPPlayerEventsHit.Status.SUCCESS, event.playerData, event.advertBlockData)
        return buildHit(CPPlayerEventsHit.EventType.CONTENT_RESUME_REQUESTED, event, data)
    }

    private fun buildAdvertErrorHit(event: PlayerAdvertErrorEvent): CPPlayerEventsHit? {
        val data = buildAdvertHitData(CPPlayerEventsHit.Status.FAILED, event.playerData, errorData = event.errorData)
        return buildHit(CPPlayerEventsHit.EventType.GENERIC_ADVERT_ERROR, event, data)
    }

    private fun buildDefaultHitData(status: CPPlayerEventsHit.Status,
                                    playerData: PlayerData,
                                    advertBlockData: AdvertBlockData? = null,
                                    errorData: ErrorData? = null): CPPlayerEventsHit.DefaultHitData {
        val errorCode = errorData?.errorCode
        val data = CPPlayerEventsHit.DefaultHitData(
                userAgentData = buildUserAgentData(applicationDataProvider.userAgentData()),
                status = status,
                ipData = buildIpData(applicationDataProvider.ipData()),
                deviceExtraData = buildDeviceData(applicationDataProvider.deviceData()),
                clientId = applicationDataProvider.userData().clientId,
                deviceId = buildDeviceId(applicationDataProvider.deviceData()),
                errorCode = errorCode,
                profileId = applicationDataProvider.userData().profileId,
                playbackTraceId = playerData.playbackTraceId,
                media = buildMediaData(playerData.mediaId),
                source = buildSourceData(playerData.sourceInfo),
                sellModel = playerData.sellModel,
                licenseId = playerData.licenseId,
                durationSeconds = (playerData.playbackDurationMs / 1000).toInt(),
                score = calculateScore(playerData.currentPositionMs, playerData.durationMs, playerData.isLive),
                positionSeconds = calculatePosition(playerData.currentPositionMs, playerData.durationMs, playerData.isLive),
                quality = "${playerData.currentQuality}p",
                bitrate = playerData.streamingSpeed.toInt(),
                frames = buildFramesData(playerData),
                bytesLoaded = buildBytesData(playerData),
                volume = (playerData.currentVolumeLevel * 100).toInt(),
                playerContext = buildPlayerContextData(applicationDataProvider.playerContext),
                advertsRequestUrl = playerData.advertsRequestUrl,
                advertsCuePoints = if (playerData.advertsCuePoints.isNotEmpty()) playerData.advertsCuePoints else null,
                advertBlockData = advertBlockData?.let { buildAdvertBlockData(it) },
                activeOverlays = if (playerData.activeOverlays.isNotEmpty()) buildActiveOverlaysData(playerData.activeOverlays) else null
        )

        return data
    }

    private fun buildAdvertHitData(status: CPPlayerEventsHit.Status,
                                   playerData: PlayerData,
                                   advertData: AdvertData? = null,
                                   advertBlockData: AdvertBlockData? = null,
                                   errorData: ErrorData? = null): CPPlayerEventsHit.AdvertHitData {
        val errorCode = errorData?.errorCode
        val data = CPPlayerEventsHit.AdvertHitData(
                userAgentData = buildUserAgentData(applicationDataProvider.userAgentData()),
                status = status,
                ipData = buildIpData(applicationDataProvider.ipData()),
                deviceExtraData = buildDeviceData(applicationDataProvider.deviceData()),
                clientId = applicationDataProvider.userData().clientId,
                deviceId = buildDeviceId(applicationDataProvider.deviceData()),
                errorCode = errorCode,
                profileId = applicationDataProvider.userData().profileId,
                playbackTraceId = playerData.playbackTraceId,
                advertData = advertData?.let { buildAdvertData(it) },
                advertBlockData = advertBlockData?.let { buildAdvertBlockData(it) }
        )

        return data
    }

    private fun buildBytesData(data: PlayerData): CPPlayerEventsHit.BytesData {
        return CPPlayerEventsHit.BytesData(data.audioBytesLoaded, data.videoBytesLoaded, (data.audioBytesLoaded + data.videoBytesLoaded))
    }

    private fun buildFramesData(data: PlayerData): CPPlayerEventsHit.FramesData {
        val totalFramesCount = data.frameRate * (data.playbackDurationMs / 1000)
        return CPPlayerEventsHit.FramesData(data.droppedFramesCount, totalFramesCount.toLong())
    }

    private fun buildHit(type: CPPlayerEventsHit.EventType,
                         event: PlayerEvent,
                         data: CPPlayerEventsHit.HitData): CPPlayerEventsHit {

        return CPPlayerEventsHit(
                jwt = authToken,
                portal = applicationDataProvider.portal,
                originator = config.originator,
                apiVersion = config.serviceVersion,
                eventDate = event.eventDate,
                eventType = type,
                traceId = applicationDataProvider.applicationTraceId,
                data = data,
                eventId = event.eventId
        )
    }

    private fun buildIpData(ipData: IpData): CPPlayerEventsHit.IpData {
        return CPPlayerEventsHit.IpData(
                ip = ipData.ip,
                country = ipData.country,
                isEu = ipData.isEu,
                isVpn = ipData.isVpn,
                continent = ipData.continent,
                isp = ipData.isp
        )
    }

    private fun buildUserAgentData(userAgentData: UserAgentData): CPPlayerEventsHit.UserAgentData {
        return CPPlayerEventsHit.UserAgentData(
                application = userAgentData.application,
                build = userAgentData.build,
                deviceType = userAgentData.deviceType,
                os = userAgentData.os,
                osInfo = userAgentData.osInfo,
                player = userAgentData.player,
                portal = userAgentData.portal,
                widevine = userAgentData.widevine
        )
    }

    private fun buildDeviceData(deviceData: DeviceData): CPPlayerEventsHit.DeviceExtraData {
        val screenSize = when (deviceData.deviceType) {
            DeviceType.PHONE -> CPPlayerEventsHit.ScreenSize(deviceData.screenHeight, deviceData.screenWidth, deviceData.screenDiagonal)
            else -> null
        }
        return CPPlayerEventsHit.DeviceExtraData(
                manufacturer = deviceData.manufacturer,
                model = deviceData.model,
                screenSize = screenSize
        )
    }

    private fun buildMediaData(mediaId: MediaId): CPPlayerEventsHit.MediaData {
        return CPPlayerEventsHit.MediaData(
                cpid = mediaId.cpid,
                id = "${mediaId.cpid}:${mediaId.id}",
                type = mediaId.type
        )
    }

    private fun buildSourceData(sourceInfo: SourceInfo): CPPlayerEventsHit.SourceData {
        return CPPlayerEventsHit.SourceData(
                id = sourceInfo.id,
                accessMethod = sourceInfo.accessMethod,
                fileFormat = sourceInfo.fileFormat,
                drmType = sourceInfo.drmType
        )
    }

    private fun calculateScore(currentPosition: Long,
                               duration: Long,
                               isLive: Boolean): Double {
        if (duration > 0 && !isLive) {
            val score: Double = (currentPosition.toDouble() / duration.toDouble()) * 100
            return if (score <= 100) score.round(2) else 100.0
        }
        return 0.0
    }

    private fun calculatePosition(currentPosition: Long,
                                  duration: Long,
                                  isLive: Boolean): Int {
        if (duration > 0) {
            if (isLive) {
                return ((currentPosition / 1000) - (duration / 1000)).toInt()
            } else {
                return (currentPosition / 1000).toInt()
            }
        }
        return 0
    }

    private fun buildPlayerContextData(playerContext: PlayerContext): CPPlayerEventsHit.PlayerContextData {
        return CPPlayerEventsHit.PlayerContextData(playerContext.placeType, playerContext.placeValue)
    }

    private fun buildDeviceId(deviceData: DeviceData): CPPlayerEventsHit.DeviceId {
        return CPPlayerEventsHit.DeviceId(type = deviceData.deviceIdType, value = deviceData.deviceIdValue)
    }

    private fun buildAdvertData(advertData: AdvertData): CPPlayerEventsHit.AdvertData? {
        return CPPlayerEventsHit.AdvertData(
                id = advertData.advertId,
                indexInBlock = advertData.advertIndex,
                durationSeconds = (advertData.advertDurationSeconds).toInt()
        )
    }

    private fun buildAdvertBlockData(advertBlockData: AdvertBlockData): CPPlayerEventsHit.AdvertBlockData? {
        val advertBlockType = when (advertBlockData.blockType) {
            AdvertBlockType.PREROLL -> CPPlayerEventsHit.AdvertBlockType.PREROLL
            AdvertBlockType.MIDROLL -> CPPlayerEventsHit.AdvertBlockType.MIDROLL
            AdvertBlockType.POSTROLL -> CPPlayerEventsHit.AdvertBlockType.POSTROLL
        }

        return CPPlayerEventsHit.AdvertBlockData(
                blockType = advertBlockType,
                totalAdsCount = advertBlockData.totalAdsCount,
                blockIndex = advertBlockData.blockIndex,
                timeOffsetSeconds = advertBlockData.timeOffsetSeconds
        )
    }

    private fun buildActiveOverlaysData(overlayDataList: List<OverlayData>): List<CPPlayerEventsHit.ActiveOverlay> {
        val activeOverlays = mutableListOf<CPPlayerEventsHit.ActiveOverlay>()
        overlayDataList.forEach { overlayData ->
            val overlayType = when (overlayData.type) {
                OverlayType.TERAVOLT -> CPPlayerEventsHit.OverlayType.TERAVOLT
                else -> null
            }
            if (overlayType != null) {
                activeOverlays.add(CPPlayerEventsHit.ActiveOverlay(overlayType, overlayData.autoStart))
            }
        }
        return activeOverlays
    }
}
