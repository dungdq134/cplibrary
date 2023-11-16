package pl.cyfrowypolsat.cpdata.api.concurrentaccess

import com.google.gson.Gson
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.messageadapter.gson.GsonMessageAdapter
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import io.reactivex.rxjava3.core.Flowable
import okhttp3.OkHttpClient
import pl.cyfrowypolsat.cpdata.CpData
import pl.cyfrowypolsat.cpdata.api.common.websocket.WebSocketBackoffStrategy
import pl.cyfrowypolsat.cpdata.api.common.websocket.WebSocketLifecycle
import pl.cyfrowypolsat.cpdata.api.concurrentaccess.request.ForcePlaybackStop
import pl.cyfrowypolsat.cpdata.api.concurrentaccess.request.PlaybackStart
import pl.cyfrowypolsat.cpdata.common.streamadapter.RxJava3StreamAdapterFactory
import pl.cyfrowypolsat.cpdata.di.CpDataModule
import pl.cyfrowypolsat.cpdata.di.CpDataQualifier
import pl.cyfrowypolsat.cpdata.di.CpDataWebSocketOkHttp
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Named

class ConcurrentAccessService(serviceUrl: String) {

    companion object {
        private const val TAG = "WebSocket"
    }

    @CpDataWebSocketOkHttp
    @Inject
    lateinit var okHttpClient: OkHttpClient

    @CpDataQualifier
    @Inject
    lateinit var gson: Gson

    private var webSocketLifecycle = WebSocketLifecycle()
    private var concurrentAccessApi: ConcurrentAccessApi

    init {
        CpData.getInstance().component.inject(this)
        concurrentAccessApi = Scarlet.Builder()
                .webSocketFactory(okHttpClient.newWebSocketFactory(serviceUrl))
                .lifecycle(webSocketLifecycle)
                .backoffStrategy(WebSocketBackoffStrategy())
                .addMessageAdapterFactory(GsonMessageAdapter.Factory(gson))
                .addStreamAdapterFactory(RxJava3StreamAdapterFactory())
                .build()
                .create()
    }


    fun openConnection() {
        webSocketLifecycle.start()
    }

    fun closeConnection() {
        webSocketLifecycle.stop()
    }

    fun observeWebSocketEvent(): Flowable<WebSocket.Event> {
        return concurrentAccessApi.observeWebSocketEvent()
                .doOnNext { event: WebSocket.Event? -> Timber.tag(TAG).d(event?.toString()) }
    }

    fun observeForcePlaybackStop(): Flowable<ForcePlaybackStop> {
        return concurrentAccessApi.observeForcePlaybackStop()
                .filter { it.action == "forcePlaybackStop" }
    }

    fun sendPlaybackStart(playbackStart: PlaybackStart) {
        Timber.tag(TAG).d("Sending: $playbackStart")
        concurrentAccessApi.sendPlaybackStart(playbackStart)
    }

}