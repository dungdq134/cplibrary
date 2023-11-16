package pl.cyfrowypolsat.cpcommon.core.utils

import android.app.UiModeManager
import android.content.Context
import android.content.res.Configuration
import android.media.AudioManager
import android.os.Build

object DeviceUtils {

    private const val AMAZON_FEATURE_FIRE_TV = "amazon.hardware.fire_tv"
    private const val AMAZON_MANUFACTURER = "Amazon"
    private const val TMOBILE_STB_MODEL = "KSTB6077"
    private const val TOYA_STB_MODEL = "DTC974x"
    private const val TOYA_STB_MANUFACTURER = "Jiuzhou"
    private const val POLSAT_OR_NETIA_STB_MODEL = "polsat soundbox 4K"

    fun isAndroidTv(context: Context): Boolean {
        val uiModeManager = context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        return uiModeManager.currentModeType == Configuration.UI_MODE_TYPE_TELEVISION
    }

    fun isAmazonFireTv(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(AMAZON_FEATURE_FIRE_TV)
    }

    fun isAmazonDevice(): Boolean {
        return Build.MANUFACTURER.equals(AMAZON_MANUFACTURER, true)
    }

    fun isTMobileSTB(): Boolean {
        return Build.MODEL.equals(TMOBILE_STB_MODEL, true)
    }

    fun isToyaSTB(): Boolean {
        return Build.MODEL.equals(TOYA_STB_MODEL, true)
                && Build.MANUFACTURER.equals(TOYA_STB_MANUFACTURER, true)
    }

    fun isPolsatOrNetiaSTB(): Boolean {
        return Build.MODEL.equals(POLSAT_OR_NETIA_STB_MODEL, true)
    }

    fun getDeviceModel(): String {
        val model = Build.MODEL
        return model.replace("\\s".toRegex(), "")
    }

    fun getDeviceManufacturer(): String {
        val vendor = Build.MANUFACTURER
        return vendor.replace("\\s".toRegex(), "")
    }

    fun getDeviceVolumeLevel(context: Context): Int {
        val am = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager?
        val streamVolume = am?.getStreamVolume(AudioManager.STREAM_MUSIC) ?: 100
        val maxStreamVolume = am?.getStreamMaxVolume(AudioManager.STREAM_MUSIC) ?: 100

        val percentLevel = (streamVolume.toFloat()/maxStreamVolume.toFloat())*100
        return percentLevel.toInt()
    }
}