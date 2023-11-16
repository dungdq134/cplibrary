package pl.cyfrowypolsat.cpchromecast.presentation.mediaroutebutton

import android.content.Context
import androidx.mediarouter.app.MediaRouteActionProvider
import androidx.mediarouter.app.MediaRouteButton

class ChromecastMediaRouteActionProvider(context: Context) : MediaRouteActionProvider(context) {

    init {
        dialogFactory = ThemeableMediaRouteDialogFactory()
    }

    override fun onCreateMediaRouteButton(): MediaRouteButton {
        return ChromecastMediaRouteButton(context)
    }

}