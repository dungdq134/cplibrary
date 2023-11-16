package pl.cyfrowypolsat.cpplayercore.core.offline

import androidx.media3.exoplayer.drm.ExoMediaDrm
import androidx.media3.exoplayer.drm.MediaDrmCallback
import java.util.*

class OfflineMediaDrmCallback : MediaDrmCallback {
    //For offline playbacks where you're providing an offlineLicenseKeySetId via DefaultDrmSessionManager.setMode, the MediaDrmCallback you're using should never be called.
    //https://github.com/google/ExoPlayer/issues/5704#issuecomment-480420288

    override fun executeKeyRequest(uuid: UUID, request: ExoMediaDrm.KeyRequest): ByteArray {
        return ByteArray(128)
    }

    override fun executeProvisionRequest(uuid: UUID,
                                         request: ExoMediaDrm.ProvisionRequest): ByteArray {
        return ByteArray(128)
    }
}