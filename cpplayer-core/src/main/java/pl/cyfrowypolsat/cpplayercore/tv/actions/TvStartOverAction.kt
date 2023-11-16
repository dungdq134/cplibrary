package pl.cyfrowypolsat.cpplayercore.tv.actions

import android.content.Context
import androidx.leanback.widget.PlaybackControlsRow
import pl.cyfrowypolsat.cpplayercore.R

class TvStartOverAction(private val context: Context) : PlaybackControlsRow.MoreActions(context) {

    init {
        icon = context.getDrawable(R.drawable.cppl_cr_common_ic_start_over)
    }

}