package pl.cyfrowypolsat.cpstats.player.gemiusaudiencestream

import android.content.Context
import android.os.Handler
import android.os.Looper
import com.gemius.sdk.stream.Player
import com.gemius.sdk.stream.PlayerData
import pl.cyfrowypolsat.cpstats.core.ApplicationDataProvider
import pl.cyfrowypolsat.cpstats.player.EventType
import pl.cyfrowypolsat.cpstats.player.PlayerEvent
import pl.cyfrowypolsat.cpstats.player.PlayerEventHandler
import timber.log.Timber

private const val TAG = "GemiusAudienceStream"

class GemiusAudienceStreamHandler(private val context: Context,
                                  private val gemiusAudienceStreamConfig: GemiusAudienceStreamConfig,
                                  private val applicationDataProvider: ApplicationDataProvider) : PlayerEventHandler {

    private val mapper: GemiusAudienceStreamMapper = GemiusAudienceStreamMapper(gemiusAudienceStreamConfig, applicationDataProvider)
    private lateinit var streamPlayer: Player

    override fun handleEvent(event: PlayerEvent) {
        Handler(Looper.getMainLooper()).post {
            try {
                if (!this::streamPlayer.isInitialized) {
                    streamPlayer = createStreamPlayer()
                    streamPlayer.setContext(context)
                }

                val gemiusAudienceStreamHit = mapper.map(event)
                when (gemiusAudienceStreamHit) {
                    is GemiusAudienceStreamHit.NewProgramHit -> sendNewProgram(gemiusAudienceStreamHit)
                    is GemiusAudienceStreamHit.ProgramEventHit -> sendProgramEvent(gemiusAudienceStreamHit)
                    is GemiusAudienceStreamHit.NewAdHit -> sendNewAd(gemiusAudienceStreamHit)
                    is GemiusAudienceStreamHit.AdEventHit -> sendAdEvent(gemiusAudienceStreamHit)
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    private fun createStreamPlayer(): Player {
        val playerData = PlayerData()
        playerData.resolution = "${applicationDataProvider.deviceData().screenWidth}x${applicationDataProvider.deviceData().screenHeight}"
        playerData.volume = applicationDataProvider.deviceData().deviceVolumeLevel
        val player = gemiusAudienceStreamConfig.let { Player(it.playerId, it.serverHost, it.accountId, playerData) }
        player.setContext(context)
        Timber.d(gemiusAudienceStreamConfig.let { "Player - playerId: ${it.playerId}, serverHost: ${it.serverHost}, accountId: ${it.accountId}, playerData: ${playerData.toDebugString()}" })
        return player
    }

    private fun sendNewProgram(newProgramHit: GemiusAudienceStreamHit.NewProgramHit) {
        Timber.tag(TAG).d("NewProgram - programId: ${newProgramHit.programId} programData: ${newProgramHit.programData.toDebugString()}")
        newProgramHit.let { streamPlayer.newProgram(it.programId, it.programData) }
    }

    private fun sendProgramEvent(programEventHit: GemiusAudienceStreamHit.ProgramEventHit) {
        Timber.tag(TAG).d("ProgramEvent - programId: ${programEventHit.programId}, eventType: ${programEventHit.eventType}, offset: ${programEventHit.offset}, eventData: ${programEventHit.eventProgramData.toDebugString()}")
        programEventHit.let { streamPlayer.programEvent(it.programId, it.offset, programEventHit.eventType, programEventHit.eventProgramData) }
    }

    private fun sendNewAd(newAdHit: GemiusAudienceStreamHit.NewAdHit) {
        Timber.tag(TAG).d("NewAd - adId: ${newAdHit.adId}, adData: ${newAdHit.adData.toDebugString()}")
        newAdHit.let { streamPlayer.newAd(it.adId, it.adData) }
    }

    private fun sendAdEvent(adEventHit: GemiusAudienceStreamHit.AdEventHit) {
        Timber.tag(TAG).d("AdEvent - adId: ${adEventHit.adId}, eventType: ${adEventHit.eventType}, offset: ${adEventHit.offset}, programId: ${adEventHit.programId}, eventAdData: ${adEventHit.eventAdData.toDebugString()}")
        adEventHit.let { streamPlayer.adEvent(it.programId, it.adId, it.offset, it.eventType, it.eventAdData) }
    }
}