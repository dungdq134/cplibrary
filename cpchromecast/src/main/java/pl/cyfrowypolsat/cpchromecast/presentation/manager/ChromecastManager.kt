package pl.cyfrowypolsat.cpchromecast.presentation.manager

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.mediarouter.media.MediaRouteSelector
import androidx.mediarouter.media.MediaRouter
import com.google.android.gms.cast.*
import com.google.android.gms.cast.framework.CastContext
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.CastStateListener
import com.google.android.gms.cast.framework.SessionManagerListener
import pl.cyfrowypolsat.cpchromecast.CpChromecast
import pl.cyfrowypolsat.cpchromecast.core.ApplicationDataProvider
import pl.cyfrowypolsat.cpchromecast.domain.enums.ChromecastState
import pl.cyfrowypolsat.cpchromecast.domain.model.device.ChromecastDevice
import pl.cyfrowypolsat.cpchromecast.domain.usecase.GetChromecastStateUseCase
import pl.cyfrowypolsat.cpchromecast.presentation.listeners.MediaRouterCallback
import pl.cyfrowypolsat.cpchromecast.presentation.listeners.MediaRouterCallback.OnDeviceSelectedListener
import pl.cyfrowypolsat.cpchromecast.presentation.player.ChromecastPlayer
import pl.cyfrowypolsat.cpcommon.presentation.lifecycle.SingleLiveEvent
import timber.log.Timber
import javax.inject.Inject

typealias ChromecastManagerContext = Triple<CastContext, String, String>

class ChromecastManager private constructor(): OnDeviceSelectedListener, CastStateListener, SessionManagerListener<CastSession?> {

    companion object {
        var appId: String? = null
            private set

        val instance = ChromecastManager()
    }

    @Inject lateinit var context: Context
    @Inject lateinit var applicationDataProvider: ApplicationDataProvider
    @Inject lateinit var chromecastPlayer: ChromecastPlayer
    @Inject lateinit var getChromecastStateUseCase: GetChromecastStateUseCase

    val state = MutableLiveData<ChromecastState>()
    val castApiConnected = SingleLiveEvent<Void>()
    var error = SingleLiveEvent<Throwable>()

    private var castState: Int? = null
    private var castRemoteDisplayClient: CastRemoteDisplayClient? = null
    private var chromecastDevice: ChromecastDevice? = null
    private var chromecastManagerContext: ChromecastManagerContext? = null

    private val castListener = object : Cast.Listener() {
        override fun onStandbyStateChanged(standbyState: Int) {
            Timber.d("onStandbyStateChanged: $standbyState")
            if (standbyState == Cast.STANDBY_STATE_YES) {
                releaseSession()
            }
            super.onStandbyStateChanged(standbyState)
        }
    }

    init {
        CpChromecast.instance.getCpChromecastComponent().inject(this)
        Timber.d("init")
        chromecastManagerContext = tryToInitData(context)
        chromecastManagerContext?.let {
            it.first.addCastStateListener(this)
            addMediaRouterCallback(it)
            castRemoteDisplayClient = CastRemoteDisplay.getClient(context)
        }
    }

    private fun tryToInitData(context: Context): ChromecastManagerContext? {
        Timber.d("tryToInitData")
        var chromecastManagerContext: ChromecastManagerContext? = null
        try {
            val chromecastData = getChromecastDataOrThrowException()
            appId = chromecastData.first
            chromecastManagerContext = ChromecastManagerContext(
                    CastContext.getSharedInstance(context),
                    chromecastData.first,
                    chromecastData.second
            )
        } catch (ex: Exception) {
            Timber.e(ex, "ChromecastManager cast context init failed. Chromecast is not available on this device or not setup")
            error.value = ex
        }
        return chromecastManagerContext
    }

    private fun getChromecastDataOrThrowException(): Pair<String, String> {
        val appId = applicationDataProvider.chromecastAppId()
        val appCustomNamespace = applicationDataProvider.chromecastAppCustomNamespace()
        val error = "Chromecast data error: appId: ${applicationDataProvider.chromecastAppId()}, appCustomNamespace: $${applicationDataProvider.chromecastAppCustomNamespace()}"

        appId ?: throw Exception(error)
        appCustomNamespace ?: throw Exception(appCustomNamespace)
        return Pair(appId, appCustomNamespace)
    }

