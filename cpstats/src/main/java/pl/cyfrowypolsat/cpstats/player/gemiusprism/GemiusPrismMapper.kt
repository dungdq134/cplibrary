package pl.cyfrowypolsat.cpstats.player.gemiusprism

import pl.cyfrowypolsat.cpstats.core.ApplicationDataProvider
import pl.cyfrowypolsat.cpstats.core.gemiusprism.GemiusPrismUtils
import pl.cyfrowypolsat.cpstats.core.model.*
import pl.cyfrowypolsat.cpstats.player.EventType
import pl.cyfrowypolsat.cpstats.player.PlayerEvent
import pl.cyfrowypolsat.cpstats.player.PlayerAdvertEvent
import kotlin.math.ceil

internal class GemiusPrismMapper(private val gemiusPrismConfig: GemiusPrismConfig,
                                 private val applicationDataProvider: ApplicationDataProvider) {
    class HrefParam {
        companion object {
            const val VIEW_CATEGORY = "Cat"
            const val TITLE = "m"
            const val MEDIA_CATEGORY_TITLE = "mCat"
            const val QUALITY = "qa"
            const val DISTRIBUTOR = "d"
            const val DURATION = "l"
            const val MEDIA_SOURCE_NAME = "mt"
            const val ADVERT_ID = "ac"
            const val ADVERT_INDEX = "ap"
            const val ADVERTS_IN_BLOCK = "an"
        }
    }

    class ExtraParam {
        companion object {
            const val GOAL_NAME = "GoalName"
            const val LOGIN_TYPE = "lt"
            const val CLIENT = "c"
            const val USER_AGENT = "ua"
            const val UAD_PORTAL = "uad_portal"
            const val UAD_DEVICE_TYPE = "uad_deviceType"
            const val UAD_APPLICATION = "uad_application"
            const val UAD_PLAYER = "uad_player"
            const val UAD_BUILD = "uad_build"
            const val UAD_OS = "uad_os"
            const val UAD_OS_INFO = "uad_os_info"
        }
    }

    fun map(event: PlayerEvent): GemiusPrismHit? {
        return when (event) {
            is PlayerAdvertEvent -> mapAdvertHit(event)
            else -> mapDefaultHit(event)
        }
    }

    fun mapToContactHit(event: PlayerEvent): GemiusPrismHit? {
        val isPlaybackBeginHit = event.eventType == EventType.PLAYBACK_STARTED
        val isPrerollBeginHit = event.eventType == EventType.ADVERT_BLOCK_STARTED && event is PlayerAdvertEvent && event.advertBlockData.blockType == AdvertBlockType.PREROLL

        return if (isPlaybackBeginHit || isPrerollBeginHit) {
            buildDefaultHit(GemiusPrismHit.PlaybackGoal.CONTACT, event.playerData)
        } else {
            null
        }
    }

    fun mapAdvertHit(event: PlayerAdvertEvent): GemiusPrismHit? {
        return when (event.eventType) {
            EventType.ADVERT_BLOCK_STARTED -> buildAdvertBlockStartedHit(event.playerData, event.advertData, event.advertBlockData)
            EventType.ADVERT_STARTED -> buildAdvertStartedHit(event.playerData, event.advertData, event.advertBlockData)
            EventType.ADVERT_BLOCK_FINISHED -> buildAdvertBlockFinishedHit(event.playerData, event.advertData, event.advertBlockData)
            else -> null
        }
    }

    fun mapDefaultHit(event: PlayerEvent): GemiusPrismHit? {
        return when (event.eventType) {
            EventType.PLAYER_INITIALIZED -> buildDefaultHit(GemiusPrismHit.PlaybackGoal.NEW_MATERIAL, event.playerData)
            EventType.PLAYER_CLOSED -> buildDefaultHit(GemiusPrismHit.PlaybackGoal.STOP, event.playerData)
            EventType.PLAYBACK_STARTED -> buildDefaultHit(GemiusPrismHit.PlaybackGoal.START, event.playerData)
            EventType.PLAYBACK_FINISHED -> buildDefaultHit(GemiusPrismHit.PlaybackGoal.END, event.playerData)
            EventType.PLAYBACK_PAUSED -> buildDefaultHit(GemiusPrismHit.PlaybackGoal.PAUSE, event.playerData)
            EventType.PLAYBACK_POSITION_UPDATED -> buildDefaultHit(GemiusPrismHit.PlaybackGoal.CYCLE, event.playerData)
            EventType.PLAYBACK_UNPAUSED -> buildDefaultHit(GemiusPrismHit.PlaybackGoal.UNPAUSE, event.playerData)
            EventType.SEEK_PROCESSED -> buildSeekHit(event.playerData)
            EventType.QUALITY_CHANGED -> buildDefaultHit(GemiusPrismHit.PlaybackGoal.CHANGE_QUALITY, event.playerData)
            EventType.BUFFERING_STARTED -> buildDefaultHit(GemiusPrismHit.PlaybackGoal.BUFFERING, event.playerData)
            EventType.VOLUME_CHANGED -> buildDefaultHit(GemiusPrismHit.PlaybackGoal.CHANGE_VOLUME, event.playerData)
            else -> null
        }
    }

    private fun buildSeekHit(playerData: PlayerData): GemiusPrismHit {
        if (playerData.isLive) {
            val playbackGoal = GemiusPrismHit.PlaybackGoal.SEEK_LIVE
            playbackGoal.value = calculateTimeshiftingPosition(playerData.currentPositionMs, playerData.durationMs)
            return buildDefaultHit(playbackGoal, playerData)
        }
        return buildDefaultHit(GemiusPrismHit.PlaybackGoal.SEEK, playerData)
    }

    private fun buildAdvertBlockStartedHit(playerData: PlayerData,
                                           advertData: AdvertData,
                                           advertBlockData: AdvertBlockData): GemiusPrismHit? {
        val playbackGoal = when (advertBlockData.blockType) {
            AdvertBlockType.PREROLL -> GemiusPrismHit.PlaybackGoal.PREROLL_BLOCK_BEGIN
            AdvertBlockType.MIDROLL -> GemiusPrismHit.PlaybackGoal.MIDROLL_BLOCK_BEGIN
            AdvertBlockType.POSTROLL -> GemiusPrismHit.PlaybackGoal.POSTROLL_BLOCK_BEGIN
        }

        return buildAdvertHit(playbackGoal, playerData, advertData)
    }

    private fun buildAdvertBlockFinishedHit(playerData: PlayerData,
                                            advertData: AdvertData,
                                            advertBlockData: AdvertBlockData): GemiusPrismHit? {
        val playbackGoal = when (advertBlockData.blockType) {
            AdvertBlockType.PREROLL -> GemiusPrismHit.PlaybackGoal.PREROLL_BLOCK_END
            AdvertBlockType.MIDROLL -> GemiusPrismHit.PlaybackGoal.MIDROLL_BLOCK_END
            AdvertBlockType.POSTROLL -> GemiusPrismHit.PlaybackGoal.POSTROLL_BLOCK_END
        }

        return buildAdvertHit(playbackGoal, playerData, advertData)
    }

    private fun buildAdvertStartedHit(playerData: PlayerData,
                                      advertData: AdvertData,
                                      advertBlockData: AdvertBlockData): GemiusPrismHit? {
        val playbackGoal = when (advertBlockData.blockType) {
            AdvertBlockType.PREROLL -> GemiusPrismHit.PlaybackGoal.PREROLL_BEGIN
            AdvertBlockType.MIDROLL -> GemiusPrismHit.PlaybackGoal.MIDROLL_BEGIN
            AdvertBlockType.POSTROLL -> GemiusPrismHit.PlaybackGoal.POSTROLL_BEGIN
        }

        return buildAdvertHit(playbackGoal, playerData, advertData)
    }

    private fun buildAdvertHit(playbackGoal: GemiusPrismHit.PlaybackGoal,
                               playerData: PlayerData,
                               advertData: AdvertData): GemiusPrismHit {

        val hrefParamsUrl = buildHrefParamsUrl(playerData, advertData)
        val extraParamsUrl = buildExtraParamsUrl(playbackGoal)

        return GemiusPrismHit(gemiusPrismConfig.account, hrefParamsUrl, extraParamsUrl, GemiusPrismHit.Type.ACTION)
    }


    private fun buildDefaultHit(playbackGoal: GemiusPrismHit.PlaybackGoal,
                                playerData: PlayerData): GemiusPrismHit {

        val hrefParamsUrl = buildHrefParamsUrl(playerData)
        val extraParamsUrl = buildExtraParamsUrl(playbackGoal)

        return GemiusPrismHit(gemiusPrismConfig.account, hrefParamsUrl, extraParamsUrl, GemiusPrismHit.Type.ACTION)
    }

    private fun buildHrefParamsUrl(playerData: PlayerData,
                                   advertData: AdvertData? = null,
                                   advertBlockData: AdvertBlockData? = null): String {
        val durationString = buildDurationClass(gemiusPrismConfig.duration, playerData.isLive).value
        val qualityString = convertToQualityString(playerData.currentQuality)

        val map: Map<*, *> = HashMap<String, String>().apply {
            gemiusPrismConfig.viewCategory.let { put(HrefParam.VIEW_CATEGORY, it) }
            gemiusPrismConfig.title.let { put(HrefParam.TITLE, it) }
            gemiusPrismConfig.mediaCategory.let { put(HrefParam.MEDIA_CATEGORY_TITLE, it) }
            qualityString?.let { put(HrefParam.QUALITY, it) }
            gemiusPrismConfig.distributor.let { put(HrefParam.DISTRIBUTOR, it) }
            durationString.let { put(HrefParam.DURATION, it) }
            gemiusPrismConfig.mediaSourceName?.let { put(HrefParam.MEDIA_SOURCE_NAME, it) }
            advertBlockData?.totalAdsCount?.let { put(HrefParam.ADVERTS_IN_BLOCK, "$it") }
            advertData?.advertIndex?.let { put(HrefParam.ADVERT_INDEX, "$it") }
            advertData?.advertId?.let { put(HrefParam.ADVERT_ID, it) }
        }
        val params = map.map { "${it.key}=${GemiusPrismUtils.specialEscape(it.value.toString())}" }.joinToString("&")
        return "${applicationDataProvider.websiteUrl}?$params"
    }

    fun buildExtraParamsUrl(playbackGoal: GemiusPrismHit.PlaybackGoal): String {
        val goalName = if (playbackGoal.hasValue && playbackGoal.value != null) "${playbackGoal.type}${playbackGoal.value}" else playbackGoal.type
        val userAgentData = applicationDataProvider.userAgentData()
        val loginType = applicationDataProvider.userData().userType.convertToString()

        val map: Map<*, *> = HashMap<String, String>().apply {
            goalName.let { put(ExtraParam.GOAL_NAME, it) }
            loginType.let { put(ExtraParam.LOGIN_TYPE, it) }
            applicationDataProvider.portal.let { put(ExtraParam.CLIENT, it) }
            gemiusPrismConfig.userAgent.let { put(ExtraParam.USER_AGENT, it) }
            userAgentData.portal.let { put(ExtraParam.UAD_PORTAL, it) }
            userAgentData.deviceType.let { put(ExtraParam.UAD_DEVICE_TYPE, it) }
            userAgentData.application.let { put(ExtraParam.UAD_APPLICATION, it) }
            userAgentData.player.let { put(ExtraParam.UAD_PLAYER, it) }
            userAgentData.build.let { put(ExtraParam.UAD_BUILD, it.toString()) }
            userAgentData.os.let { put(ExtraParam.UAD_OS, it) }
            userAgentData.osInfo.let { put(ExtraParam.UAD_OS_INFO, it) }
        }
        val params = map.map { "${it.key}=${GemiusPrismUtils.specialEscape(it.value.toString())}" }.joinToString("|")
        return params
    }


    private fun buildDurationClass(durationSeconds: Long,
                                   isLive: Boolean): GemiusPrismHit.DurationClass {
        val durationMinutes = durationSeconds / 60
        return if (isLive) {
            GemiusPrismHit.DurationClass.DURATION_LIVE
        } else if (durationMinutes in 1..5) {
            GemiusPrismHit.DurationClass.DURATION_0_5
        } else if (durationMinutes in 6..10) {
            GemiusPrismHit.DurationClass.DURATION_5_10
        } else if (durationMinutes in 11..15) {
            GemiusPrismHit.DurationClass.DURATION_10_15
        } else {
            GemiusPrismHit.DurationClass.DURATION_15_00
        }
    }

    private fun calculateTimeshiftingPosition(currentPosition: Long,
                                              durationMilliseconds: Long): Int {
        return ceil(((durationMilliseconds - currentPosition) / 1000 / 60).toDouble() / 60.0).toInt()
    }

    private fun convertToQualityString(quality: Int): String? {
        return if (quality > 0) "${quality}p" else null
    }

    private fun UserType.convertToString(): String {
        return when (this) {
            UserType.UNLOGGED -> "none"
            UserType.NATIVE -> "native"
            UserType.FACEBOOK -> "fb"
            UserType.ICOK -> "icok"
            UserType.APPLE -> "apple"
            UserType.GOOGLE -> "google"
            UserType.PLUS -> "plus"
            UserType.NETIA -> "netia"
            UserType.SSO -> "sso"
        }
    }
}