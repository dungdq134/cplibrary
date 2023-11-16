package pl.cyfrowypolsat.cpchromecast.presentation.player

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.cast.Cast.MessageReceivedCallback
import com.google.android.gms.cast.CastDevice
import com.google.android.gms.cast.framework.CastSession
import com.google.android.gms.cast.framework.media.RemoteMediaClient
import pl.cyfrowypolsat.cpchromecast.core.utils.LogUtils
import pl.cyfrowypolsat.cpchromecast.domain.enums.ChromecastPlaybackState
import pl.cyfrowypolsat.cpchromecast.domain.listeners.OnDataReceivedListener
import pl.cyfrowypolsat.cpchromecast.domain.model.ad.AdStateInfo
import pl.cyfrowypolsat.cpchromecast.domain.model.error.ChromecastError
import pl.cyfrowypolsat.cpchromecast.domain.model.media.ChromecastMediaInfo
import pl.cyfrowypolsat.cpchromecast.domain.model.media.ChromecastPlayerData
import pl.cyfrowypolsat.cpchromecast.domain.model.receiver.ReceiverInfo
import pl.cyfrowypolsat.cpchromecast.domain.usecase.GetReceivedMessageUseCase
import pl.cyfrowypolsat.cpchromecast.domain.usecase.GetStartPlayerJsonUseCase
import pl.cyfrowypolsat.cpcommon.presentation.lifecycle.SingleLiveEvent
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

typealias ChromecastPlayerSession = Pair<CastSession, String>

class ChromecastPlayer
@Inject constructor(private val getStartPlayerJsonUseCase: GetStartPlayerJsonUseCase,
                    private val getReceivedMessageUseCase: GetReceivedMessageUseCase): RemoteMediaClient.Callback(), MessageReceivedCallback {

    companion object {
        const val TAG = "ChromecastPlayer"
    }

    private var chromecastPlayerSession: ChromecastPlayerSession? = null

    var remoteMediaClient: RemoteMediaClient? = null
        private set

    var chromecastMediaInfo: ChromecastMediaInfo? = null
        private set

    val playbackState = MutableLiveData<ChromecastPlaybackState>()
    val playbackError = SingleLiveEvent<ChromecastError>()
    val playbackStarting = SingleLiveEvent<Void>()
    val unstableEnvironmentDetected = SingleLiveEvent<Void>()

    fun initChromecastPlayer(castSession: CastSession, chromecastCustomNamespace: String) {
        val chromecastPlayerSession = ChromecastPlayerSession(castSession, chromecastCustomNamespace)
        remoteMediaClient = chromecastPlayerSession.first.remoteMediaClient
        this.chromecastPlayerSession = chromecastPlayerSession
        try {
            castSession.setMessageReceivedCallbacks(chromecastPlayerSession.second, this)
            remoteMediaClient?.registerCallback(this)
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override fun onMessageReceived(castDevice: CastDevice, namespace: String, message: String) {
        LogUtils.logLongString(TAG, "Receiver message $message")
        try {
            getReceivedMessageUseCase.getReceivedMessage(message, object : OnDataReceivedListener {
                override fun onDataReceived(obj: Any) {
                    when (obj) {
                        is ChromecastError -> {
                            playbackError.value = obj
                        }
                        is ChromecastMediaInfo -> {
                            chromecastMediaInfo = obj
                            playbackStarting.callNow()
                            playbackState.value = ChromecastPlaybackState.MEDIA
                        }
                        is AdStateInfo -> {
                            playbackState.value = if (obj.isAdPlaying) {
                                ChromecastPlaybackState.AD
                            } else {
                                ChromecastPlaybackState.MEDIA
                            }
                        }
                        is ReceiverInfo -> {
                            if (!obj.isStableEnvironment) {
                                unstableEnvironmentDetected.callNow()
                            }
                        }
                    }
                }
            })
        } catch (e: IOException) {
            Timber.e(e)
        }
    }

    override fun onStatusUpdated() {
        val remoteMediaClient = this.remoteMediaClient
        if (remoteMediaClient != null && !remoteMediaClient.hasMediaSession()) {
            chromecastMediaInfo = null
        }
    }

    fun cast(chromecastPlayerData: ChromecastPlayerData) {
        val chromecastPlayerSession = chromecastPlayerSession ?: return
        if(this.chromecastMediaInfo?.toChromecastMediaId() == chromecastPlayerData.chromecastMediaId) return

        try {
            val message = getStartPlayerJsonUseCase.getStartPlayerJson(
                    chromecastPlayerData.chromecastMediaId,
                    chromecastPlayerData.chromecastUserAuthData,
                    chromecastPlayerData.forcePlay,
                    chromecastPlayerData.portal,
                    chromecastPlayerData.startPositionSeconds
            )
            LogUtils.logLongString(TAG, "Sender StartPlayerJson $message")

            chromecastPlayerSession.first.sendMessage(chromecastPlayerSession.second, message)
        } catch (ex: Exception) {
            Timber.e(ex)
        }
    }

    fun releaseData() {
        remoteMediaClient?.unregisterCallback(this)
        chromecastPlayerSession = null
        remoteMediaClient = null
        chromecastMediaInfo = null
    }

}