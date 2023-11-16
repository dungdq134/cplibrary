package pl.cyfrowypolsat.cpplayercore.mobile.settings

import android.content.Context
import android.util.AttributeSet
import pl.cyfrowypolsat.cpplayercore.R
import pl.cyfrowypolsat.cpplayercore.core.subtitlesettings.contrast.SubtitleContrastSettingsItem

class SubtitleContrastSettingsItemView
@JvmOverloads constructor(context: Context,
                          attrs: AttributeSet? = null,
                          defStyleAttr: Int = 0) : SubtitleSettingsItemView(context, attrs, defStyleAttr) {

    init {
        setupAsSwitch()
    }

    var subtitleContrastSettingsItem: SubtitleContrastSettingsItem? = null
        set(value) {
            field = value
            value?.let {
                setIsChecked(it.selected)
            }
        }

    fun setIsChecked(isChecked: Boolean) {
        binding.subtitleSettingsItemSwitch.isChecked = isChecked
        setProperName(isChecked)
    }

    private fun setProperName(isSelected: Boolean) {
        if (isSelected) {
            binding.subtitleSettingsItemText.text = context.getString(R.string.cppl_cr_subtitle_contrast_on)
        } else {
            binding.subtitleSettingsItemText.text = context.getString(R.string.cppl_cr_subtitle_contrast_off)
        }
    }
}