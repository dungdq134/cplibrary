package pl.cyfrowypolsat.cpstats.player.gemiusaudiencestream

import com.gemius.sdk.stream.*
import pl.cyfrowypolsat.cpstats.core.ApplicationDataProvider
import pl.cyfrowypolsat.cpstats.core.model.AdvertBlockType
import pl.cyfrowypolsat.cpstats.core.model.MediaId
import pl.cyfrowypolsat.cpstats.player.*
import java.util.*

class GemiusAudienceStreamMapper(private val gemiusAudienceStreamConfig: GemiusAudienceStreamConfig,
                                 private val applicationDataProvider: ApplicationDataProvider) {

    fun map(event: PlayerEvent): GemiusAudienceStreamHit? {
        return when (event) {
            is PlayerAdvertStartedEvent -> mapAdvertStartedEvent(event)
            is PlayerAdvertUnpausedEvent -> mapAdvertUnpausedEvent(event)
            is PlayerAdvertEvent -> mapDefaultAdvertEvent(event)
            is PlayerContentPauseRequestEvent -> mapContentPauseRequestedEvent(event)
            is PlayerPlaybackStartedEvent -> mapPlaybackStartedEvent(event)
            is PlayerPlaybackUnpausedEvent -> mapPlaybackUnpausedEvent(event)
            else -> mapDefaultEvent(event)
        }
    }

    private fun mapDefaultEvent(event: PlayerEvent): GemiusAudienceStreamHit? {
        if (event.playerData.isPlayingAdvert) {
            return null
        }

        return when (event.eventType) {
            EventType.PLAYER_INITIALIZED -> buildNewProgramtHit(event)
            EventType.PLAYER_CLOSED -> buildProgramEventHit(Player.EventType.CLOSE, event)
            EventType.PLAYBACK_FINISHED -> buildProgramEventHit(Player.EventType.COMPLETE, event)
            EventType.PLAYBACK_PAUSED -> buildProgramEventHit(Player.EventType.PAUSE, event)
            EventType.SEEK_PROCESSED -> buildProgramEventHit(Player.EventType.SEEK, event)
            EventType.QUALITY_CHANGED -> buildProgramEventHitWithEventProgramData(Player.EventType.CHANGE_QUAL, event, null)
            EventType.BUFFERING_STARTED -> buildProgramEventHit(Player.EventType.BUFFER, event)
            else -> null
        }
    }

    private fun mapContentPauseRequestedEvent(event: PlayerContentPauseRequestEvent): GemiusAudienceStreamHit? {
        if (event.advertBlockData.blockType != AdvertBlockType.PREROLL) {
            return buildProgramEventHit(Player.EventType.BREAK, event)
        }
        return null
    }

    private fun mapPlaybackStartedEvent(event: PlayerPlaybackStartedEvent): GemiusAudienceStreamHit? {
        return buildProgramEventHitWithEventProgramData(Player.EventType.PLAY, event, event.autoplay)
    }

    private fun mapPlaybackUnpausedEvent(event: PlayerPlaybackUnpausedEvent): GemiusAudienceStreamHit? {
        return buildProgramEventHitWithEventProgramData(Player.EventType.PLAY, event, event.autoplay)
    }

    private fun mapAdvertStartedEvent(event: PlayerAdvertStartedEvent): GemiusAudienceStreamHit? {
        return buildAdEventHitWithEventAdData(Player.EventType.PLAY, event, event.autoplay)
    }

    private fun mapAdvertUnpausedEvent(event: PlayerAdvertUnpausedEvent): GemiusAudienceStreamHit? {
        return buildAdEventHitWithEventAdData(Player.EventType.PLAY, event, event.autoplay)
    }

    private fun mapDefaultAdvertEvent(advertEvent: PlayerAdvertEvent): GemiusAudienceStreamHit? {
        return when (advertEvent.eventType) {
            EventType.ADVERT_INITIALIZED -> buildNewAdHit(advertEvent)
            EventType.ADVERT_FINISHED -> buildAdEventHit(Player.EventType.COMPLETE, advertEvent)
            EventType.ADVERT_PAUSED -> buildAdEventHit(Player.EventType.PAUSE, advertEvent)
            else -> null
        }
    }

    private fun buildNewProgramtHit(event: PlayerEvent): GemiusAudienceStreamHit.NewProgramHit {
        val playerData = event.playerData
        return GemiusAudienceStreamHit.NewProgramHit(
                programId = buildProgramId(playerData.mediaId),
                programData = buildProgramData(event))
    }

    private fun buildNewAdHit(event: PlayerAdvertEvent): GemiusAudienceStreamHit.NewAdHit {
        return GemiusAudienceStreamHit.NewAdHit(
                adId = formatId(event.advertData.advertId),
                adData = buildAdData()
        )
    }

    private fun buildAdEventHitWithEventAdData(et: Player.EventType,
                                               event: PlayerAdvertEvent,
                                               autoplay: Boolean?): GemiusAudienceStreamHit.AdEventHit {
        val playerData = event.playerData
        return GemiusAudienceStreamHit.AdEventHit(
                adId = formatId(event.advertData.advertId),
                programId = buildProgramId(playerData.mediaId),
                offset = calculateOffset(playerData.currentPositionMs, playerData.durationMs, playerData.isLive),
                eventType = et,
                eventAdData = buildEventAdData(autoplay)
        )
    }

    private fun buildAdEventHit(et: Player.EventType,
                                event: PlayerAdvertEvent): GemiusAudienceStreamHit.AdEventHit {
        val playerData = event.playerData
        return GemiusAudienceStreamHit.AdEventHit(
                adId = formatId(event.advertData.advertId),
                programId = buildProgramId(playerData.mediaId),
                offset = calculateOffset(playerData.currentPositionMs, playerData.durationMs, playerData.isLive),
                eventType = et,
                eventAdData = EventAdData())
    }

    private fun buildProgramEventHitWithEventProgramData(et: Player.EventType,
                                                         event: PlayerEvent,
                                                         autoplay: Boolean?): GemiusAudienceStreamHit.ProgramEventHit {
        val playerData = event.playerData
        return GemiusAudienceStreamHit.ProgramEventHit(
                programId = buildProgramId(playerData.mediaId),
                offset = calculateOffset(playerData.currentPositionMs, playerData.durationMs, playerData.isLive),
                eventProgramData = buildEventProgramData(autoplay),
                eventType = et)
    }


    private fun buildProgramEventHit(et: Player.EventType,
                                     event: PlayerEvent): GemiusAudienceStreamHit.ProgramEventHit {
        val playerData = event.playerData
        return GemiusAudienceStreamHit.ProgramEventHit(
                programId = buildProgramId(playerData.mediaId),
                offset = calculateOffset(playerData.currentPositionMs, playerData.durationMs, playerData.isLive),
                eventProgramData = EventProgramData(),
                eventType = et)
    }

    private fun buildProgramData(event: PlayerEvent): ProgramData {
        val playerData = event.playerData
        val transmissionType = convertToTransmissionType(playerData.mediaId)

        val data = ProgramData()
        data.name = formatName(gemiusAudienceStreamConfig.title)
        data.duration = gemiusAudienceStreamConfig.duration
        data.programType = ProgramData.ProgramType.VIDEO
        data.transmissionType = transmissionType
        if (transmissionType == TRANSMISSION_TYPE_BROADCAST) {
            data.transmissionChannel = buildTransmissionChannel(playerData.mediaId, gemiusAudienceStreamConfig.title, gemiusAudienceStreamConfig.playerId)
            data.transmissionStartTime = convertToTransmissionStartTime(event.eventDate)
        }
        data.volume = getDeviceVolume()
        data.resolution = getDeviceResolution()
        return data
    }

    private fun buildAdData(): AdData {
        val adData = AdData()
        adData.volume = getDeviceVolume()
        adData.resolution = getDeviceResolution()
        return adData
    }

    private fun buildEventAdData(autoplay: Boolean?): EventAdData {
        val eventAdData = EventAdData()
        eventAdData.autoPlay = autoplay
        eventAdData.volume = getDeviceVolume()
        eventAdData.resolution = getDeviceResolution()
        return eventAdData
    }

    private fun buildEventProgramData(autoplay: Boolean?): EventProgramData {
        val eventProgramData = EventProgramData()
        eventProgramData.autoPlay = autoplay
        eventProgramData.volume = getDeviceVolume()
        eventProgramData.resolution = getDeviceResolution()
        return eventProgramData
    }

    private fun buildProgramId(mediaId: MediaId): String {
        return formatId("${mediaId.cpid}_${mediaId.id}")
    }

    private fun buildTransmissionChannel(mediaId: MediaId,
                                         mediaTitle: String,
                                         playerId: String): String {
        if (mediaId.type == "live") {
            return "$playerId Live"
        } else {
            return formatName(mediaTitle)
        }
    }

    private fun convertToTransmissionType(mediaId: MediaId): Int {
        return if (mediaId.cpid == 0) {
            TRANSMISSION_TYPE_BROADCAST
        } else {
            TRANSMISSION_TYPE_ON_DEMAND
        }
    }

    private fun calculateOffset(currentPosition: Long,
                                duration: Long,
                                isLive: Boolean): Int {
        if (duration > 0) {
            if (isLive) {
                val timeFromNow = ((currentPosition / 1000) - (duration / 1000)).toInt()
                return ((Date().time / 1000) + timeFromNow).toInt()
            } else {
                return (currentPosition / 1000).toInt()
            }
        }
        return 0
    }

    private fun getDeviceResolution(): String {
        return "${applicationDataProvider.deviceData().screenWidth}x${applicationDataProvider.deviceData().screenHeight}"
    }

    private fun getDeviceVolume(): Int {
        val volume = applicationDataProvider.deviceData().deviceVolumeLevel
        return if (volume <= 0) {
            -1
        } else {
            volume
        }
    }

    private fun convertToTransmissionStartTime(date: Date): String {
        val seconds = (date.time / 1000).toInt()
        return seconds.toString()
    }

    private fun formatName(name: String): String {
        val regex = "[^a-zA-Z0-9 _.!@#*()-/?:;~$,]".toRegex()
        val result = regex.replace(name, "")
        return result.take(64)
    }

    private fun formatId(id: String): String {
        return id.take(64)
    }
}