    private fun addMediaRouterCallback(chromecastManagerContext: ChromecastManagerContext) {
        val mediaRouteSelector = MediaRouteSelector.Builder()
                .addControlCategory(CastMediaControlIntent.categoryForCast(chromecastManagerContext.second))
                .build()
        val mediaRouter = MediaRouter.getInstance(context)
        mediaRouter.addCallback(mediaRouteSelector, MediaRouterCallback(this))
    }

    override fun onCastStateChanged(i: Int) {
        Timber.d("CastStateChanged: %s", i)
        castState = i
        refreshState()
    }

    override fun onDeviceSelected(chromecastDevice: ChromecastDevice?) {
        Timber.d("onDeviceSelected")
        this.chromecastDevice = chromecastDevice
        this.chromecastDevice?.mRouteInfo?.select()
        chromecastManagerContext?.first?.sessionManager?.addSessionManagerListener<CastSession>(this, CastSession::class.java)
    }

    override fun onSessionStarting(session: CastSession) {
        Timber.d("SESSION: - onSessionStarting")
    }

    override fun onSessionStarted(session: CastSession, s: String) {
        Timber.d("SESSION: - onSessionStarted")
        session.let {
            connectCastApi(session)
            session.addCastListener(castListener)
        }
    }

    override fun onSessionStartFailed(session: CastSession, i: Int) {
        Timber.d("SESSION: - onSessionStartFailed")
    }

    override fun onSessionEnding(session: CastSession) {
        Timber.d("SESSION: - onSessionEnding")
    }

    override fun onSessionEnded(session: CastSession, i: Int) {
        Timber.d("SESSION: - onSessionEnded")
        releaseSession()
    }

    override fun onSessionResuming(session: CastSession, s: String) {
        Timber.d("SESSION: - onSessionResuming")
    }

    override fun onSessionResumed(session: CastSession, b: Boolean) {
        Timber.d("SESSION: - onSessionResumed")
    }

    override fun onSessionResumeFailed(session: CastSession, i: Int) {
        Timber.d("SESSION: - onSessionResumeFailed")
    }

    override fun onSessionSuspended(session: CastSession, i: Int) {
        Timber.d("SESSION: - onSessionSuspended")
        releaseSession()
    }

    private fun connectCastApi(castSession: CastSession) {
        val chromecastManagerData = chromecastManagerContext ?: return
        val castDevice = CastDevice.getFromBundle(chromecastDevice?.mRouteInfo?.extras) ?: return

        castRemoteDisplayClient?.startRemoteDisplay(castDevice, chromecastManagerData.second,
                CastRemoteDisplay.CONFIGURATION_NONINTERACTIVE, null)
        chromecastPlayer.initChromecastPlayer(castSession, chromecastManagerData.third)
        castApiConnected.callNow()
    }

    private fun disconnectCastApi() {
        Timber.d("disconnectCastApi")
        castRemoteDisplayClient?.stopRemoteDisplay()
    }

    private fun unselectDevice() {
        Timber.d("unselectDevice")
        chromecastManagerContext?.first?.sessionManager?.currentCastSession?.removeCastListener(castListener)
        chromecastManagerContext?.first?.sessionManager?.removeSessionManagerListener<CastSession>(this, CastSession::class.java)
        chromecastDevice?.mRouter?.unselect(MediaRouter.UNSELECT_REASON_UNKNOWN)
    }

    private fun releaseSession() {
        chromecastPlayer.releaseData()
        disconnectCastApi()
        unselectDevice()
    }

    private fun refreshState() {
        state.value = getChromecastStateUseCase.mapChromecastState(castState, error.value)
    }

    fun isConnectingOrConnected(): Boolean = state.value == ChromecastState.CONNECTING
            || state.value == ChromecastState.CONNECTED

    fun endSession() {
        chromecastManagerContext?.first?.sessionManager?.endCurrentSession(true)
    }

    fun setUpMediaRouteButtonSafe(setUpMediaRouteButton: () -> Unit) {
        try {
            setUpMediaRouteButton()
            error.value = null
        } catch (ex: Exception) {
            error.value = ex
        } finally {
            refreshState()
        }
    }
}