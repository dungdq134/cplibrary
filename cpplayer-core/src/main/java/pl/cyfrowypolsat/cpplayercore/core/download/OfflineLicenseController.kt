package pl.cyfrowypolsat.cpplayercore.core.download

import androidx.media3.common.C
import androidx.media3.exoplayer.drm.DefaultDrmSessionManager
import androidx.media3.exoplayer.drm.DrmSessionEventListener
import androidx.media3.exoplayer.drm.FrameworkMediaDrm
import androidx.media3.exoplayer.drm.OfflineLicenseHelper
import androidx.media3.exoplayer.offline.DownloadRequest
import pl.cyfrowypolsat.cpplayercore.core.offline.OfflineMediaDrmCallback

class OfflineLicenseController {
    private val offlineLicenseHelper = createOfflineLicenseHelper()

    fun getLicenseDurationInfo(downloadRequest: DownloadRequest): Pair<Long, Long> {
        val licenseDuration = try {
            offlineLicenseHelper.getLicenseDurationRemainingSec(downloadRequest.keySetId!!)
        } catch (e: Exception) {
            android.util.Pair(-1L, -1L)
        }

        return Pair(licenseDuration.first, licenseDuration.second)
    }

    private fun createOfflineLicenseHelper(): OfflineLicenseHelper {
        val offlineDrmSessionManager = DefaultDrmSessionManager.Builder()
                .setUuidAndExoMediaDrmProvider(C.WIDEVINE_UUID, FrameworkMediaDrm.DEFAULT_PROVIDER)
                .build(OfflineMediaDrmCallback())
        return OfflineLicenseHelper(offlineDrmSessionManager, DrmSessionEventListener.EventDispatcher())
    }
}