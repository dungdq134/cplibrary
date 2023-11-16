package pl.cyfrowypolsat.cpstats.application.cpactivityevents

import pl.cyfrowypolsat.cpstats.application.ApplicationEvent
import pl.cyfrowypolsat.cpstats.application.ApplicationItemClickEvent
import pl.cyfrowypolsat.cpstats.application.ApplicationLoginErrorEvent
import pl.cyfrowypolsat.cpstats.application.ApplicationLoginEvent
import pl.cyfrowypolsat.cpstats.application.ApplicationLogoutEvent
import pl.cyfrowypolsat.cpstats.application.ApplicationNavigationErrorEvent
import pl.cyfrowypolsat.cpstats.application.ApplicationNavigationEvent
import pl.cyfrowypolsat.cpstats.application.ApplicationPauseEvent
import pl.cyfrowypolsat.cpstats.application.ApplicationRateAppActionEvent
import pl.cyfrowypolsat.cpstats.application.ApplicationResumeEvent
import pl.cyfrowypolsat.cpstats.application.ApplicationStartEvent
import pl.cyfrowypolsat.cpstats.core.model.AccountData
import pl.cyfrowypolsat.cpstats.core.model.ApplicationData
import pl.cyfrowypolsat.cpstats.core.model.DeviceData
import pl.cyfrowypolsat.cpstats.core.model.DeviceType
import pl.cyfrowypolsat.cpstats.core.model.IpData
import pl.cyfrowypolsat.cpstats.core.model.ItemData
import pl.cyfrowypolsat.cpstats.core.model.ListData
import pl.cyfrowypolsat.cpstats.core.model.PlaceData
import pl.cyfrowypolsat.cpstats.core.model.RateAppAction
import pl.cyfrowypolsat.cpstats.core.model.UserAgentData
import pl.cyfrowypolsat.cpstats.core.model.UserType

internal class CPActivityEventsMapper(private val config: CPActivityEventsConfig) {
    var authToken = config.authToken

    fun map(event: ApplicationEvent): CPActivityEventsHit? {
        return when (event) {
            is ApplicationLoginEvent -> buildUserLoggedHit(event)
            is ApplicationLoginErrorEvent -> buildUserLoggedErrorHit(event)
            is ApplicationNavigationEvent -> buildUserNavigatedHit(event)
            is ApplicationNavigationErrorEvent -> buildUserNavigatedErrorHit(event)
            is ApplicationLogoutEvent -> buildUserLoggedOutHit(event)
            is ApplicationStartEvent -> buildAppStartedHit(event)
            is ApplicationPauseEvent -> buildAppPausedHit(event)
            is ApplicationResumeEvent -> buildAppResumedHit(event)
            is ApplicationItemClickEvent -> buildAppUserItemClickedHit(event)
            is ApplicationRateAppActionEvent -> buildAppUserRateActionDoneHit(event)
            else -> null
        }
    }

    private fun buildUserLoggedHit(event: ApplicationLoginEvent): CPActivityEventsHit {

        val data = buildData(CPActivityEventsHit.Status.SUCCESS, event.applicationData)
        data.account = buildAccount(event.accountData)
        return buildHit(CPActivityEventsHit.EventType.LOGGED, event, data)
    }

    private fun buildUserLoggedErrorHit(event: ApplicationLoginErrorEvent): CPActivityEventsHit {

        val data = buildData(CPActivityEventsHit.Status.FAILED, event.applicationData, event.errorData.errorCode)
        data.account = buildAccount(event.userType)
        return buildHit(CPActivityEventsHit.EventType.LOGGED, event, data)
    }

    private fun buildUserNavigatedHit(event: ApplicationNavigationEvent,
                                      status: CPActivityEventsHit.Status = CPActivityEventsHit.Status.SUCCESS,
                                      errorCode: String? = null): CPActivityEventsHit {

        val data = buildData(status, event.applicationData, errorCode)
        data.place = buildPlace(event.placeData)
        return buildHit(CPActivityEventsHit.EventType.NAVIGATED, event, data)
    }

