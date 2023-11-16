package pl.cyfrowypolsat.cpplayercore.tv.trackselection

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import pl.cyfrowypolsat.cpplayercore.R
import pl.cyfrowypolsat.cpplayercore.core.trackselection.TrackSelectionItem
import pl.cyfrowypolsat.cpplayercore.databinding.CpplCrTvViewTrackSelectionItemBinding

class TvTrackSelectionItemView
@JvmOverloads constructor(context: Context,
                          attrs: AttributeSet? = null,
                          defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding = CpplCrTvViewTrackSelectionItemBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        setBackgroundResource(R.drawable.cppl_cr_tv_track_selection_item_ripple)
        isFocusable = true
        isClickable = true
    }

    var trackSelectionItem: TrackSelectionItem? = null
        set(value) {
            field = value
            value?.let {
                binding.tvTrackSelectionItemText.text = it.name
            }
        }

}