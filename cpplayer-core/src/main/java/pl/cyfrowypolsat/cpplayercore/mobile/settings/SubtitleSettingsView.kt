package pl.cyfrowypolsat.cpplayercore.mobile.settings

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.GridLayout
import android.widget.Space
import pl.cyfrowypolsat.cpplayercore.databinding.CpplCrMobileViewSubtitleSettingsBinding

class SubtitleSettingsView
@JvmOverloads constructor(context: Context,
                          attrs: AttributeSet? = null,
                          defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    val binding = CpplCrMobileViewSubtitleSettingsBinding.inflate(LayoutInflater.from(context), this, true)

    private var selectedSizeItem: SubtitleSizeSettingsItemView? = null

    fun setup(sizeViewItems: List<SubtitleSizeSettingsItemView>,
              contrastViewItems: List<SubtitleContrastSettingsItemView>) {
        binding.subtitleSettingsSizeContainer.removeAllViews()
        binding.subtitleSettingsContrastContainer.removeAllViews()
        sizeViewItems.forEach {
            val params = GridLayout.LayoutParams().apply {
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f)
                width = 0
            }
            it.layoutParams = params

            binding.subtitleSettingsSizeContainer.addView(it)
        }
        contrastViewItems.forEach {
            val params = GridLayout.LayoutParams().apply {
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f)
                width = 0
            }
            it.layoutParams = params

            binding.subtitleSettingsContrastContainer.addView(it)
        }
        if (contrastViewItems.size == 1) {
            val space = Space(context)
            space.apply {
                val params = GridLayout.LayoutParams().apply {
                    columnSpec = GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f)
                    width = 0
                }
                this.layoutParams = params

                binding.subtitleSettingsContrastContainer.addView(this)
            }
        }
    }

    fun setSelectedItem(id: Int) {
        val newSelectedItem: View? = findViewById(id)
        newSelectedItem?.let {
            if (it is SubtitleSizeSettingsItemView) {
                selectedSizeItem?.selectItem(false)
                selectedSizeItem = it
                selectedSizeItem?.selectItem(true)
            }
        }
    }

    fun switchSelectionChanged(viewId: Int, selected: Boolean) {
        val switchViewItem: View? = findViewById(viewId)
        switchViewItem?.let {
            if (it is SubtitleContrastSettingsItemView) {
                it.setIsChecked(selected)
            }
        }
    }

}