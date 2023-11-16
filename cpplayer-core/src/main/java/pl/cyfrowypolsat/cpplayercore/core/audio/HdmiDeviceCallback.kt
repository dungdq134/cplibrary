package pl.cyfrowypolsat.cpplayercore.core.audio

import android.media.AudioDeviceCallback
import android.media.AudioDeviceInfo
import pl.cyfrowypolsat.cpplayercore.events.playerview.PlayerViewListenerMapper

class HdmiDeviceCallback(private val playerViewListenerMapper: PlayerViewListenerMapper?) : AudioDeviceCallback() {
    override fun onAudioDevicesRemoved(removedDevices: Array<out AudioDeviceInfo>?) {
        if (removedDevices?.any { it.type == AudioDeviceInfo.TYPE_HDMI } == true) {
            playerViewListenerMapper?.onPlayerCloseRequested()
        }

        super.onAudioDevicesRemoved(removedDevices)
    }
}

