package pl.cyfrowypolsat.cpchromecast.domain.model.device

import androidx.mediarouter.media.MediaRouter

class ChromecastDevice(val mRouter: MediaRouter, val mRouteInfo: MediaRouter.RouteInfo) {
    val name: String
        get() = mRouteInfo.name

    val description: String?
        get() = mRouteInfo.description

    val isEnabled: Boolean
        get() = mRouteInfo.isEnabled

    override fun equals(other: Any?): Boolean {
        return try {
            if (this === other) return true
            if (other == null) return false
            if (other is MediaRouter.RouteInfo) return false
            if (javaClass != other.javaClass) return false
            val that = other as ChromecastDevice
            areChromecastDevicesEqual(mRouteInfo, that.mRouteInfo)
        } catch (ex: Exception) {
            ex.printStackTrace()
            false
        }
    }

    private fun areChromecastDevicesEqual(deviceInfo1: MediaRouter.RouteInfo, deviceInfo2: MediaRouter.RouteInfo): Boolean {
        return deviceInfo2.id == deviceInfo1.id
    }

    override fun hashCode(): Int {
        return try {
            mRouteInfo.name.hashCode() + 1
        } catch (ex: Exception) {
            ex.printStackTrace()
            -1
        }
    }

}