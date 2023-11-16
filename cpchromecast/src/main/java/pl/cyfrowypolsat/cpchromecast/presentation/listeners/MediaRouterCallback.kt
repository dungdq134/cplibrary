package pl.cyfrowypolsat.cpchromecast.presentation.listeners

import androidx.mediarouter.media.MediaRouter
import pl.cyfrowypolsat.cpchromecast.domain.model.device.ChromecastDevice
import timber.log.Timber

class MediaRouterCallback(private val onDeviceSelectedListener: OnDeviceSelectedListener) : MediaRouter.Callback() {
    interface OnDeviceSelectedListener {
        fun onDeviceSelected(chromecastDevice: ChromecastDevice?)
    }

    override fun onRouteSelected(router: MediaRouter,
                                 info: MediaRouter.RouteInfo,
                                 reason: Int) {
        Timber.d("onRouteSelected")
        onDeviceSelectedListener.onDeviceSelected(ChromecastDevice(router, info))
    }

}