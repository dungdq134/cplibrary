package pl.cyfrowypolsat.cpplayercore.mobile.trackselection

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.GridLayout
import pl.cyfrowypolsat.cpplayercore.databinding.CpplCrMobileViewTrackSelectionBinding

class TrackSelectionView
@JvmOverloads constructor(context: Context,
                          attrs: AttributeSet? = null,
                          defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    val binding = CpplCrMobileViewTrackSelectionBinding.inflate(LayoutInflater.from(context), this, true)

    private var selectedItem: TrackSelectionItemView? = null

    fun setup(name: String,
              viewItems: List<TrackSelectionItemView>) {
        binding.trackSelectionName.text = name
        binding.trackSelectionContainer.removeAllViews()
        viewItems.forEach {
            val params = GridLayout.LayoutParams().apply {
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f)
                width = 0
            }
            it.layoutParams = params

            binding.trackSelectionContainer.addView(it)
        }


    }

    fun setSelectedItem(id: Int) {
        val newSelectedItem: TrackSelectionItemView? = findViewById(id)
        newSelectedItem?.let {
            selectedItem?.selectItem(false)
            selectedItem = it
            selectedItem?.selectItem(true)
        }
    }

}