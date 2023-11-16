package pl.cyfrowypolsat.cpcommon.core.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pl.cyfrowypolsat.cpcommon.core.ApplicationDataProvider
import pl.cyfrowypolsat.cpcommon.domain.usecase.FindBestImageUseCase
import pl.cyfrowypolsat.cpcommon.domain.usecase.PrepareImageUrlUseCase

@Module
@InstallIn(SingletonComponent::class)
internal class CpCommonModule {

    @CpCommonQualifier
    @Provides
    fun provideFindBestImageUseCase(@CpCommonQualifier prepareImageUrlUseCase: PrepareImageUrlUseCase): FindBestImageUseCase {
        return FindBestImageUseCase(prepareImageUrlUseCase)
    }

    @CpCommonQualifier
    @Provides
    fun providePrepareImageUrlUseCase(applicationDataProvider: ApplicationDataProvider): PrepareImageUrlUseCase {
        return PrepareImageUrlUseCase(applicationDataProvider.imgGeneratorCDNUrl(), applicationDataProvider.imgGeneratorCDNKey())
    }
}