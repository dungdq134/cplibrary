package pl.cyfrowypolsat.cpchromecast.core

interface ApplicationDataProvider {
    fun chromecastAppId(): String?
    fun chromecastAppCustomNamespace(): String?
    fun imgGeneratorCDNKey(): String?
    fun imgGeneratorCDNUrl(): String?
}