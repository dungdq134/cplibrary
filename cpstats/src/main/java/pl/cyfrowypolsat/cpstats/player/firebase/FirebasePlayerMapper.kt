package pl.cyfrowypolsat.cpstats.player.firebase

import pl.cyfrowypolsat.cpstats.core.model.ErrorData
import pl.cyfrowypolsat.cpstats.player.PlayerAdvertErrorEvent
import pl.cyfrowypolsat.cpstats.player.PlayerErrorEvent
import pl.cyfrowypolsat.cpstats.player.PlayerEvent

class FirebasePlayerMapper() {

    fun mapEvent(event: PlayerEvent): FirebasePlayerHit? {
        return when (event) {
            is PlayerErrorEvent -> buildErrorHit(event, event.errorData)
            is PlayerAdvertErrorEvent -> buildErrorHit(event, event.errorData)
            else -> null
        }
    }

    private fun buildErrorHit(event: PlayerEvent,
                              errorData: ErrorData): FirebasePlayerHit {
        val rootThrowable = Throwable(errorData.throwable.message)
        rootThrowable.stackTrace = buildStackTraceForRootThrowable(event, errorData)
        return FirebasePlayerHit(rootThrowable)
    }

    private fun buildStackTraceForRootThrowable(event: PlayerEvent,
                                                errorData: ErrorData): Array<StackTraceElement> {
        val errorStackTrace = mutableListOf<StackTraceElement>()
        errorStackTrace.add(StackTraceElement(event.eventType.name, "", "", 0))
        errorData.backendErrorData?.let {
            errorStackTrace.add(StackTraceElement(it.serviceUrl + it.methodName, "", "${it.type}:${errorData.errorCode}", 0))
        }
        return errorStackTrace.toTypedArray()
    }

}