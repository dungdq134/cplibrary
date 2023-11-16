package pl.cyfrowypolsat.cpcommon.presentation.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import pl.cyfrowypolsat.cpcommon.R
import pl.cyfrowypolsat.cpcommon.presentation.extensions.focusable

fun getAgeGroupDrawable(context: Context, ageGroup: Int?): Drawable? {
    return when (ageGroup) {
        7 -> ContextCompat.getDrawable(context, R.drawable.cpcmn_common_ic_polish_tv_rating_system_7_2011)
        12 -> ContextCompat.getDrawable(context, R.drawable.cpcmn_common_ic_polish_tv_rating_system_12_2011)
        16 -> ContextCompat.getDrawable(context, R.drawable.cpcmn_common_ic_polish_tv_rating_system_16_2011)
        18 -> ContextCompat.getDrawable(context, R.drawable.cpcmn_common_ic_polish_tv_rating_system_18_2011)
        else -> null
    }
}

fun setAgeGroupContentDescription(view: View, ageGroup: Int?) {
    view.focusable()
    view.contentDescription = when (ageGroup) {
        7 -> view.context.getString(R.string.cpcmn_talkback_agegroup_7)
        12 -> view.context.getString(R.string.cpcmn_talkback_agegroup_12)
        16 -> view.context.getString(R.string.cpcmn_talkback_agegroup_16)
        18 -> view.context.getString(R.string.cpcmn_talkback_agegroup_18)
        else -> null
    }
}