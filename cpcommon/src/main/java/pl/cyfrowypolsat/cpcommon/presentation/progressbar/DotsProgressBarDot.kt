package pl.cyfrowypolsat.cpcommon.presentation.progressbar

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import pl.cyfrowypolsat.cpcommon.R


class DotsProgressBarDot @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyleAttr) {

    init {
        layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
        setImageDrawable(ContextCompat.getDrawable(context, R.drawable.cpcmn_common_ic_progress_bar_dot))
    }

}