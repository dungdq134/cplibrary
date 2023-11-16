package pl.cyfrowypolsat.cpdata.common.utils

import android.content.Context
import android.provider.Settings
import pl.cyfrowypolsat.cpcommon.core.utils.DeviceUtils
import pl.cyfrowypolsat.cpdata.api.common.model.DeviceId
import pl.cyfrowypolsat.cpdata.common.manager.AccountManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceIdGenerator
@Inject constructor(private val context: Context,
                    private val accountManager: AccountManager) {

    companion object {
        private const val OTHER_TYPE = "other"
        private const val MODULE_TYPE = "module"
    }

    fun generateDeviceId(): DeviceId {
        val type = if (DeviceUtils.isPolsatOrNetiaSTB()) {
            MODULE_TYPE
        } else {
            OTHER_TYPE
        }
        val value = if (DeviceUtils.isPolsatOrNetiaSTB()) {
            accountManager.getChipId() ?: "0000000000000000"
        } else {
            try {
                Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            } catch (e: Exception) {
                "0000000000000000"
            }
        }
        return DeviceId(type, value)
    }

}