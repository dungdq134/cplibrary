package pl.cyfrowypolsat.cpstats.player

import pl.cyfrowypolsat.cpstats.core.model.AdvertBlockData
import pl.cyfrowypolsat.cpstats.core.model.AdvertData
import pl.cyfrowypolsat.cpstats.core.model.ErrorData
import pl.cyfrowypolsat.cpstats.core.model.PlayerData
import java.util.*

enum class EventType {
    PLAYER_INITIALIZED,
    PLAYER_CLOSED,

    PLAYBACK_STARTED,
    PLAYBACK_FINISHED,
    PLAYBACK_ERROR,

    LICENSE_REQUEST_STARTED,
    LICENSE_REQUEST_COMPLETED,
    LICENSE_REQUEST_ERROR,

    DRM_SESSION_INITIALIZED,
    DRM_SESSION_STARTED,
    DRM_SESSION_ERROR,

    PLAYBACK_PAUSED,
    PLAYBACK_UNPAUSED,
    PLAYBACK_POSITION_UPDATED,

    SEEK_PROCESSED,
    QUALITY_CHANGED,
    VOLUME_CHANGED,

    BUFFERING_STARTED,
    BUFFERING_FINISHED,

    CONTENT_PAUSE_REQUESTED,
    CONTENT_RESUME_REQUESTED,

    OVERLAY_STATE_CHANGED,

    /* ADVERTS */
    ADVERT_BLOCK_STARTED,
    ADVERT_INITIALIZED,
    ADVERT_STARTED,
    ADVERT_FINISHED,
    ADVERT_BLOCK_FINISHED,
    ADVERT_ERROR,
    ADVERT_PAUSED,
    ADVERT_UNPAUSED,
    ADVERT_FIRST_QUARTILE,
    ADVERT_MID_POINT,
    ADVERT_THIRD_QUARTILE,
}

open class PlayerEvent(val eventType: EventType,
                       val playerData: PlayerData,
                       val eventDate: Date = Date(),
                       val eventId: String = UUID.randomUUID().toString())

class PlayerErrorEvent(eventType: EventType,
                       playerData: PlayerData,
                       val errorData: ErrorData) : PlayerEvent(eventType, playerData)

open class PlayerContentPauseRequestEvent(playerData: PlayerData,
                                          val advertBlockData: AdvertBlockData) : PlayerEvent(EventType.CONTENT_PAUSE_REQUESTED, playerData)

open class PlayerContentResumeRequestEvent(playerData: PlayerData,
                                          val advertBlockData: AdvertBlockData) : PlayerEvent(EventType.CONTENT_RESUME_REQUESTED, playerData)

open class PlayerAdvertEvent(eventType: EventType,
                             playerData: PlayerData,
                             val advertData: AdvertData,
                             val advertBlockData: AdvertBlockData) : PlayerEvent(eventType, playerData)

class PlayerAdvertErrorEvent(playerData: PlayerData,
                             val errorData: ErrorData) : PlayerEvent(EventType.ADVERT_ERROR, playerData)


class PlayerAdvertStartedEvent(playerData: PlayerData,
                               val autoplay: Boolean,
                               advertData: AdvertData,
                               advertBlockData: AdvertBlockData) : PlayerAdvertEvent(EventType.ADVERT_STARTED, playerData, advertData, advertBlockData)

class PlayerAdvertUnpausedEvent(playerData: PlayerData,
                                val autoplay: Boolean,
                                advertData: AdvertData,
                                advertBlockData: AdvertBlockData) : PlayerAdvertEvent(EventType.ADVERT_UNPAUSED, playerData, advertData, advertBlockData)

class PlayerPlaybackStartedEvent(playerData: PlayerData,
                                 val autoplay: Boolean) : PlayerEvent(EventType.PLAYBACK_STARTED, playerData)

class PlayerPlaybackUnpausedEvent(playerData: PlayerData,
                                  val autoplay: Boolean) : PlayerEvent(EventType.PLAYBACK_UNPAUSED, playerData)