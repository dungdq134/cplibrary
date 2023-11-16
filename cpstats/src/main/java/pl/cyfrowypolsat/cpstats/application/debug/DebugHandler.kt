package pl.cyfrowypolsat.cpstats.application.debug

import pl.cyfrowypolsat.cpstats.application.*
import pl.cyfrowypolsat.cpstats.core.ApplicationDataProvider
import timber.log.Timber
private const val TAG = "CpStats"

class DebugHandler(private val applicationDataProvider: ApplicationDataProvider) : ApplicationEventHandler {
    init {
        Timber.tag(TAG).i("Application -> data - (${applicationDataProvider.buildApplicationData()})")
    }

    override fun handleEvent(event: ApplicationEvent) {
        val message = when (event) {
            is ApplicationNavigationEvent -> "${event.eventType.name} - place: ${event.placeData}"
            is ApplicationNavigationErrorEvent -> "${event.eventType.name} - place: ${event.placeData} - error: ${event.errorData.errorCode}"
            is ApplicationNonFatalErrorEvent -> "${event.eventType.name} - exception: ${event.errorData.throwable.message} - errorCode: ${event.errorData.errorCode}"
            else -> event.eventType.name
        }
        Timber.tag(TAG).i("Application -> $message")
    }
}