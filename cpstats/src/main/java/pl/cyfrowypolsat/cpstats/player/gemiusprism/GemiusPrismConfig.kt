package pl.cyfrowypolsat.cpstats.player.gemiusprism

data class GemiusPrismConfig(val userAgent: String,
                             val service: String,
                             val account: String,
                             val viewCategory: String,
                             val mediaCategory: String,
                             val distributor: String,
                             val title: String,
                             val mediaType: String,
                             val duration: Long,
                             val intervalMs: Int,
                             val mediaSourceName: String?) {
    companion object {
        private const val GEMIUS_PRISM_STAGING_INTERVAL_IN_MILLISECONDS = 10 * 1000
        private const val GEMIUS_PRISM_PRODUCTION_INTERVAL_IN_MILLISECONDS = 5 * 60 * 1000

        fun getIntervalInMilliseconds(staging: Boolean): Int {
            return if (!staging) GEMIUS_PRISM_PRODUCTION_INTERVAL_IN_MILLISECONDS else GEMIUS_PRISM_STAGING_INTERVAL_IN_MILLISECONDS
        }
    }
}