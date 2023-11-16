package pl.cyfrowypolsat.cpchromecast.core.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pl.cyfrowypolsat.cpchromecast.core.gson.MessageDeserializer
import pl.cyfrowypolsat.cpchromecast.data.model.received.Message
import pl.cyfrowypolsat.cpchromecast.core.ApplicationDataProvider
import pl.cyfrowypolsat.cpchromecast.presentation.manager.ChromecastManager
import pl.cyfrowypolsat.cpcommon.domain.usecase.FindBestImageUseCase
import pl.cyfrowypolsat.cpcommon.domain.usecase.PrepareImageUrlUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class CpChromecastModule {

    @CpChromecastQualifier
    @Provides
    fun provideChromecastManager(): ChromecastManager {
        return ChromecastManager.instance
    }

    @CpChromecastQualifier
    @Provides
    fun provideFindBestImageUseCase(@CpChromecastQualifier prepareImageUrlUseCase: PrepareImageUrlUseCase): FindBestImageUseCase {
        return FindBestImageUseCase(prepareImageUrlUseCase)
    }

    @CpChromecastQualifier
    @Provides
    fun providePrepareImageUrlUseCase(applicationDataProvider: ApplicationDataProvider): PrepareImageUrlUseCase {
        return PrepareImageUrlUseCase(applicationDataProvider.imgGeneratorCDNUrl(), applicationDataProvider.imgGeneratorCDNKey())
    }

    @CpChromecastQualifier
    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
                .registerTypeAdapter(Message::class.java, MessageDeserializer())
                .create()
    }
}