    private fun buildUserNavigatedErrorHit(event: ApplicationNavigationErrorEvent): CPActivityEventsHit {

        val data = buildData(CPActivityEventsHit.Status.FAILED, event.applicationData, event.errorData.errorCode)
        data.place = buildPlace(event.placeData)
        return buildHit(CPActivityEventsHit.EventType.NAVIGATED, event, data)
    }

    private fun buildUserLoggedOutHit(event: ApplicationLogoutEvent): CPActivityEventsHit {
        val data = buildData(CPActivityEventsHit.Status.SUCCESS, event.applicationData)
        return buildHit(CPActivityEventsHit.EventType.LOGGED_OUT, event, data)
    }

    private fun buildAppStartedHit(event: ApplicationStartEvent): CPActivityEventsHit {
        val data = buildData(CPActivityEventsHit.Status.SUCCESS, event.applicationData)
        data.launchCount = event.applicationData.launchCount
        data.autoStart = event.applicationData.autoStart
        return buildHit(CPActivityEventsHit.EventType.APP_STARTED, event, data)
    }

    private fun buildAppPausedHit(event: ApplicationPauseEvent): CPActivityEventsHit {
        val data = buildData(CPActivityEventsHit.Status.SUCCESS, event.applicationData)
        data.place = buildPlace(event.applicationData.currentPlaceData)
        data.sessionDurationSeconds = event.applicationData.sessionDurationSeconds
        return buildHit(CPActivityEventsHit.EventType.APP_PAUSED, event, data)
    }

    private fun buildAppResumedHit(event: ApplicationResumeEvent): CPActivityEventsHit {
        val data = buildData(CPActivityEventsHit.Status.SUCCESS, event.applicationData)
        return buildHit(CPActivityEventsHit.EventType.APP_RESUMED, event, data)
    }

    private fun buildAppUserItemClickedHit(event: ApplicationItemClickEvent): CPActivityEventsHit {
        val data = buildData(CPActivityEventsHit.Status.SUCCESS, event.applicationData)
        data.place = buildPlace(event.placeData)
        data.contentItem = buildItem(event.itemData)
        data.list = buildList(event.listData)
        return buildHit(CPActivityEventsHit.EventType.ITEM_CLICKED, event, data)
    }

    private fun buildAppUserRateActionDoneHit(event: ApplicationRateAppActionEvent): CPActivityEventsHit {
        val data = buildData(CPActivityEventsHit.Status.SUCCESS, event.applicationData)
        data.rateAppActionType = event.rateAppAction.convertToRateAppActionType()
        return buildHit(CPActivityEventsHit.EventType.RATE_APP_ACTION_DONE, event, data)
    }

    private fun buildData(status: CPActivityEventsHit.Status,
                          applicationData: ApplicationData,
                          errorCode: String? = null): CPActivityEventsHit.Data {
        val data = CPActivityEventsHit.Data(
                userAgentData = buildUserAgentData(applicationData.userAgentData),
                status = status,
                ipData = buildIpData(applicationData.ipData),
                deviceExtraData = buildDeviceData(applicationData.deviceData),
                clientId = applicationData.userData.clientId,
                deviceId = buildDeviceId(applicationData.deviceData),
                errorCode = errorCode,
                profileId = applicationData.userData.profileId,
                account = null,
                place = null,
                contentItem = null,
                list = null,
                sessionDurationSeconds = null,
                launchCount = null,
                autoStart = null,
                rateAppActionType = null
        )

        return data
    }

    private fun buildHit(type: CPActivityEventsHit.EventType,
                         event: ApplicationEvent,
                         data: CPActivityEventsHit.Data): CPActivityEventsHit {

        return CPActivityEventsHit(
                jwt = authToken,
                portal = event.applicationData.portalId,
                originator = config.originator,
                apiVersion = config.serviceVersion,
                eventDate = event.eventDate,
                eventType = type,
                traceId = event.applicationData.traceId,
                data = data,
                eventId = event.eventId
        )

    }

    private fun buildIpData(ipData: IpData): CPActivityEventsHit.IpData {
        return CPActivityEventsHit.IpData(
                ip = ipData.ip,
                country = ipData.country,
                isEu = ipData.isEu,
                isVpn = ipData.isVpn,
                continent = ipData.continent,
                isp = ipData.isp
        )
    }

