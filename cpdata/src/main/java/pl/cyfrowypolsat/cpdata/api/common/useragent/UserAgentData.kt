package pl.cyfrowypolsat.cpdata.api.common.useragent

data class UserAgentData(val application: String,
                         val build: Int,
                         val deviceType: String,
                         val os: String,
                         val player: String,
                         val portal: String,
                         val widevine: Boolean)