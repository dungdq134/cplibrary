package pl.cyfrowypolsat.cpplayercore.tv.settings

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import pl.cyfrowypolsat.cpplayercore.R
import pl.cyfrowypolsat.cpplayercore.databinding.CpplCrTvViewSubtitleSettingsBinding

class TvSubtitleSettingsView
@JvmOverloads constructor(context: Context,
                          attrs: AttributeSet? = null,
                          defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding = CpplCrTvViewSubtitleSettingsBinding.inflate(LayoutInflater.from(context), this, true)
    private var selectedSizeItem: TvSubtitleSizeSettingsItemView? = null

    fun setup(sizeViewItems: List<TvSubtitleSizeSettingsItemView>,
              contrastViewItems: List<TvSubtitleContrastSettingsItemView>) {
        binding.tvSubtitleSettingsSizeContainer.removeAllViews()
        binding.tvSubtitleSettingsContrastContainer.removeAllViews()
        sizeViewItems.forEach {
            binding.tvSubtitleSettingsSizeContainer.addView(it)
        }
        contrastViewItems.forEach {
            binding.tvSubtitleSettingsContrastContainer.addView(it)
        }
    }

    fun setSelectedItem(id: Int) {
        val newSelectedItem: View? = findViewById(id)
        newSelectedItem?.let {
            if (it is TvSubtitleSizeSettingsItemView) {
                selectedSizeItem?.findViewById<View>(R.id.tv_subtitle_settings_indicator)?.visibility = View.INVISIBLE
                selectedSizeItem = it
                selectedSizeItem?.findViewById<View>(R.id.tv_subtitle_settings_indicator)?.visibility = View.VISIBLE
            }
        }
    }

    fun switchSelectionChanged(viewId: Int, selected: Boolean) {
        val switchViewItem: View? = findViewById(viewId)
        switchViewItem?.let {
            if (it is TvSubtitleContrastSettingsItemView) {
                it.setIsChecked(selected)
            }
        }
    }

}
