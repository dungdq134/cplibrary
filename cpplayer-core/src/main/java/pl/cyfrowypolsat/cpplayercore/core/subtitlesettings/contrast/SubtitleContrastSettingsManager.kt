package pl.cyfrowypolsat.cpplayercore.core.subtitlesettings.contrast

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.media3.ui.CaptionStyleCompat
import pl.cyfrowypolsat.cpplayercore.R
import pl.cyfrowypolsat.cpplayercore.core.sharedprefs.PlayerSettingsSharedPrefs

class SubtitleContrastSettingsManager(val context: Context) {

    private val sharedPrefs = PlayerSettingsSharedPrefs(context)

    fun getSubtitleContrastSettingsItems(): List<SubtitleContrastSettingsItem> {
        return listOf(SubtitleContrastSettingsItem(isHighContrastSelected()))
    }

    fun subtitleContrastSettingsChanged(selected: Boolean) {
        sharedPrefs.subtitleHighContrastOn = selected
    }

    fun isHighContrastSelected(): Boolean {
        return sharedPrefs.subtitleHighContrastOn
    }

    fun getProperCaptionStyle(): CaptionStyleCompat {
        return if (sharedPrefs.subtitleHighContrastOn) {
            CaptionStyleCompat(ContextCompat.getColor(context, R.color.cppl_cr_high_contrast_subtitles_color), // foregroundColor
                Color.BLACK, // backgroundColor
                Color.TRANSPARENT, // windowColor
                CaptionStyleCompat.EDGE_TYPE_NONE, // edgeType
                Color.WHITE, // edgeColor
                null)
        } else {
            CaptionStyleCompat(Color.WHITE, // foregroundColor
                Color.TRANSPARENT, // backgroundColor
                Color.TRANSPARENT, // windowColor
                CaptionStyleCompat.EDGE_TYPE_OUTLINE, // edgeType
                Color.BLACK, // edgeColor
                null)
        }
    }
}