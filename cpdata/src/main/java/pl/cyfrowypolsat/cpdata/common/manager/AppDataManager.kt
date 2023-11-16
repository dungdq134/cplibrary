package pl.cyfrowypolsat.cpdata.common.manager

import pl.cyfrowypolsat.cpdata.CpData
import pl.cyfrowypolsat.cpdata.api.navigation.response.media.MediaIdResult
import pl.cyfrowypolsat.cpdata.local.SharedPrefs
import javax.inject.Inject

class AppDataManager
@Inject constructor() {

    @Inject lateinit var sharedPrefs: SharedPrefs

    init {
        CpData.getInstance().component.inject(this)
    }

    var isForceCodeLoginMode = false

    var uiModeType: String? = sharedPrefs.uiModeType
        get() = sharedPrefs.uiModeType
        set(value) {
            sharedPrefs.uiModeType = value
            field = value
        }

    var isAppInitialized: Boolean = sharedPrefs.isAppInitialized
        get() = sharedPrefs.isAppInitialized
        set(value) {
            sharedPrefs.isAppInitialized = value
            field = value
        }

    var isIntroShown: Boolean = sharedPrefs.isIntroShown
        get() = sharedPrefs.isIntroShown
        set(value) {
            sharedPrefs.isIntroShown = value
            field = value
        }

    var isUiModeShown: Boolean = sharedPrefs.isUiModeShown
        get() = sharedPrefs.isUiModeShown
        set(value) {
            sharedPrefs.isUiModeShown = value
            field = value
        }

    var clientId: String = sharedPrefs.clientId
        get() = sharedPrefs.clientId
        set(value) {
            sharedPrefs.clientId = value
            field = value
        }

    var clientContextToken: String = sharedPrefs.clientContextToken
        get() = sharedPrefs.clientContextToken
        set(value) {
            sharedPrefs.clientContextToken = value
            field = value
        }

    var sessionUpdateTime: Long = sharedPrefs.sessionUpdateTime
        get() = sharedPrefs.sessionUpdateTime
        set(value) {
            sharedPrefs.sessionUpdateTime = value
            field = value
        }

    var lastUpdateStatusResponseStatus: Int = sharedPrefs.lastUpdateStatusResponseStatus
        get() = sharedPrefs.lastUpdateStatusResponseStatus
        set(value) {
            sharedPrefs.lastUpdateStatusResponseStatus = value
            field = value
        }

    var lastUpdateStatusResponseUserMessage: String? = sharedPrefs.lastUpdateStatusResponseUserMessage
        get() = sharedPrefs.lastUpdateStatusResponseUserMessage
        set(value) {
            sharedPrefs.lastUpdateStatusResponseUserMessage = value
            field = value
        }


    var ignoreUnstableTvEnvironment: Boolean = sharedPrefs.ignoreUnstableTvEnvironment
        get() = sharedPrefs.ignoreUnstableTvEnvironment
        set(value) {
            sharedPrefs.ignoreUnstableTvEnvironment = value
            field = value
        }

    var downloadOwnerUserId: Int = sharedPrefs.downloadOwnerUserId
        get() = sharedPrefs.downloadOwnerUserId
        set(value) {
            sharedPrefs.downloadOwnerUserId = value
            field = value
        }

    var onlyWifiPlayer: Boolean = sharedPrefs.onlyWifiPlayer
        get() = sharedPrefs.onlyWifiPlayer
        set(value) {
            sharedPrefs.onlyWifiPlayer = value
            field = value
        }

    var onlyWifiDownload: Boolean = sharedPrefs.onlyWifiDownload
        get() = sharedPrefs.onlyWifiDownload
        set(value) {
            sharedPrefs.onlyWifiDownload = value
            field = value
        }

    var downloadQuality: Int = sharedPrefs.downloadQuality
        get() = sharedPrefs.downloadQuality
        set(value) {
            sharedPrefs.downloadQuality = value
            field = value
        }

    var playerQuality: Int = sharedPrefs.playerQuality
        get() = sharedPrefs.playerQuality
        set(value) {
            sharedPrefs.playerQuality = value
            field = value
        }

    var pushNotificationsEnabled: Boolean = sharedPrefs.pushNotificationsEnabled
        get() = sharedPrefs.pushNotificationsEnabled
        set(value) {
            sharedPrefs.pushNotificationsEnabled = value
            field = value
        }

    var pushAgreementReminderEnabled: Boolean = sharedPrefs.pushAgreementReminderEnabled
        get() = sharedPrefs.pushAgreementReminderEnabled
        set(value) {
            sharedPrefs.pushAgreementReminderEnabled = value
            field = value
        }

    var pushAgreementReminderCounter: Int = sharedPrefs.pushAgreementReminderCounter
        get() = sharedPrefs.pushAgreementReminderCounter
        set(value) {
            sharedPrefs.pushAgreementReminderCounter = value
            field = value
        }

    var isCrashFree: Boolean = sharedPrefs.isCrashFree
        get() = sharedPrefs.isCrashFree
        set(value) {
            sharedPrefs.isCrashFree = value
            field = value
        }

    var userClickedRateApp: Boolean = sharedPrefs.userClickedRateApp
        get() = sharedPrefs.userClickedRateApp
        set(value) {
            sharedPrefs.userClickedRateApp = value
            field = value
        }

    var rateAppPromptCounter: Int = sharedPrefs.rateAppPromptCounter
        get() = sharedPrefs.rateAppPromptCounter
        set(value) {
            sharedPrefs.rateAppPromptCounter = value
            field = value
        }

    var rateAppPromptEnabled: Boolean = sharedPrefs.rateAppPromptEnabled
        get() = sharedPrefs.rateAppPromptEnabled
        set(value) {
            sharedPrefs.rateAppPromptEnabled = value
            field = value
        }

    var rateAppLastPromptTime: Long = sharedPrefs.rateAppLastPromptTime
        get() = sharedPrefs.rateAppLastPromptTime
        set(value) {
            sharedPrefs.rateAppLastPromptTime = value
            field = value
        }

    var playerLastMediaId: MediaIdResult? = sharedPrefs.playerLastMediaId
        get() = sharedPrefs.playerLastMediaId
        set(value) {
            sharedPrefs.playerLastMediaId = value
            field = value
        }

    var maintenanceModeDisabled: Boolean = sharedPrefs.maintenanceModeDisabled
        get() = sharedPrefs.maintenanceModeDisabled
        set(value) {
            sharedPrefs.maintenanceModeDisabled = value
            field = value
        }


    fun clearSharedPrefs() {
        sharedPrefs.clear()
    }
}