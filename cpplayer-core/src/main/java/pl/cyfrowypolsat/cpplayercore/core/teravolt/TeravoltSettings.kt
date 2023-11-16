package pl.cyfrowypolsat.cpplayercore.core.teravolt

import com.teravolt.mobile.tvx_video_plugin.models.TvxFeatures
import com.teravolt.mobile.tvx_video_plugin.models.TvxLogLevel
import com.teravolt.mobile.tvx_video_plugin.models.TvxSettingsInfo
import pl.cyfrowypolsat.cpplayercore.BuildConfig

// Setting this to true enables the overlay mode of the SDK
const val renderAsOverlay = true

val tvxSettings = TvxSettingsInfo(
        takerId = "cisQ7P",
        languageCountryCode = "PL",
        halfTimeBreakFraction = null,
        preAndPostFraction = null,
        alwaysUseSingleMatch = false,
        logLevel = if(BuildConfig.DEBUG) TvxLogLevel.Debug else TvxLogLevel.None,
        usesDrm = true,
        useAsOverlay = renderAsOverlay,
        useStaging = false,  //TODO change to false before release
        matchDay = "33",
        matchEventDelay = null,
        features = TvxFeatures(showRecordButton = false,
                showBackButton = true,
                showOnboarding = false,
                menuMatches = true,
                menuAlerts = true)
)
