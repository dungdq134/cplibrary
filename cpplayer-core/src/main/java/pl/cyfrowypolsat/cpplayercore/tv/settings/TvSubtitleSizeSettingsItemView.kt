package pl.cyfrowypolsat.cpplayercore.tv.settings

import android.content.Context
import android.util.AttributeSet
import pl.cyfrowypolsat.cpplayercore.core.subtitlesettings.textsize.SubtitleSizeSettingsItem

class TvSubtitleSizeSettingsItemView
@JvmOverloads constructor(context: Context,
                          attrs: AttributeSet? = null,
                          defStyleAttr: Int = 0) : TvSubtitleSettingsItemView(context, attrs, defStyleAttr) {

    init {
        setupAsSelection()
    }

    var subtitleSizeSettingsItem: SubtitleSizeSettingsItem? = null
        set(value) {
            field = value
            value?.let {
                binding.tvSubtitleSettingsItemText.text = it.name
            }
        }

}