package pl.cyfrowypolsat.cpstats.application.cpactivityevents

data class CPActivityEventsConfig(val service: String,
                                  val serviceVersion: String,
                                  val userAgent: String,
                                  val originator: String,
                                  val authToken: String,
                                  internal val unauthorizedCallback: CPActivityEventsUnauthorizedCallback) {

    companion object {
        private const val ACTIVITY_EVENTS_API_VERSION = "1"
        private const val ACTIVITY_EVENTS_STAGING_URL = "https://yag-staging.redefine.pl/events/activity"

        fun getServiceVersion(): String {
            return ACTIVITY_EVENTS_API_VERSION
        }

        fun getServiceUrl(url: String,
                          staging: Boolean): String {
            return if (staging) ACTIVITY_EVENTS_STAGING_URL else url
        }
    }
}