package pl.cyfrowypolsat.cpplayercore.tv.actions

import android.content.Context
import androidx.leanback.widget.PlaybackControlsRow
import pl.cyfrowypolsat.cpplayercore.R

class TvPlayerOverlayAction(private val context: Context): PlaybackControlsRow.MoreActions(context) {

    init {
        icon = context.getDrawable(R.drawable.cppl_cr_tv_ic_overlay)
        label1 = context.getString(R.string.cppl_cr_action_overlay)
    }
}