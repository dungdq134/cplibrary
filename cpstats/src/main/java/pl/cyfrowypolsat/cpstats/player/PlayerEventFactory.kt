package pl.cyfrowypolsat.cpstats.player

import pl.cyfrowypolsat.cpstats.core.model.AdvertBlockData
import pl.cyfrowypolsat.cpstats.core.model.AdvertData
import pl.cyfrowypolsat.cpstats.core.model.ErrorData
import pl.cyfrowypolsat.cpstats.core.model.PlayerData

class PlayerEventFactory {
    companion object {
        fun playerInitializedEvent(playerData: PlayerData): PlayerEvent {
            return PlayerEvent(EventType.PLAYER_INITIALIZED, playerData)
        }

        fun playerClosedEvent(playerData: PlayerData): PlayerEvent {
            return PlayerEvent(EventType.PLAYER_CLOSED, playerData)
        }

        fun playbackStartedEvent(playerData: PlayerData,
                                 autoplay: Boolean): PlayerEvent {
            return PlayerPlaybackStartedEvent(playerData, autoplay)
        }

        fun playbackFinishedEvent(playerData: PlayerData): PlayerEvent {
            return PlayerEvent(EventType.PLAYBACK_FINISHED, playerData)
        }

        fun playbackPausedEvent(playerData: PlayerData): PlayerEvent {
            return PlayerEvent(EventType.PLAYBACK_PAUSED, playerData)
        }

        fun playbackPositionUpdatedEvent(playerData: PlayerData): PlayerEvent {
            return PlayerEvent(EventType.PLAYBACK_POSITION_UPDATED, playerData)
        }

        fun playbackUnPausedEvent(playerData: PlayerData,
                                  autoplay: Boolean): PlayerEvent {
            return PlayerPlaybackUnpausedEvent(playerData, autoplay)
        }

        fun seekProcessedEvent(playerData: PlayerData): PlayerEvent {
            return PlayerEvent(EventType.SEEK_PROCESSED, playerData)
        }

        fun qualityChangedEvent(playerData: PlayerData): PlayerEvent {
            return PlayerEvent(EventType.QUALITY_CHANGED, playerData)
        }

        fun volumeChangedEvent(playerData: PlayerData): PlayerEvent {
            return PlayerEvent(EventType.VOLUME_CHANGED, playerData)
        }

        fun bufferingStartedEvent(playerData: PlayerData): PlayerEvent {
            return PlayerEvent(EventType.BUFFERING_STARTED, playerData)
        }

        fun bufferingFinishedEvent(playerData: PlayerData): PlayerEvent {
            return PlayerEvent(EventType.BUFFERING_FINISHED, playerData)
        }

        fun licenseRequestStartedEvent(playerData: PlayerData): PlayerEvent {
            return PlayerEvent(EventType.LICENSE_REQUEST_STARTED, playerData)
        }

        fun licenseRequestCompletedEvent(playerData: PlayerData): PlayerEvent {
            return PlayerEvent(EventType.LICENSE_REQUEST_COMPLETED, playerData)
        }

        fun licenseRequestError(errorData: ErrorData,
                                playerData: PlayerData): PlayerEvent {
            return PlayerErrorEvent(EventType.LICENSE_REQUEST_ERROR, playerData, errorData)
        }

        fun drmSessionInitializedEvent(playerData: PlayerData): PlayerEvent {
            return PlayerEvent(EventType.DRM_SESSION_INITIALIZED, playerData)
        }

        fun drmSessionStartedEvent(playerData: PlayerData): PlayerEvent {
            return PlayerEvent(EventType.DRM_SESSION_STARTED, playerData)
        }

        fun drmSessionErrorEvent(errorData: ErrorData,
                                 playerData: PlayerData): PlayerEvent {
            return PlayerErrorEvent(EventType.DRM_SESSION_ERROR, playerData, errorData)
        }

        fun behindLiveWindowErrorEvent(errorData: ErrorData,
                                       playerData: PlayerData): PlayerErrorEvent {
            return PlayerErrorEvent(EventType.PLAYBACK_ERROR, playerData, errorData)
        }

        fun interruptEvent(errorData: ErrorData,
                           playerData: PlayerData): PlayerErrorEvent {
            return PlayerErrorEvent(EventType.PLAYBACK_ERROR, playerData, errorData)
        }

        fun contentPauseRequestEvent(advertBlockData: AdvertBlockData,
                                     playerData: PlayerData): PlayerContentPauseRequestEvent {
            return PlayerContentPauseRequestEvent(playerData, advertBlockData)
        }

        fun contentResumeRequestedEvent(advertBlockData: AdvertBlockData,
                                        playerData: PlayerData): PlayerContentResumeRequestEvent {
            return PlayerContentResumeRequestEvent(playerData, advertBlockData)
        }

        fun overlayStateChangedEvent(playerData: PlayerData): PlayerEvent {
            return PlayerEvent(EventType.OVERLAY_STATE_CHANGED, playerData)
        }

        fun advertBlockStartedEvent(advertData: AdvertData,
                                    advertBlockData: AdvertBlockData,
                                    playerData: PlayerData): PlayerAdvertEvent {
            return PlayerAdvertEvent(EventType.ADVERT_BLOCK_STARTED, playerData, advertData, advertBlockData)
        }

        fun advertBlockFinishedEvent(advertData: AdvertData,
                                     advertBlockData: AdvertBlockData,
                                     playerData: PlayerData): PlayerAdvertEvent {
            return PlayerAdvertEvent(EventType.ADVERT_BLOCK_FINISHED, playerData, advertData, advertBlockData)
        }

        fun advertInitializedEvent(advertData: AdvertData,
                                   advertBlockData: AdvertBlockData,
                                   playerData: PlayerData): PlayerAdvertEvent {
            return PlayerAdvertEvent(EventType.ADVERT_INITIALIZED, playerData, advertData, advertBlockData)
        }

        fun advertStartedEvent(advertData: AdvertData,
                               advertBlockData: AdvertBlockData,
                               playerData: PlayerData,
                               autoplay: Boolean): PlayerAdvertEvent {
            return PlayerAdvertStartedEvent(playerData, autoplay, advertData, advertBlockData)
        }

        fun advertFinishedEvent(advertData: AdvertData,
                                advertBlockData: AdvertBlockData,
                                playerData: PlayerData): PlayerAdvertEvent {
            return PlayerAdvertEvent(EventType.ADVERT_FINISHED, playerData, advertData, advertBlockData)
        }

        fun advertPausedEvent(advertData: AdvertData,
                              advertBlockData: AdvertBlockData,
                              playerData: PlayerData): PlayerAdvertEvent {
            return PlayerAdvertEvent(EventType.ADVERT_PAUSED, playerData, advertData, advertBlockData)
        }

        fun advertUnPausedEvent(advertData: AdvertData,
                                advertBlockData: AdvertBlockData,
                                playerData: PlayerData,
                                autoplay: Boolean): PlayerAdvertEvent {
            return PlayerAdvertUnpausedEvent(playerData, autoplay, advertData, advertBlockData)
        }

        fun advertFirstQuartileEvent(advertData: AdvertData,
                                     advertBlockData: AdvertBlockData,
                                     playerData: PlayerData): PlayerAdvertEvent {
            return PlayerAdvertEvent(EventType.ADVERT_FIRST_QUARTILE, playerData, advertData, advertBlockData)
        }

        fun advertMidPointEvent(advertData: AdvertData,
                                advertBlockData: AdvertBlockData,
                                playerData: PlayerData): PlayerAdvertEvent {
            return PlayerAdvertEvent(EventType.ADVERT_MID_POINT, playerData, advertData, advertBlockData)
        }

        fun advertThirdQuartileEvent(advertData: AdvertData,
                                     advertBlockData: AdvertBlockData,
                                     playerData: PlayerData): PlayerAdvertEvent {
            return PlayerAdvertEvent(EventType.ADVERT_THIRD_QUARTILE, playerData, advertData, advertBlockData)
        }

        fun advertErrorEvent(errorData: ErrorData,
                             playerData: PlayerData): PlayerAdvertErrorEvent {
            return PlayerAdvertErrorEvent(playerData, errorData)
        }
    }
}