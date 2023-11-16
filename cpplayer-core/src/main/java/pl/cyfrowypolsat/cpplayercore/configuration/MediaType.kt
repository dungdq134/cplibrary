package pl.cyfrowypolsat.cpplayercore.configuration

enum class MediaType {
    LIVE,
    CHANNEL,
    VOD;

    companion object {
        fun getFromString(value: String): MediaType {
            return when (value) {
                "live" -> LIVE
                "tv" -> CHANNEL
                else -> VOD
            }
        }
    }

}