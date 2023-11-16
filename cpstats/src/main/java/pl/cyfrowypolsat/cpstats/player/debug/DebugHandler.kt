package pl.cyfrowypolsat.cpstats.player.debug

import android.os.Handler
import android.os.Looper
import pl.cyfrowypolsat.cpstats.core.ApplicationDataProvider
import pl.cyfrowypolsat.cpstats.player.*
import timber.log.Timber
private const val TAG = "CpStats"

class DebugHandler(private val applicationDataProvider: ApplicationDataProvider) : PlayerEventHandler {
    init {
        Timber.tag(TAG).i("Player -> init - (${applicationDataProvider.buildApplicationData()})")
    }

    override fun handleEvent(event: PlayerEvent) {
        if (event.eventType == EventType.PLAYBACK_POSITION_UPDATED) return

        Handler(Looper.getMainLooper()).post {
            when (event) {
                is PlayerErrorEvent -> {
                    Timber.tag(TAG).i("Player -> ${event.eventType.name} (${event.errorData.errorCode}) - (${event.playerData})")
                }
                is PlayerAdvertEvent -> {
                    Timber.tag(TAG).i("Advert Player -> ${event.eventType.name} - (${event.advertData})")
                }
                is PlayerAdvertErrorEvent -> {
                    Timber.tag(TAG).i("Advert Player -> ${event.eventType.name} (${event.errorData.errorCode})")
                }
                else -> {
                    Timber.tag(TAG).i("Player -> ${event.eventType.name} - (${event.playerData})")
                }
            }
        }
    }
}