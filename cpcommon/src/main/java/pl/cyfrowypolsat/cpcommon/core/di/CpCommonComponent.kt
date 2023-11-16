package pl.cyfrowypolsat.cpcommon.core.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import pl.cyfrowypolsat.cpcommon.core.ApplicationDataProvider
import pl.cyfrowypolsat.cpcommon.presentation.imageloader.ImageLoader
import javax.inject.Singleton

@Singleton
@Component(modules = [CpCommonModule::class])
internal interface CpCommonComponent {

    fun inject(imageLoader: ImageLoader)

    @Component.Builder
    interface Builder {
        fun context(@BindsInstance context: Context): Builder
        fun applicationDataProvider(@BindsInstance applicationDataProvider: ApplicationDataProvider): Builder
        fun build(): CpCommonComponent
    }
}