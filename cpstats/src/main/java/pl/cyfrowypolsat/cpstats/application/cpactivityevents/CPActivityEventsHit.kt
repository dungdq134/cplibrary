package pl.cyfrowypolsat.cpstats.application.cpactivityevents

import com.google.gson.annotations.SerializedName
import java.util.*

data class CPActivityEventsHit(@SerializedName("data") val data: Data?,
                               @SerializedName("jwt") val jwt: String,
                               @SerializedName("portalId") val portal: String,
                               @SerializedName("originator") val originator: String,
                               @SerializedName("version") val apiVersion: String,
                               @SerializedName("eventDate") val eventDate: Date,
                               @SerializedName("type") val eventType: EventType,
                               @SerializedName("eventId") val eventId: String,
                               @SerializedName("traceId") val traceId: String) {

    enum class EventType {
        @SerializedName("AppUserLogged")
        LOGGED,

        @SerializedName("AppUserNavigated")
        NAVIGATED,

        @SerializedName("AppUserLoggedOut")
        LOGGED_OUT,

        @SerializedName("AppStarted")
        APP_STARTED,

        @SerializedName("AppPaused")
        APP_PAUSED,

        @SerializedName("AppResumed")
        APP_RESUMED,

        @SerializedName("AppUserItemClicked")
        ITEM_CLICKED,

        @SerializedName("AppUserRateAppActionDone")
        RATE_APP_ACTION_DONE,
    }

    data class Data(@SerializedName("userAgentData") val userAgentData: UserAgentData,
                    @SerializedName("ipData") val ipData: IpData,
                    @SerializedName("status") val status: Status,
                    @SerializedName("errorCode") val errorCode: String?,
                    @SerializedName("deviceExtraData") val deviceExtraData: DeviceExtraData,
                    @SerializedName("clientId") val clientId: String,
                    @SerializedName("deviceId") val deviceId: DeviceId,
                    @SerializedName("profileId") val profileId: String?,
                    @SerializedName("account") var account: Account?,
                    @SerializedName("place") var place: Place?,
                    @SerializedName("contentItem") var contentItem: ContentItem?,
                    @SerializedName("list") var list: List?,
                    @SerializedName("sessionDuration") var sessionDurationSeconds: Int?,
                    @SerializedName("launchCount") var launchCount: Int?,
                    @SerializedName("autoStart") var autoStart: Boolean?,
                    @SerializedName("rateAppActionType") var rateAppActionType: RateAppActionType?)

    data class UserAgentData(@SerializedName("portal") val portal: String,
                             @SerializedName("deviceType") val deviceType: String,
                             @SerializedName("application") val application: String,
                             @SerializedName("player") val player: String,
                             @SerializedName("build") val build: Int,
                             @SerializedName("os") val os: String,
                             @SerializedName("osInfo") val osInfo: String,
                             @SerializedName("widevine") val widevine: Boolean)

    data class IpData(@SerializedName("ip") val ip: String,
                      @SerializedName("country") val country: String?,
                      @SerializedName("isEu") val isEu: Boolean?,
                      @SerializedName("isVpn") val isVpn: Boolean?,
                      @SerializedName("isp") val isp: String?,
                      @SerializedName("continent") val continent: String?)

    enum class Status {

        @SerializedName("success")
        SUCCESS,

        @SerializedName("failed")
        FAILED

    }

    data class DeviceExtraData(@SerializedName("manufacturer") val manufacturer: String,
                               @SerializedName("model") val model: String,
                               @SerializedName("screenSize") val screenSize: ScreenSize?)

    data class ScreenSize(@SerializedName("height") val height: Int,
                          @SerializedName("width") val width: Int,
                          @SerializedName("diagonal") val diagonal: Double)

    data class Place(@SerializedName("type") val type: String,
                     @SerializedName("value") val value: String)

    data class ContentItem(@SerializedName("type") val type: String,
                           @SerializedName("value") val value: String,
                           @SerializedName("position") val position: Int?)

    data class List(@SerializedName("type") val type: String,
                    @SerializedName("value") val value: String?,
                    @SerializedName("position") val position: Int?)

    data class Account(@SerializedName("provider") val provider: ProviderType,
                       @SerializedName("fbId") val facebookId: String?,
                       @SerializedName("login") val login: String?,
                       @SerializedName("msisdn") val msisdn: String?,
                       @SerializedName("ssoExternalAccountId") val ssoExternalAccountId: String?)

    data class DeviceId(@SerializedName("type") val type: String,
                        @SerializedName("value") val value: String)

    enum class ProviderType {
        @SerializedName("native")
        NATIVE,

        @SerializedName("facebook")
        FACEBOOK,

        @SerializedName("icok")
        ICOK,

        @SerializedName("plus")
        PLUS,

        @SerializedName("netia")
        NETIA,

        @SerializedName("apple")
        APPLE,

        @SerializedName("google")
        GOOGLE,

        @SerializedName("sso")
        SSO,
    }

    enum class RateAppActionType {
        @SerializedName("dislike_app")
        DISLIKE_APP,

        @SerializedName("like_app")
        LIKE_APP,

        @SerializedName("rate_app")
        RATE_APP,

        @SerializedName("remind_later")
        REMIND_LATER,

        @SerializedName("send_email")
        SEND_EMAIL;
    }
}