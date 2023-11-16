package pl.cyfrowypolsat.cpplayer.concurrentaccess

import android.os.Handler
import android.os.Looper
import com.tinder.scarlet.WebSocket
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import pl.cyfrowypolsat.cpdata.api.concurrentaccess.ConcurrentAccessService
import pl.cyfrowypolsat.cpdata.api.concurrentaccess.request.*
import pl.cyfrowypolsat.cpdata.api.system.SystemService
import pl.cyfrowypolsat.cpplayer.drm.LicenseInfo
import pl.cyfrowypolsat.cpplayer.drm.LicenseListener
import pl.cyfrowypolsat.cpplayercore.events.stats.PlaybackData
import pl.cyfrowypolsat.cpplayercore.events.stats.PlayerAnalyticsListener
import timber.log.Timber

class ConcurrentAccessController(private val systemService: SystemService,
                                 private val deviceInfo: DeviceInfo,
                                 private val mediaId: PlaybackMediaId,
                                 private val concurrentAccessListener: ConcurrentAccessListener) : PlayerAnalyticsListener, LicenseListener {

    companion object {
        private const val AUTHENTICATION_EXPIRED_CODE = 4000
        private const val MAX_RETRY_COUNT = 2
    }

    private var licenseId: String = ""
    private var jwtToken: String = ""
    private var jwtTokenServiceUrl: String = ""

    private var webSocketSubscription: Disposable? = null
    private var forcePlaybackStopSubscription: Disposable? = null
    private var refreshJwtTokenSubscription: Disposable? = null

    private var concurrentAccessService: ConcurrentAccessService? = null
    private var openConnectionRetryCount = 0

    override fun onPlayerClosed(playbackData: PlaybackData) {
        concurrentAccessService?.closeConnection()
        webSocketSubscription?.dispose()
        forcePlaybackStopSubscription?.dispose()
        refreshJwtTokenSubscription?.dispose()
    }

    override fun onLicenseRequestCompleted(licenseInfo: LicenseInfo) {
        val cacInfo = licenseInfo.cacInfo ?: return
        concurrentAccessService = ConcurrentAccessService(cacInfo.serviceUrl)
        jwtToken = cacInfo.authToken
        jwtTokenServiceUrl = cacInfo.authTokenServiceUrl
        licenseId = licenseInfo.licenseId ?: ""
    }

    override fun onDrmKeysLoaded(playbackData: PlaybackData) {
        val service = concurrentAccessService ?: return
        service.openConnection()
        webSocketSubscription = service.observeWebSocketEvent()
                .subscribe(this::onWebSocketEvent, Timber::e)
        forcePlaybackStopSubscription = service.observeForcePlaybackStop()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onForcePlaybackStop, Timber::e)
    }

    private fun onWebSocketEvent(event: WebSocket.Event) {
        when (event) {
            is WebSocket.Event.OnConnectionOpened<*> -> {
                openConnectionRetryCount = 0
                val playbackData = PlaybackStartData(mediaId = mediaId, licenseId = licenseId)
                val playbackStart = PlaybackStart(deviceInfo = deviceInfo,
                        jwtToken = jwtToken,
                        playbackData = playbackData)
                concurrentAccessService?.sendPlaybackStart(playbackStart)
            }
            is WebSocket.Event.OnConnectionClosed -> {
                if (event.shutdownReason.code == AUTHENTICATION_EXPIRED_CODE) {
                    concurrentAccessService?.closeConnection()
                    refreshJwtToken()
                }
            }
            is WebSocket.Event.OnConnectionFailed -> {
                openConnectionRetryCount++
                if (openConnectionRetryCount > MAX_RETRY_COUNT) {
                    concurrentAccessService?.closeConnection()
                    Handler(Looper.getMainLooper()).post {
                        concurrentAccessListener.openConnectionFailed(event.throwable)
                    }
                }
            }
            else -> {}
        }
    }

    private fun onForcePlaybackStop(forcePlaybackStop: ForcePlaybackStop) {
        concurrentAccessService?.closeConnection()
        concurrentAccessListener.forcePlaybackStop(forcePlaybackStop.reason ?: "")
    }

    private fun refreshJwtToken() {
        refreshJwtTokenSubscription = systemService.getCacAuthToken(jwtTokenServiceUrl)
                .subscribe(this::handleRefreshJwtTokenSuccess, Timber::e)
    }

    private fun handleRefreshJwtTokenSuccess(token: String) {
        jwtToken = token
        concurrentAccessService?.openConnection()
    }

}