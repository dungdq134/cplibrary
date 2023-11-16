package pl.cyfrowypolsat.cpdata.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import pl.cyfrowypolsat.cpdata.api.auth.AuthService
import pl.cyfrowypolsat.cpdata.api.common.clientcontext.ClientContextRefreshApi
import pl.cyfrowypolsat.cpdata.api.common.clientcontext.ClientContextRepository
import pl.cyfrowypolsat.cpdata.api.common.session.AutoLoginFailedListener
import pl.cyfrowypolsat.cpdata.api.common.useragent.UserAgentDataBuilder
import pl.cyfrowypolsat.cpdata.api.concurrentaccess.ConcurrentAccessService
import pl.cyfrowypolsat.cpdata.api.drm.DrmService
import pl.cyfrowypolsat.cpdata.api.navigation.NavigationService
import pl.cyfrowypolsat.cpdata.api.payments.PaymentsService
import pl.cyfrowypolsat.cpdata.api.system.SystemService
import pl.cyfrowypolsat.cpdata.api.usercontent.UserContentService
import pl.cyfrowypolsat.cpdata.common.ApplicationData
import pl.cyfrowypolsat.cpdata.common.manager.AccountManager
import pl.cyfrowypolsat.cpdata.common.manager.AppDataManager
import pl.cyfrowypolsat.cpdata.common.manager.LaunchCountManager
import pl.cyfrowypolsat.cpdata.files.FilesManager
import pl.cyfrowypolsat.cpdata.repository.*
import javax.inject.Singleton

@Singleton
@Component(modules = [CpDataModule::class])
internal interface CpDataComponent {

    // Service
    fun inject(authService: AuthService)
    fun inject(drmService: DrmService)
    fun inject(navigationService: NavigationService)
    fun inject(paymentsService: PaymentsService)
    fun inject(systemService: SystemService)
    fun inject(concurrentAccessService: ConcurrentAccessService)

    // Repository
    fun inject(configurationRepository: ConfigurationRepository)
    fun inject(homeMenuRepository: HomeMenuRepository)
    fun inject(avatarsRepository: AvatarsRepository)
    fun inject(watchedContentDataRepository: WatchedContentDataRepository)
    fun inject(tvChannelsRepository: TvChannelsRepository)
    fun inject(channelsProgramRepository: ChannelsProgramRepository)
    fun inject(clientContextRepository: ClientContextRepository)

    // Manager
    fun inject(accountManager: AccountManager)
    fun inject(appDataManager: AppDataManager)
    fun inject(launchCountManager: LaunchCountManager)
    fun inject(filesManager: FilesManager)

    // UserContent
    fun inject(userContentService: UserContentService)

    @Component.Builder
    interface Builder {
        fun context(@BindsInstance context: Context): Builder
        fun userAgentDataBuilder(@BindsInstance userAgentDataBuilder: UserAgentDataBuilder): Builder
        fun applicationData(@BindsInstance applicationData: ApplicationData): Builder
        fun autoLoginFailedListener(@BindsInstance autoLoginFailedListener: AutoLoginFailedListener): Builder
        fun build(): CpDataComponent
    }
}