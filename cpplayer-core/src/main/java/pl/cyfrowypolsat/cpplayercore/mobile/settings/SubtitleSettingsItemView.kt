package pl.cyfrowypolsat.cpplayercore.mobile.settings

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import pl.cyfrowypolsat.cpcommon.presentation.extensions.gone
import pl.cyfrowypolsat.cpcommon.presentation.extensions.visible
import pl.cyfrowypolsat.cpplayercore.R
import pl.cyfrowypolsat.cpplayercore.databinding.CpplCrMobileViewSubtitlesSettingsItemBinding

open class SubtitleSettingsItemView
@JvmOverloads constructor(context: Context,
                          attrs: AttributeSet? = null,
                          defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    val binding = CpplCrMobileViewSubtitlesSettingsItemBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        isFocusable = true
        isClickable = true
    }

    fun selectItem(select: Boolean) {
        if (select) {
            binding.subtitleSettingsItemText.setTextColor(ContextCompat.getColor(context, R.color.cppl_cr_color_primary))
        } else {
            binding.subtitleSettingsItemText.setTextColor(ContextCompat.getColor(context, R.color.cppl_cr_grey_a9aeb4))
        }
    }

    fun setupAsSwitch() {
        binding.subtitleSettingsItemSwitch.visible()
    }

    fun setupAsSelection() {
        binding.subtitleSettingsItemSwitch.gone()
    }

}