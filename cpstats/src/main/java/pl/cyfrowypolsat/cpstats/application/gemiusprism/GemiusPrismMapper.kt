package pl.cyfrowypolsat.cpstats.application.gemiusprism

import pl.cyfrowypolsat.cpstats.application.*
import pl.cyfrowypolsat.cpstats.core.gemiusprism.GemiusPrismUtils
import pl.cyfrowypolsat.cpstats.core.model.ApplicationData
import pl.cyfrowypolsat.cpstats.core.model.UserType

internal class GemiusPrismMapper(private val gemiusPrismConfig: GemiusPrismConfig) {
    class HrefParam {
        companion object {
            const val TITLE = "m"
            const val CATEGORY = "Cat"
            const val QUERY = "q"
        }
    }

    class ExtraParam {
        companion object {
            const val GOAL_NAME = "GoalName"
            const val LOGIN_TYPE = "lt"
            const val CLIENT = "c"
            const val USER_AGENT = "ua"
            const val UAD_PORTAL = "uad_portal"
            const val UAD_DEVICE_TYPE = "uad_deviceType"
            const val UAD_APPLICATION = "uad_application"
            const val UAD_PLAYER = "uad_player"
            const val UAD_BUILD = "uad_build"
            const val UAD_OS = "uad_os"
            const val UAD_OS_INFO = "uad_os_info"
        }
    }

    fun map(event: ApplicationEvent): GemiusPrismHit? {
        return when (event) {
            else -> mapDefaultEvent(event)
        }
    }

    private fun mapDefaultEvent(event: ApplicationEvent): GemiusPrismHit? {
        return when (event.eventType) {
            EventType.APP_START -> buildStartHit(event.applicationData)
            EventType.APP_PAUSE -> buildDefaultHit(GemiusPrismHit.InterfaceEvent.APP_BACKGROUND, event.applicationData)
            EventType.APP_RESUME -> buildDefaultHit(GemiusPrismHit.InterfaceEvent.APP_FOREGROUND, event.applicationData)
            EventType.LOGIN -> buildLoginHit(event.applicationData)
            EventType.LOGOUT -> buildDefaultHit(GemiusPrismHit.InterfaceEvent.LOGOUT, event.applicationData)
            else -> null
        }
    }

    private fun buildDefaultHit(eventType: GemiusPrismHit.InterfaceEvent,
                                applicationData: ApplicationData): GemiusPrismHit {
        val hrefParamsUrl = buildHrefParamsUrl(applicationData)
        val extraParamsUrl = buildExtraParamsUrl(applicationData, eventType)
        return GemiusPrismHit(gemiusPrismConfig.account, hrefParamsUrl, extraParamsUrl, GemiusPrismHit.Type.ACTION)
    }

    private fun buildLoginHit(applicationData: ApplicationData): GemiusPrismHit {
        val hrefParamsUrl = buildHrefParamsUrl(applicationData)
        val eventType = when (applicationData.userData.userType) {
            UserType.NATIVE -> GemiusPrismHit.InterfaceEvent.LOGIN_EMAIL
            UserType.FACEBOOK -> GemiusPrismHit.InterfaceEvent.LOGIN_FACEBOOK
            UserType.ICOK -> GemiusPrismHit.InterfaceEvent.LOGIN_ICOK
            UserType.PLUS -> GemiusPrismHit.InterfaceEvent.LOGIN_PLUS
            UserType.APPLE -> GemiusPrismHit.InterfaceEvent.LOGIN_APPLE
            UserType.GOOGLE -> GemiusPrismHit.InterfaceEvent.LOGIN_GOOGLE
            UserType.SSO -> GemiusPrismHit.InterfaceEvent.LOGIN_SSO
            else -> GemiusPrismHit.InterfaceEvent.LOGIN
        }
        val extraParamsUrl = buildExtraParamsUrl(applicationData, eventType)
        return GemiusPrismHit(gemiusPrismConfig.account, hrefParamsUrl, extraParamsUrl, GemiusPrismHit.Type.ACTION)
    }

    private fun buildStartHit(applicationData: ApplicationData): GemiusPrismHit? {
        val eventType = if (applicationData.isFirstLaunch) GemiusPrismHit.InterfaceEvent.FIRST_APP_START else GemiusPrismHit.InterfaceEvent.APP_START
        return buildDefaultHit(eventType, applicationData)
    }

    private fun buildHrefParamsUrl(applicationData: ApplicationData,
                                   mediaTitle: String? = null,
                                   category: String? = null,
                                   searchQuery: String? = null): String {
        val map: Map<*, *> = HashMap<String, String>().apply {
            mediaTitle?.let { put(HrefParam.TITLE, it) }
            category?.let { put(HrefParam.CATEGORY, it) }
            searchQuery?.let { put(HrefParam.QUERY, it) }
        }
        val params = map.map { "${it.key}=${GemiusPrismUtils.specialEscape(it.value.toString())}" }.joinToString("&")
        return "${applicationData.websiteUrl}?$params"
    }

    fun buildExtraParamsUrl(applicationData: ApplicationData,
                            event: GemiusPrismHit.InterfaceEvent): String {
        val goalName = if (event.hasValue && event.value != null) "${event.type}${event.value}" else event.type
        val loginType = applicationData.userData.userType.convertToString()
        val userAgentData = applicationData.userAgentData

        val map: Map<*, *> = HashMap<String, String>().apply {
            goalName.let { put(ExtraParam.GOAL_NAME, it) }
            loginType.let { put(ExtraParam.LOGIN_TYPE, it) }
            applicationData.portalId.let { put(ExtraParam.CLIENT, it) }
            gemiusPrismConfig.userAgent.let { put(ExtraParam.USER_AGENT, it) }
            userAgentData.portal.let { put(ExtraParam.UAD_PORTAL, it) }
            userAgentData.deviceType.let { put(ExtraParam.UAD_DEVICE_TYPE, it) }
            userAgentData.application.let { put(ExtraParam.UAD_APPLICATION, it) }
            userAgentData.player.let { put(ExtraParam.UAD_PLAYER, it) }
            userAgentData.build.let { put(ExtraParam.UAD_BUILD, it.toString()) }
            userAgentData.os.let { put(ExtraParam.UAD_OS, it) }
            userAgentData.osInfo.let { put(ExtraParam.UAD_OS_INFO, it) }
        }
        val params = map.map { "${it.key}=${GemiusPrismUtils.specialEscape(it.value.toString())}" }.joinToString("|")
        return params
    }

    private fun UserType.convertToString(): String {
        return when (this) {
            UserType.UNLOGGED -> "none"
            UserType.NATIVE -> "native"
            UserType.FACEBOOK -> "fb"
            UserType.ICOK -> "icok"
            UserType.APPLE -> "apple"
            UserType.GOOGLE -> "google"
            UserType.PLUS -> "plus"
            UserType.NETIA -> "netia"
            UserType.SSO -> "sso"
        }
    }
}