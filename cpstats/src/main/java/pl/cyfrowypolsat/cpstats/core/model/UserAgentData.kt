package pl.cyfrowypolsat.cpstats.core.model

data class UserAgentData(val application: String,
                         val build: Int,
                         val deviceType: String,
                         val os: String,
                         val osInfo: String,
                         val player: String,
                         val portal: String,
                         val widevine: Boolean)