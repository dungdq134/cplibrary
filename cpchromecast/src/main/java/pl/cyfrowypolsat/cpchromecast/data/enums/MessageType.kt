package pl.cyfrowypolsat.cpchromecast.data.enums

enum class MessageType(val type: String) {
    PLAYBACK_OPTIONS_UPDATED("onPlaybackOptionsUpdated"),
    SKIP_ZONE_STARTED("onSkipZoneStarted"),
    SKIP_ZONE_ENDED("onSkipZoneEnded"),
    PLAYBACK_READY("onPlaybackReady"),
    PLAY_NEXT("onPlayNext"),
    LINEAR_AD_POD_STARTED("onLinearAdPodStarted"),
    LINEAR_AD_POD_ENDED("onLinearAdPodEnded"),
    LINEAR_AD_STARTED("onLinearAdStarted"),
    ERROR("onError"),
    PLAYER_STATUS("playerStatus"),
    RECEIVER_ENVIRONMENT("receiverEnvironment");
}