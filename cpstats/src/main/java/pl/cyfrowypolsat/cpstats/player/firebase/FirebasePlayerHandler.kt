package pl.cyfrowypolsat.cpstats.player.firebase

import com.google.firebase.crashlytics.FirebaseCrashlytics
import pl.cyfrowypolsat.cpstats.core.ApplicationDataProvider
import pl.cyfrowypolsat.cpstats.player.PlayerErrorEvent
import pl.cyfrowypolsat.cpstats.player.PlayerEvent
import pl.cyfrowypolsat.cpstats.player.PlayerEventHandler


class FirebasePlayerHandler(private val applicationDataProvider: ApplicationDataProvider) : PlayerEventHandler {
    private val mapper: FirebasePlayerMapper = FirebasePlayerMapper()

    override fun handleEvent(event: PlayerEvent) {
        val hit = mapper.mapEvent(event)

        hit?.let {
            setFirebaseKeys(event)
            FirebaseCrashlytics.getInstance().recordException(it.throwable)
        }
    }

    private fun setFirebaseKeys(event: PlayerEvent) {
        val placeData = applicationDataProvider.currentPlaceData
        val playerContext = applicationDataProvider.playerContext
        val mediaId = event.playerData.mediaId
        val clientId = applicationDataProvider.userData().clientId

        FirebaseCrashlytics.getInstance().let {
            it.setCustomKey("applicationTraceId", applicationDataProvider.applicationTraceId)
            it.setCustomKey("playbackTraceId", event.playerData.playbackTraceId)
            it.setCustomKey("userId", (applicationDataProvider.userData().userId?.toString() ?: ""))
            it.setCustomKey("clientId", clientId)
            it.setCustomKey("userAgent", applicationDataProvider.applicationUserAgent())
            it.setCustomKey("currentPlaceType", placeData.type)
            it.setCustomKey("currentPlaceValue", (placeData.value ?: ""))

            it.setCustomKey("playerContextType", playerContext.placeType)
            it.setCustomKey("playerContextValue", (playerContext.placeValue ?: ""))

            it.setCustomKey("mediaId", mediaId.id)
            it.setCustomKey("mediaCpid", mediaId.cpid)

            it.setCustomKey("quality", event.playerData.currentQuality)
            it.setCustomKey("licenseId", event.playerData.licenseId ?: "")

            it.setCustomKey("advertsRequestUrl", event.playerData.advertsRequestUrl ?: "")

            if (event is PlayerErrorEvent) {
                it.setCustomKey("errorCode", event.errorData.errorCode)
            }
        }
    }
}