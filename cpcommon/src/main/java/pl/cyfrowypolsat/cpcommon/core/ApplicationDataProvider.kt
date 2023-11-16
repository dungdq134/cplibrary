package pl.cyfrowypolsat.cpcommon.core

interface ApplicationDataProvider {
    fun imgGeneratorCDNKey(): String?
    fun imgGeneratorCDNUrl(): String?
}