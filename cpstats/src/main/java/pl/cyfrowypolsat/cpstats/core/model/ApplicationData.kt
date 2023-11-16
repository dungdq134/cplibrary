package pl.cyfrowypolsat.cpstats.core.model

data class ApplicationData(val userAgentData: UserAgentData,
                           val userData: UserData,
                           val deviceData: DeviceData,
                           val ipData: IpData,
                           val portalId: String,
                           val websiteUrl: String,
                           val clientVersionCode: Int,
                           val clientVersion: String,
                           val traceId: String,
                           val autoStart: Boolean,
                           val isFirstLaunch: Boolean,
                           val launchCount: Int,
                           val userAgent: String,
                           val currentPlaceData: PlaceData,
                           val sessionDurationSeconds: Int,
                           val playerContext: PlayerContext)