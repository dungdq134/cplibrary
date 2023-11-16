package pl.cyfrowypolsat.cpplayercore.tv.settings

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.RelativeLayout
import pl.cyfrowypolsat.cpcommon.presentation.extensions.gone
import pl.cyfrowypolsat.cpcommon.presentation.extensions.visible
import pl.cyfrowypolsat.cpplayercore.R
import pl.cyfrowypolsat.cpplayercore.databinding.CpplCrTvViewSubtitleSettingsItemBinding


open class TvSubtitleSettingsItemView
@JvmOverloads constructor(context: Context,
                          attrs: AttributeSet? = null,
                          defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    val binding = CpplCrTvViewSubtitleSettingsItemBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        setBackgroundResource(R.drawable.cppl_cr_tv_track_selection_item_ripple)
        isFocusable = true
        isClickable = true
    }

    fun setupAsSwitch() {
        binding.tvSubtitleSettingsIndicator.gone()
        binding.tvSubtitleSettingsItemSwitch.visible()
        (binding.tvSubtitleSettingsItemText.layoutParams as RelativeLayout.LayoutParams).addRule(RelativeLayout.START_OF, binding.tvSubtitleSettingsItemSwitch.id)
    }

    fun setupAsSelection() {
        binding.tvSubtitleSettingsItemSwitch.gone()
        (binding.tvSubtitleSettingsItemText.layoutParams as RelativeLayout.LayoutParams).addRule(RelativeLayout.START_OF, binding.tvSubtitleSettingsIndicator.id)
    }
}