package pl.cyfrowypolsat.cpplayer.drm

interface LicenseListener {
    fun onLicenseRequestStarted() {}
    fun onLicenseRequestCompleted(licenseInfo: LicenseInfo) {}
    fun onLicenseRequestError(throwable: Throwable) {}
}