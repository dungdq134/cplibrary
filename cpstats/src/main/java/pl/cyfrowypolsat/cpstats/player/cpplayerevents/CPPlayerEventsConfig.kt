package pl.cyfrowypolsat.cpstats.player.cpplayerevents

data class CPPlayerEventsConfig(val service: String,
                                val serviceVersion: String,
                                val userAgent: String,
                                val originator: String,
                                val authToken: String,
                                val intervalMs: Int,
                                internal val unauthorizedCallback: CPPlayerEventsUnauthorizedCallback) {

    companion object {
        private const val PLAYER_EVENTS_API_VERSION = "1"
        private const val PLAYER_EVENTS_STAGING_URL = "https://yag-staging.redefine.pl/events/player"
        private const val PLAYER_EVENTS_STAGING_INTERVAL_IN_MILLISECONDS = 10 * 1000

        fun getIntervalInMilliseconds(intervalInSeconds: Int,
                                      staging: Boolean): Int {
            return if (!staging) intervalInSeconds * 1000 else PLAYER_EVENTS_STAGING_INTERVAL_IN_MILLISECONDS
        }

        fun getServiceUrl(url: String,
                          staging: Boolean = true): String {
            return if (staging) PLAYER_EVENTS_STAGING_URL else url
        }

        fun getServiceVersion(): String {
            return PLAYER_EVENTS_API_VERSION
        }
    }
}