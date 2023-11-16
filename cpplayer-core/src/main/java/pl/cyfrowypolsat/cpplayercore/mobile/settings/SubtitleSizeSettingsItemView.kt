package pl.cyfrowypolsat.cpplayercore.mobile.settings

import android.content.Context
import android.util.AttributeSet
import pl.cyfrowypolsat.cpplayercore.core.subtitlesettings.textsize.SubtitleSizeSettingsItem


class SubtitleSizeSettingsItemView
@JvmOverloads constructor(context: Context,
                          attrs: AttributeSet? = null,
                          defStyleAttr: Int = 0) : SubtitleSettingsItemView(context, attrs, defStyleAttr) {

    init {
        setupAsSelection()
    }

    var subtitleSizeSettingsItem: SubtitleSizeSettingsItem? = null
        set(value) {
            field = value
            value?.let {
                binding.subtitleSettingsItemText.text = it.name
            }
        }
}