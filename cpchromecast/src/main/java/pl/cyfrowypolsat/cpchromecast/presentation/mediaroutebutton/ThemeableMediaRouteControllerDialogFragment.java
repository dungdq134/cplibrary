package pl.cyfrowypolsat.cpchromecast.presentation.mediaroutebutton;

import android.content.Context;
import android.os.Bundle;

import androidx.mediarouter.app.MediaRouteControllerDialog;
import androidx.mediarouter.app.MediaRouteControllerDialogFragment;

import pl.cyfrowypolsat.cpchromecast.R;

public class ThemeableMediaRouteControllerDialogFragment extends MediaRouteControllerDialogFragment {

    @Override
    public MediaRouteControllerDialog onCreateControllerDialog(Context context, Bundle savedInstanceState) {
        return new MediaRouteControllerDialog(context, R.style.MediaRouteControllerDialogTheme);
    }

}