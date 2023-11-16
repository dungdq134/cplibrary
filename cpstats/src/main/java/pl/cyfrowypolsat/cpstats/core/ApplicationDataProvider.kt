package pl.cyfrowypolsat.cpstats.core

import pl.cyfrowypolsat.cpstats.core.model.*
import java.util.*

interface ApplicationDataProvider {

    var applicationTraceId: String

    var sessionDurationSeconds: Long

    var currentPlaceData: PlaceData

    var playerContext: PlayerContext

    val isFirstLaunch: Boolean

    val launchCount: Int

    val autoStart: Boolean

    val portal: String

    val websiteUrl: String

    val clientVersion: String

    val clientVersionCode: Int

    val isStaging: Boolean

    val gemiusPlayerId: String

    fun applicationUserAgent(): String

    fun userAgentData(): UserAgentData

    fun userData(): UserData

    fun deviceData(): DeviceData

    fun ipData(): IpData

    fun buildApplicationData(): ApplicationData {

        return ApplicationData(
                userAgentData = userAgentData(),
                userData = userData(),
                deviceData = deviceData(),
                ipData = ipData(),
                portalId = portal,
                websiteUrl = websiteUrl,
                clientVersionCode = clientVersionCode,
                clientVersion = clientVersion,
                traceId = applicationTraceId,
                userAgent = applicationUserAgent(),
                autoStart = autoStart,
                isFirstLaunch = isFirstLaunch,
                launchCount = launchCount,
                currentPlaceData = currentPlaceData,
                sessionDurationSeconds = sessionDurationSeconds.toInt(),
                playerContext = playerContext
        )
    }
}