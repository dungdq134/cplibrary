package pl.cyfrowypolsat.cpchromecast.core.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import pl.cyfrowypolsat.cpchromecast.presentation.imagepicker.ChromecastImagePicker
import pl.cyfrowypolsat.cpchromecast.core.ApplicationDataProvider
import pl.cyfrowypolsat.cpchromecast.presentation.manager.ChromecastManager
import pl.cyfrowypolsat.cpchromecast.presentation.mediaroutebutton.ChromecastMediaRouteButton
import javax.inject.Singleton

@Singleton
@Component(modules = [CpChromecastModule::class])
internal interface CpChromecastComponent {

    fun inject(chromecastMediaRouteButton: ChromecastMediaRouteButton)
    fun inject(chromecastImagePicker: ChromecastImagePicker)
    fun inject(chromecastManager: ChromecastManager)

    @Component.Builder
    interface Builder {
        fun context(@BindsInstance context: Context): Builder
        fun applicationDataProvider(@BindsInstance applicationDataProvider: ApplicationDataProvider): Builder
        fun build(): CpChromecastComponent
    }
}