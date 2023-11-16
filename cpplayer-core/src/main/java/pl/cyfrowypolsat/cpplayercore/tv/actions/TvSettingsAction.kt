package pl.cyfrowypolsat.cpplayercore.tv.actions

import android.content.Context
import androidx.leanback.widget.PlaybackControlsRow
import pl.cyfrowypolsat.cpplayercore.R

class TvSettingsAction(private val context: Context): PlaybackControlsRow.MoreActions(context) {

    init {
        icon = context.getDrawable(R.drawable.cppl_cr_tv_settings_action_drawable)
        label1 = context.getString(R.string.cppl_cr_action_settings)
    }
}