    private fun buildUserAgentData(userAgentData: UserAgentData): CPActivityEventsHit.UserAgentData {
        return CPActivityEventsHit.UserAgentData(
                application = userAgentData.application,
                build = userAgentData.build,
                deviceType = userAgentData.deviceType,
                os = userAgentData.os,
                osInfo = userAgentData.osInfo,
                player = userAgentData.player,
                portal = userAgentData.portal,
                widevine = userAgentData.widevine
        )
    }

    private fun buildDeviceData(deviceData: DeviceData): CPActivityEventsHit.DeviceExtraData {
        val screenSize = when (deviceData.deviceType) {
            DeviceType.PHONE -> CPActivityEventsHit.ScreenSize(deviceData.screenHeight, deviceData.screenWidth, deviceData.screenDiagonal)
            else -> null
        }

        return CPActivityEventsHit.DeviceExtraData(
                manufacturer = deviceData.manufacturer,
                model = deviceData.model,
                screenSize = screenSize
        )
    }

    private fun buildDeviceId(deviceData: DeviceData): CPActivityEventsHit.DeviceId {
        return CPActivityEventsHit.DeviceId(type = deviceData.deviceIdType, value = deviceData.deviceIdValue)
    }

    private fun buildPlace(placeData: PlaceData): CPActivityEventsHit.Place {
        return CPActivityEventsHit.Place(
                type = placeData.type,
                value = placeData.value ?: ""
        )
    }

    private fun buildItem(itemData: ItemData): CPActivityEventsHit.ContentItem? {
        return CPActivityEventsHit.ContentItem(
                value = itemData.activityEventsContentItem?.value ?: "",
                type = itemData.activityEventsContentItem?.type ?: "",
                position = itemData.activityEventsContentItem?.position)
    }

    private fun buildList(listData: ListData): CPActivityEventsHit.List? {
        return CPActivityEventsHit.List(
                value = listData.activityEventsList?.value,
                type = listData.activityEventsList?.type ?: "",
                position = listData.activityEventsList?.position)
    }

    private fun buildAccount(accountData: AccountData): CPActivityEventsHit.Account {
        return CPActivityEventsHit.Account(
                provider = accountData.userType.convertToProviderType(),
                facebookId = accountData.facebookId,
                login = accountData.login,
                msisdn = accountData.msisdn,
                ssoExternalAccountId = accountData.ssoExternalAccountId
        )
    }

    private fun buildAccount(userType: UserType): CPActivityEventsHit.Account {
        return CPActivityEventsHit.Account(
                provider = userType.convertToProviderType(),
                facebookId = null,
                login = null,
                msisdn = null,
                ssoExternalAccountId = null
        )
    }

    private fun RateAppAction.convertToRateAppActionType(): CPActivityEventsHit.RateAppActionType {
        return when (this){
            RateAppAction.DISLIKE -> CPActivityEventsHit.RateAppActionType.DISLIKE_APP
            RateAppAction.LIKE -> CPActivityEventsHit.RateAppActionType.LIKE_APP
            RateAppAction.RATE -> CPActivityEventsHit.RateAppActionType.RATE_APP
            RateAppAction.REMIND_LATER -> CPActivityEventsHit.RateAppActionType.REMIND_LATER
            RateAppAction.SEND_EMAIL -> CPActivityEventsHit.RateAppActionType.SEND_EMAIL
        }
    }

    private fun UserType.convertToProviderType(): CPActivityEventsHit.ProviderType {
        return when (this) {
            UserType.FACEBOOK -> CPActivityEventsHit.ProviderType.FACEBOOK
            UserType.NATIVE -> CPActivityEventsHit.ProviderType.NATIVE
            UserType.ICOK -> CPActivityEventsHit.ProviderType.ICOK
            UserType.APPLE -> CPActivityEventsHit.ProviderType.APPLE
            UserType.GOOGLE -> CPActivityEventsHit.ProviderType.GOOGLE
            UserType.UNLOGGED -> CPActivityEventsHit.ProviderType.NATIVE
            UserType.PLUS -> CPActivityEventsHit.ProviderType.PLUS
            UserType.NETIA -> CPActivityEventsHit.ProviderType.NETIA
            UserType.SSO -> CPActivityEventsHit.ProviderType.SSO
        }
    }
}
