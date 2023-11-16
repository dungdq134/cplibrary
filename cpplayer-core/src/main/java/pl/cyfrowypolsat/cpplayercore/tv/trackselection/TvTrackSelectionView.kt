package pl.cyfrowypolsat.cpplayercore.tv.trackselection

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import pl.cyfrowypolsat.cpplayercore.R
import pl.cyfrowypolsat.cpplayercore.databinding.CpplCrTvViewTrackSelectionBinding

class TvTrackSelectionView
@JvmOverloads constructor(context: Context,
                          attrs: AttributeSet? = null,
                          defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding = CpplCrTvViewTrackSelectionBinding.inflate(LayoutInflater.from(context), this, true)
    private var selectedItem: TvTrackSelectionItemView? = null

    fun setup(name: String,
              viewItems: List<TvTrackSelectionItemView>) {
        binding.tvTrackSelectionName.text = name
        binding.tvTrackSelectionContainer.removeAllViews()
        viewItems.forEach {
            binding.tvTrackSelectionContainer.addView(it)
        }
    }

    fun setSelectedItem(id: Int) {
        val newSelectedItem: TvTrackSelectionItemView? = findViewById(id)
        newSelectedItem?.let {
            selectedItem?.findViewById<View>(R.id.tv_track_selected_indicator)?.visibility = View.INVISIBLE
            selectedItem = it
            selectedItem?.findViewById<View>(R.id.tv_track_selected_indicator)?.visibility = View.VISIBLE
        }
    }

}
