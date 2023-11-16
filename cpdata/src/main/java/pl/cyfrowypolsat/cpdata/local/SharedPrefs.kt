package pl.cyfrowypolsat.cpdata.local

import android.content.Context
import androidx.core.content.edit
import com.google.gson.Gson
import pl.cyfrowypolsat.cpdata.BuildConfig
import pl.cyfrowypolsat.cpdata.api.auth.request.login.LoginAuthData
import pl.cyfrowypolsat.cpdata.api.auth.response.ProfileResult
import pl.cyfrowypolsat.cpdata.api.common.model.AuthResult
import pl.cyfrowypolsat.cpdata.api.navigation.response.homemenu.HomeMenuItemResult
import pl.cyfrowypolsat.cpdata.api.navigation.response.media.MediaIdResult
import pl.cyfrowypolsat.cpdata.api.system.response.AvatarResult
import pl.cyfrowypolsat.cpdata.api.system.response.ConfigurationResponse
import pl.cyfrowypolsat.cpdata.common.extensions.fromJson
import pl.cyfrowypolsat.cpdata.di.CpDataQualifier
import pl.cyfrowypolsat.cpdata.local.billing.BillingOrder
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SharedPrefs
@Inject constructor(@CpDataQualifier private val gson: Gson,
                    private val context: Context) {

    companion object {
        const val PREFS_NAME = BuildConfig.LIBRARY_PACKAGE_NAME + ".sharedPreferences"
        const val PREF_CONFIGURATION = "PREF_CONFIGURATION"
        const val PREF_CLIENT_ID = "PREF_CLIENT_ID"
        const val PREF_CLIENT_CONTEXT_TOKEN = "PREF_CLIENT_CONTEXT_TOKEN"
        const val PREF_SESSION_UPDATE_TIME = "PREF_SESSION_UPDATE_TIME"
        const val PREF_CONFIGURATION_TIME = "PREF_CONFIGURATION_TIME"
        const val PREF_DEVICE_TIME = "PREF_DEVICE_TIME"
        const val PREF_USER_AGENT = "PREF_USER_AGENT"
        const val PREF_IS_APP_INITIALIZED = "PREF_IS_APP_INITIALIZED"
        const val PREF_IS_INTRO_SHOWN = "PREF_IS_INTRO_SHOWN"
        const val PREF_IS_UI_MODE_SHOWN = "PREF_IS_UI_MODE_SHOWN"
        const val PREF_LAUNCH_COUNT = "PREF_LAUNCH_COUNT"
        const val PREF_HOME_MENU = "PREF_HOME_MENU"
        const val PREF_AVATARS = "PREF_AVATARS"

        // Settings
        const val PREF_UI_MODE_TYPE = "PREF_UI_MODE_TYPE"

        // Auth
        const val PREF_AUTH_RESULT = "PREF_AUTH_RESULT"
        const val PREF_LOGIN_AUTH_DATA = "PREF_LOGIN_AUTH_DATA"
        const val PREF_PROFILES_RESULT = "PREF_PROFILES_RESULT"
        const val PREF_SELECTED_PROFILE_ID = "PREF_SELECTED_PROFILE_ID"
        const val PREF_LAST_PROFILE_SELECT_TIME = "PREF_LAST_PROFILE_SELECT_TIME"
        const val PREF_CHIP_ID = "PREF_CHIP_ID"

        // Update info
        const val PREF_LAST_UPDATE_STATUS_RESPONSE_STATUS = "PREF_LAST_UPDATE_STATUS_RESPONSE_STATUS"
        const val PREF_LAST_UPDATE_STATUS_RESPONSE_USER_MESSAGE = "PREF_LAST_UPDATE_STATUS_RESPONSE_USER_MESSAGE"

        // Chromecast
        const val PREF_IGNORE_UNSTABLE_TV_ENVIRONMENT = "PREF_IGNORE_UNSTABLE_TV_ENVIRONMENT"

        // Download
        const val PREF_DOWNLOAD_OWNER_USER_ID = "PREF_DOWNLOAD_OWNER_ID"
        const val PREF_DOWNLOAD_QUALITY = "PREF_DOWNLOAD_QUALITY"

        // Player
        const val PREF_PLAYER_QUALITY = "PREF_PLAYER_QUALITY"

        // Only Wi-Fi
        const val PREF_ONLY_WIFI_PLAYER = "PREF_ONLY_WIFI_PLAYER"
        const val PREF_ONLY_WIFI_DOWNLOAD = "PREF_ONLY_WIFI_DOWNLOAD"

        // Push notifications
        const val PREF_PUSH_NOTIFICATIONS_ENABLED = "PREF_PUSH_NOTIFICATIONS_ENABLED"
        const val PREF_PUSH_NOTIFICATIONS_AGREEMENT_REMINDER_ENABLED = "PREF_PUSH_NOTIFICATIONS_AGREEMENT_REMINDER_ENABLED"
        const val PREF_PUSH_AGREEMENT_REMINDER_COUNTER = "PREF_PUSH_AGREEMENT_REMINDER_COUNTER"

        // Rate App
        const val PREF_RATE_APP_PROMPT_COUNTER = "PREF_RATE_APP_PROMPT_COUNTER"
        const val PREF_RATE_APP_PROMPT_ENABLED = "PREF_RATE_APP_PROMPT_ENABLED"
        const val PREF_RATE_APP_LAST_PROMPT_TIME = "PREF_RATE_APP_LAST_PROMPT_TIME"
        const val PREF_USER_CLICKED_RATE_APP = "PREF_USER_CLICKED_RATE_APP"
        const val PREF_IS_CRASH_FREE = "PREF_IS_CRASH_FREE"

        // Billing order
        const val PREF_BILLING_ORDER = "PREF_BILLING_ORDER"

        // Player
        const val PREF_PLAYER_LAST_MEDIA_ID = "PREF_PLAYER_LAST_MEDIA_ID"

        // Maintenance mode
        const val MAINTENANCE_MODE_DISABLED = "MAINTENANCE_MODE_DISABLED"
    }

    private val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var configuration: ConfigurationResponse?
        get() = gson.fromJson(sharedPref.getString(PREF_CONFIGURATION, null), ConfigurationResponse::class.java)
        set(value) = sharedPref.edit(commit = true) {
            putString(PREF_CONFIGURATION, gson.toJson(value))
        }

    var clientId: String
        get() = sharedPref.getString(PREF_CLIENT_ID, "") ?: ""
        set(value) = sharedPref.edit(commit = true) {
            putString(PREF_CLIENT_ID, value)
        }

    var clientContextToken: String
        get() = sharedPref.getString(PREF_CLIENT_CONTEXT_TOKEN, "") ?: ""
        set(value) = sharedPref.edit(commit = true) {
            putString(PREF_CLIENT_CONTEXT_TOKEN, value)
        }

    var sessionUpdateTime: Long
        get() = sharedPref.getLong(PREF_SESSION_UPDATE_TIME, -1)
        set(value) = sharedPref.edit(commit = true) {
            putLong(PREF_SESSION_UPDATE_TIME, value)
        }

    var configurationTime: Long?
        get() = sharedPref.getString(PREF_CONFIGURATION_TIME, null)?.toLongOrNull()
        set(value) = sharedPref.edit(commit = true) {
            putString(PREF_CONFIGURATION_TIME, value.toString())
        }

    var deviceTime: Long?
        get() = sharedPref.getString(PREF_DEVICE_TIME, null)?.toLongOrNull()
        set(value) = sharedPref.edit(commit = true) {
            putString(PREF_DEVICE_TIME, value.toString())
        }

    var userAgent: String?
        get() = sharedPref.getString(PREF_USER_AGENT, null)
        set(value) = sharedPref.edit(commit = true) {
            putString(PREF_USER_AGENT, value)
        }

    var isAppInitialized: Boolean
        get() = sharedPref.getBoolean(PREF_IS_APP_INITIALIZED, false)
        set(value) = sharedPref.edit(commit = true) {
            putBoolean(PREF_IS_APP_INITIALIZED, value)
        }

    var isIntroShown: Boolean
        get() = sharedPref.getBoolean(PREF_IS_INTRO_SHOWN, false)
        set(value) = sharedPref.edit(commit = true) {
            putBoolean(PREF_IS_INTRO_SHOWN, value)
        }

    var isUiModeShown: Boolean
        get() = sharedPref.getBoolean(PREF_IS_UI_MODE_SHOWN, false)
        set(value) = sharedPref.edit(commit = true) {
            putBoolean(PREF_IS_UI_MODE_SHOWN, value)
        }

    var launchCount: Int
        get() = sharedPref.getInt(PREF_LAUNCH_COUNT, 1)
        set(value) = sharedPref.edit(commit = true) {
            putInt(PREF_LAUNCH_COUNT, value)
        }

    var homeMenu: List<HomeMenuItemResult>?
        get() = gson.fromJson(sharedPref.getString(PREF_HOME_MENU, null))
        set(value) = sharedPref.edit(commit = true) {
            putString(PREF_HOME_MENU, gson.toJson(value))
        }

    var avatars: List<AvatarResult>?
        get() = gson.fromJson(sharedPref.getString(PREF_AVATARS, null))
        set(value) = sharedPref.edit(commit = true) {
            putString(PREF_AVATARS, gson.toJson(value))
        }


    // Settings
    var uiModeType: String?
        get() = sharedPref.getString(PREF_UI_MODE_TYPE, null)
        set(value) = sharedPref.edit(commit = true) {
            putString(PREF_UI_MODE_TYPE, value)
        }


    // Auth
    var authResult: AuthResult?
        get() = gson.fromJson(sharedPref.getString(PREF_AUTH_RESULT, null), AuthResult::class.java)
        set(value) = sharedPref.edit(commit = true) {
            putString(PREF_AUTH_RESULT, gson.toJson(value))
        }

    var loginAuthData: LoginAuthData?
        get() = gson.fromJson(sharedPref.getString(PREF_LOGIN_AUTH_DATA, null), LoginAuthData::class.java)
        set(value) = sharedPref.edit(commit = true) {
            putString(PREF_LOGIN_AUTH_DATA, gson.toJson(value))
        }

    var profilesResult: List<ProfileResult>?
        get() = gson.fromJson(sharedPref.getString(PREF_PROFILES_RESULT, null))
        set(value) = sharedPref.edit(commit = true) {
            putString(PREF_PROFILES_RESULT, gson.toJson(value))
        }

    var selectedProfileId: String?
        get() = sharedPref.getString(PREF_SELECTED_PROFILE_ID, null)
        set(value) = sharedPref.edit(commit = true) {
            putString(PREF_SELECTED_PROFILE_ID, value)
        }

    var lastProfileSelectTime: Long
        get() = sharedPref.getLong(PREF_LAST_PROFILE_SELECT_TIME, -1)
        set(value) = sharedPref.edit(commit = true) {
            putLong(PREF_LAST_PROFILE_SELECT_TIME, value)
        }
    var chipId: String?
        get() = sharedPref.getString(PREF_CHIP_ID, null)
        set(value) = sharedPref.edit(commit = true) {
            putString(PREF_CHIP_ID, value)
        }


    // Update info
    var lastUpdateStatusResponseStatus: Int
        get() = sharedPref.getInt(PREF_LAST_UPDATE_STATUS_RESPONSE_STATUS, -1)
        set(value) = sharedPref.edit(commit = true) {
            putInt(PREF_LAST_UPDATE_STATUS_RESPONSE_STATUS, value)
        }

    var lastUpdateStatusResponseUserMessage: String?
        get() = sharedPref.getString(PREF_LAST_UPDATE_STATUS_RESPONSE_USER_MESSAGE, "")
        set(value) = sharedPref.edit(commit = true) {
            putString(PREF_LAST_UPDATE_STATUS_RESPONSE_USER_MESSAGE, value)
        }

    //Chromecast
    var ignoreUnstableTvEnvironment: Boolean
        get() = sharedPref.getBoolean(PREF_IGNORE_UNSTABLE_TV_ENVIRONMENT, false)
        set(value) = sharedPref.edit(commit = true) {
            putBoolean(PREF_IGNORE_UNSTABLE_TV_ENVIRONMENT, value)
        }

    // Download
    var downloadOwnerUserId: Int
        get() = sharedPref.getInt(PREF_DOWNLOAD_OWNER_USER_ID, -1)
        set(value) = sharedPref.edit(commit = true) {
            putInt(PREF_DOWNLOAD_OWNER_USER_ID, value)
        }

    var downloadQuality: Int
        get() = sharedPref.getInt(PREF_DOWNLOAD_QUALITY, -1)
        set(value) = sharedPref.edit(commit = true) {
            putInt(PREF_DOWNLOAD_QUALITY, value)
        }

    // Player
    var playerQuality: Int
        get() = sharedPref.getInt(PREF_PLAYER_QUALITY, Int.MAX_VALUE)
        set(value) = sharedPref.edit(commit = true) {
            putInt(PREF_PLAYER_QUALITY, value)
        }

    // Only Wi-Fi
    var onlyWifiPlayer: Boolean
        get() = sharedPref.getBoolean(PREF_ONLY_WIFI_PLAYER, false)
        set(value) = sharedPref.edit(commit = true) {
            putBoolean(PREF_ONLY_WIFI_PLAYER, value)
        }

    var onlyWifiDownload: Boolean
        get() = sharedPref.getBoolean(PREF_ONLY_WIFI_DOWNLOAD, true)
        set(value) = sharedPref.edit(commit = true) {
            putBoolean(PREF_ONLY_WIFI_DOWNLOAD, value)
        }

    var pushNotificationsEnabled: Boolean
        get() = sharedPref.getBoolean(PREF_PUSH_NOTIFICATIONS_ENABLED, true)
        set(value) = sharedPref.edit(commit = true) {
            putBoolean(PREF_PUSH_NOTIFICATIONS_ENABLED, value)
        }

    var pushAgreementReminderEnabled: Boolean
        get() = sharedPref.getBoolean(PREF_PUSH_NOTIFICATIONS_AGREEMENT_REMINDER_ENABLED, true)
        set(value) = sharedPref.edit(commit = true) {
            putBoolean(PREF_PUSH_NOTIFICATIONS_AGREEMENT_REMINDER_ENABLED, value)
        }

    var pushAgreementReminderCounter: Int
        get() = sharedPref.getInt(PREF_PUSH_AGREEMENT_REMINDER_COUNTER, 0)
        set(value) = sharedPref.edit(commit = true) {
            putInt(PREF_PUSH_AGREEMENT_REMINDER_COUNTER, value)
        }

    var isCrashFree: Boolean
        get() = sharedPref.getBoolean(PREF_IS_CRASH_FREE, true)
        set(value) = sharedPref.edit(commit = true) {
            putBoolean(PREF_IS_CRASH_FREE, value)
        }

    var userClickedRateApp: Boolean
        get() = sharedPref.getBoolean(PREF_USER_CLICKED_RATE_APP, false)
        set(value) = sharedPref.edit(commit = true) {
            putBoolean(PREF_USER_CLICKED_RATE_APP, value)
        }

    var rateAppPromptCounter: Int
        get() = sharedPref.getInt(PREF_RATE_APP_PROMPT_COUNTER, 0)
        set(value) = sharedPref.edit(commit = true) {
            putInt(PREF_RATE_APP_PROMPT_COUNTER, value)
        }

    var rateAppPromptEnabled: Boolean
        get() = sharedPref.getBoolean(PREF_RATE_APP_PROMPT_ENABLED, true)
        set(value) = sharedPref.edit(commit = true) {
            putBoolean(PREF_RATE_APP_PROMPT_ENABLED, value)
        }

    var rateAppLastPromptTime: Long
        get() = sharedPref.getLong(PREF_RATE_APP_LAST_PROMPT_TIME, -1)
        set(value) = sharedPref.edit(commit = true) {
            putLong(PREF_RATE_APP_LAST_PROMPT_TIME, value)
        }


    // Billing order
    var billingOrder: BillingOrder?
        get() = gson.fromJson(sharedPref.getString(PREF_BILLING_ORDER, null))
        set(value) = sharedPref.edit(commit = true) {
            putString(PREF_BILLING_ORDER, gson.toJson(value))
        }


    var playerLastMediaId: MediaIdResult?
        get() = gson.fromJson(sharedPref.getString(PREF_PLAYER_LAST_MEDIA_ID, null), MediaIdResult::class.java)
        set(value) = sharedPref.edit(commit = true) {
            putString(PREF_PLAYER_LAST_MEDIA_ID, gson.toJson(value))
        }


    // Maintenance mode
    var maintenanceModeDisabled: Boolean
        get() = sharedPref.getBoolean(MAINTENANCE_MODE_DISABLED, false)
        set(value) = sharedPref.edit(commit = true) {
            putBoolean(MAINTENANCE_MODE_DISABLED, value)
        }


    fun clear() {
        sharedPref.edit().clear().commit()
    }
}


