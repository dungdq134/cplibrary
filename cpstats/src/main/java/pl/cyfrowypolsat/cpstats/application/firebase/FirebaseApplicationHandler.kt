package pl.cyfrowypolsat.cpstats.application.firebase

import com.google.firebase.crashlytics.FirebaseCrashlytics
import pl.cyfrowypolsat.cpstats.application.*
import pl.cyfrowypolsat.cpstats.core.ApplicationDataProvider


class FirebaseApplicationHandler(private val applicationDataProvider: ApplicationDataProvider) : ApplicationEventHandler {
    private val mapper: FirebaseApplicationMapper = FirebaseApplicationMapper()

    override fun handleEvent(event: ApplicationEvent) {
        val hit = mapper.mapEvent(event)

        hit?.let {
            setFirebaseKeys()
            FirebaseCrashlytics.getInstance().recordException(it.exception)
        }
    }

    private fun setFirebaseKeys(){
        val placeData = applicationDataProvider.currentPlaceData
        val clientId = applicationDataProvider.userData().clientId

        FirebaseCrashlytics.getInstance().let {
            it.setCustomKey("applicationTraceId", applicationDataProvider.applicationTraceId)
            it.setCustomKey("userId", (applicationDataProvider.userData().userId?.toString() ?: ""))
            it.setCustomKey("clientId", clientId)
            it.setCustomKey("userAgent", applicationDataProvider.applicationUserAgent())
            it.setCustomKey("currentPlaceType", placeData.type)
            it.setCustomKey("currentPlaceValue", (placeData.value ?: ""))
        }
    }
}