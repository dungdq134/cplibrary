package pl.cyfrowypolsat.cpplayercore.core.sharedprefs

import android.content.Context
import androidx.core.content.edit
import pl.cyfrowypolsat.cpplayercore.BuildConfig

class PlayerSettingsSharedPrefs(context: Context) {

    companion object {
        private const val PREFS_NAME = BuildConfig.LIBRARY_PACKAGE_NAME + ".sharedPreferences"

        private const val PREF_AUDIO_LANGUAGE = "PREF_AUDIO_LANGUAGE"
        private const val PREF_SUBTITLE_LANGUAGE = "PREF_SUBTITLE_LANGUAGE"
        private const val PREF_SUBTITLE_TEXT_SIZE = "PREF_SUBTITLE_TEXT_SIZE"
        private const val PREF_SUBTITLE_HIGH_CONTRAST = "PREF_SUBTITLE_HIGH_CONTRAST"
        private const val PREF_TERAVOLT_OVERLAY_SETTINGS = "PREF_TERAVOLT_OVERLAY_SETTINGS"
    }

    private val sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    var audioLanguage: String?
        get() = sharedPref.getString(PREF_AUDIO_LANGUAGE, null)
        set(value) = sharedPref.edit(commit = true) {
            putString(PREF_AUDIO_LANGUAGE, value)
        }

    var subtitleLanguage: String?
        get() = sharedPref.getString(PREF_SUBTITLE_LANGUAGE, null)
        set(value) = sharedPref.edit(commit = true) {
            putString(PREF_SUBTITLE_LANGUAGE, value)
        }

    var subtitleTextSizeType: Int?
        get() = sharedPref.getString(PREF_SUBTITLE_TEXT_SIZE, null)?.toIntOrNull()
        set(value) = sharedPref.edit(commit = true) {
            putString(PREF_SUBTITLE_TEXT_SIZE, value.toString())
        }

    var subtitleHighContrastOn: Boolean
        get() = sharedPref.getBoolean(PREF_SUBTITLE_HIGH_CONTRAST, false)
        set(value) = sharedPref.edit(commit = true) {
            putBoolean(PREF_SUBTITLE_HIGH_CONTRAST, value)
        }

    var teravoltOverlaySettings: String?
        get() = sharedPref.getString(PREF_TERAVOLT_OVERLAY_SETTINGS, null)
        set(value) = sharedPref.edit(commit = true) {
            putString(PREF_TERAVOLT_OVERLAY_SETTINGS, value)
        }
}