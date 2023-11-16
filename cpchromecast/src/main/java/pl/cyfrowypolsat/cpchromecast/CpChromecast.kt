package pl.cyfrowypolsat.cpchromecast

import android.content.Context
import pl.cyfrowypolsat.cpchromecast.core.di.CpChromecastComponent
import pl.cyfrowypolsat.cpchromecast.core.di.DaggerCpChromecastComponent
import pl.cyfrowypolsat.cpchromecast.core.ApplicationDataProvider

class CpChromecast private constructor() {

    companion object {
        val instance = CpChromecast()
    }

    private var cpChromecastComponent: CpChromecastComponent? = null

    internal fun getCpChromecastComponent(): CpChromecastComponent {
        return this.cpChromecastComponent ?: throw RuntimeException("CpChromecast is not initialize correctly. Call CpChromecast.getInstance().init() in Application onCreate.")
    }

    fun init(context: Context, applicationDataProvider: ApplicationDataProvider) {
        this.cpChromecastComponent = DaggerCpChromecastComponent
                .builder()
                .context(context)
                .applicationDataProvider(applicationDataProvider)
                .build()
    }

    fun isChromecastComponentInitialized(): Boolean {
        return cpChromecastComponent != null
    }

}