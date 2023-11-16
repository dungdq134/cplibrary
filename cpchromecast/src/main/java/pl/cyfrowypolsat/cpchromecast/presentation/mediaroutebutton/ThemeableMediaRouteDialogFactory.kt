package pl.cyfrowypolsat.cpchromecast.presentation.mediaroutebutton

import androidx.annotation.NonNull
import androidx.mediarouter.app.MediaRouteControllerDialogFragment
import androidx.mediarouter.app.MediaRouteDialogFactory


class ThemeableMediaRouteDialogFactory : MediaRouteDialogFactory() {

    @NonNull
    override fun onCreateControllerDialogFragment(): MediaRouteControllerDialogFragment {
        return ThemeableMediaRouteControllerDialogFragment()
    }
}