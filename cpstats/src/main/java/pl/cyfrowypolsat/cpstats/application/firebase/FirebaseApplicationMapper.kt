package pl.cyfrowypolsat.cpstats.application.firebase

import pl.cyfrowypolsat.cpstats.application.*
import pl.cyfrowypolsat.cpstats.core.model.ErrorData
import timber.log.Timber
import java.lang.System.arraycopy

class FirebaseApplicationMapper() {

    fun mapEvent(event: ApplicationEvent): FirebaseApplicationHit? {
        return when (event) {
            is ApplicationNonFatalErrorEvent -> buildErrorHit(event, event.errorData)
            is ApplicationNavigationErrorEvent -> buildErrorHit(event, event.errorData)
            is ApplicationLoginErrorEvent -> buildErrorHit(event, event.errorData)
            is ApplicationRegisterErrorEvent -> buildErrorHit(event, event.errorData)
            else -> null
        }
    }

    private fun buildErrorHit(event: ApplicationEvent,
                              errorData: ErrorData): FirebaseApplicationHit {

        val exception = Exception(errorData.throwable.message)

        var errorStackTrace = mutableListOf<StackTraceElement>()
        errorStackTrace.add(StackTraceElement(event.eventType.name, errorData.errorCode, errorData.throwable.javaClass.simpleName, 0))
        errorData.backendErrorData?.let {
            errorStackTrace.add(StackTraceElement(it.serviceUrl + it.methodName, "" , "${it.type}:${errorData.errorCode}", 0))
        }

        errorStackTrace.addAll(errorData.throwable.stackTrace)
        exception.stackTrace = errorStackTrace.toTypedArray()
        return FirebaseApplicationHit(exception)
    }
}