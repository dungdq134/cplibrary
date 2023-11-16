package pl.cyfrowypolsat.cpstats.session

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import timber.log.Timber
import java.util.*

class SessionExpiredManager : LifecycleObserver {

    private var dateOfPause: Date? = null

    interface SessionExpiredListener {
        fun onSessionExpired()
    }

    private var sessionExpiredListenerList: MutableList<SessionExpiredListener> = mutableListOf()

    init {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    fun addSessionExpiredListener(sessionExpiredListener: SessionExpiredListener) {
        if(!sessionExpiredListenerList.contains(sessionExpiredListener)){
            sessionExpiredListenerList.add(sessionExpiredListener)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun onAppBackgrounded() {
        Timber.d("onAppBackgrounded")
        pauseAppSession()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun onAppForegrounded() {
        Timber.d("onAppForegrounded")
        if (checkIfSessionExpired()) {
            Timber.d("sessionExpired")
            sessionExpiredListenerList.forEach { it.onSessionExpired() }
            return
        }
        resumeAppSession()
    }

    private fun pauseAppSession() {
        Timber.d("pauseAppSession")
        dateOfPause = Date()
    }

    private fun resumeAppSession() {
        Timber.d("resumeAppSession")
        dateOfPause = null
    }

    private fun checkIfSessionExpired(): Boolean {
        val currentDate = Date()
        return dateOfPause != null && currentDate.time - dateOfPause!!.time > DEFAULT_SESSION_DURATION
    }

    companion object {

        private val MINUTE = (60 * 1000)
        private val DEFAULT_SESSION_DURATION = 20 * MINUTE
    }
}
