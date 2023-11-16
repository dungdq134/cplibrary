package pl.cyfrowypolsat.cpdata.api.common.useragent

class UserAgentDataBuilder constructor(private val portalName: String,
                                       private val buildVersion: Int,
                                       private val isAndroidTv: Boolean,
                                       private val isAmazonDevice: Boolean,
                                       private val isAmazonFireTv: Boolean,
                                       private val isTMobileSTB: Boolean,
                                       private val isToyaSTB: Boolean,
                                       private val isPolsatOrNetiaSTB: Boolean,
                                       private val hasGoogleServices: Boolean) {

    companion object {
        private const val TV_VALUE = "tv"
        private const val TV_AMAZON_VALUE = "tv_amazon"
        private const val MOBILE_VALUE = "mobile"
        private const val MOBILE_AMAZON_VALUE = "mobile_amazon"
        private const val STB_TMOBILE_VALUE = "stb_tmobile"
        private const val STB_TOYA_VALUE = "stb_toya"
        private const val STB_POLSAT_OR_NETIA_VALUE = "stb_cp_atv_64"
        private const val APPLICATION_VALUE = "native"
        private const val PLAYER_VALUE = "cpplayer"
        private const val OS_WITH_GOOGLE_SERVICES_VALUE = "android"
        private const val OS_WITHOUT_GOOGLE_SERVICES_VALUE = "android_nogs"
    }

    fun build(): UserAgentData {
        return UserAgentData(application = APPLICATION_VALUE,
                build = buildVersion,
                deviceType = getDeviceTypeValue(),
                os = getOsValue(),
                player = PLAYER_VALUE,
                portal = portalName,
                widevine = true
        )
    }

    private fun getDeviceTypeValue(): String {
        return if (isTMobileSTB) {
            STB_TMOBILE_VALUE
        } else if (isToyaSTB) {
            STB_TOYA_VALUE
        } else if (isPolsatOrNetiaSTB) {
            STB_POLSAT_OR_NETIA_VALUE
        } else if (isAmazonFireTv) {
            TV_AMAZON_VALUE
        } else if (isAndroidTv) {
            TV_VALUE
        } else if(isAmazonDevice){
            MOBILE_AMAZON_VALUE
        } else {
            MOBILE_VALUE
        }
    }

    private fun getOsValue(): String {
        return if (hasGoogleServices) {
            OS_WITH_GOOGLE_SERVICES_VALUE
        } else {
            OS_WITHOUT_GOOGLE_SERVICES_VALUE
        }
    }

}