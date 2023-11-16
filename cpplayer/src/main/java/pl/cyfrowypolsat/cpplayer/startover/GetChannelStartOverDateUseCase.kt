package pl.cyfrowypolsat.cpplayer.startover

import io.reactivex.rxjava3.core.Observable
import pl.cyfrowypolsat.cpcommon.data.ntp.SNTPClient
import pl.cyfrowypolsat.cpdata.api.navigation.NavigationService
import pl.cyfrowypolsat.cpdata.api.navigation.request.channelprogram.GetChannelsCurrentProgramParams
import pl.cyfrowypolsat.cpdata.repository.ConfigurationRepository
import pl.cyfrowypolsat.cpplayercore.core.startover.StartOverProvider
import java.util.*

class GetChannelStartOverDateUseCase(private val navigationService: NavigationService,
                                     private val configurationRepository: ConfigurationRepository,
                                     private val channelId: String) : StartOverProvider {

    override fun provideStartOverDate(): Observable<Date> {
        return navigationService.getChannelsCurrentProgram(GetChannelsCurrentProgramParams(listOf(channelId), 3))
                .flatMap { currentPrograms ->
                    SNTPClient.getCurrentDate(configurationRepository.getCachedConfiguration()?.ntpServer)
                            .map { date ->
                                currentPrograms[channelId]?.currentProgram(date)?.startTime ?: Date(-1)
                            }
                }
    }



}