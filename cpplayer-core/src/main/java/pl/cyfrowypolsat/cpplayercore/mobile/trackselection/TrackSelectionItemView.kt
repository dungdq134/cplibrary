package pl.cyfrowypolsat.cpplayercore.mobile.trackselection

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import pl.cyfrowypolsat.cpplayercore.R
import pl.cyfrowypolsat.cpplayercore.core.trackselection.TrackSelectionItem
import pl.cyfrowypolsat.cpplayercore.databinding.CpplCrMobileViewTrackSelectionItemBinding


class TrackSelectionItemView
@JvmOverloads constructor(context: Context,
                          attrs: AttributeSet? = null,
                          defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

    val binding = CpplCrMobileViewTrackSelectionItemBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        isFocusable = true
        isClickable = true
    }

    var trackSelectionItem: TrackSelectionItem? = null
        set(value) {
            field = value
            value?.let {
                binding.trackSelectionItemText.text = it.name
            }
        }

    fun selectItem(select: Boolean) {
        if (select) {
            binding.trackSelectionItemText.setTextColor(ContextCompat.getColor(context, R.color.cppl_cr_color_primary))
        } else {
            binding.trackSelectionItemText.setTextColor(ContextCompat.getColor(context, R.color.cppl_cr_grey_a9aeb4))
        }
    }

}