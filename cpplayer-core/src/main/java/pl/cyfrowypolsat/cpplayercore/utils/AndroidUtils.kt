package pl.cyfrowypolsat.cpplayercore.utils

import android.annotation.SuppressLint
import android.content.res.Resources
import android.media.MediaDrm
import android.os.Build
import androidx.media3.common.C
import pl.cyfrowypolsat.cpplayercore.R


fun hasMultiWindowSupport(): Boolean {
    return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N
}


@SuppressLint("WrongConstant")
fun getWidevineLevelInfo(resources: Resources): String {
    return try {
        val mediaDrm = MediaDrm(C.WIDEVINE_UUID)
        val drmLevel = mediaDrm.getPropertyString("securityLevel")
        when (drmLevel) {
            "L1" -> resources.getString(R.string.cppl_cr_widevine_level_1)
            "L3" -> resources.getString(R.string.cppl_cr_widevine_level_3)
            else -> resources.getString(R.string.cppl_cr_widevine_not_supported)
        }
    } catch (e: Exception) {
        resources.getString(R.string.cppl_cr_widevine_no_drm_data)
    }
}
