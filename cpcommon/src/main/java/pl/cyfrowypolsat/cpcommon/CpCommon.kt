package pl.cyfrowypolsat.cpcommon

import android.content.Context
import pl.cyfrowypolsat.cpcommon.core.ApplicationDataProvider
import pl.cyfrowypolsat.cpcommon.core.di.CpCommonComponent
import pl.cyfrowypolsat.cpcommon.core.di.DaggerCpCommonComponent

class CpCommon private constructor() {

    companion object {
        val instance = CpCommon()
    }

    private var cpCommonComponent: CpCommonComponent? = null

    internal fun getCpCommonComponent(): CpCommonComponent {
        return this.cpCommonComponent ?: throw RuntimeException("CpCommon is not initialize correctly. Call CpCommon.getInstance().init() in Application onCreate.")
    }

    fun init(context: Context, applicationDataProvider: ApplicationDataProvider) {
        this.cpCommonComponent = DaggerCpCommonComponent
                .builder()
                .context(context)
                .applicationDataProvider(applicationDataProvider)
                .build()
    }

}