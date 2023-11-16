package pl.cyfrowypolsat.cpdata.api.concurrentaccess

import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import io.reactivex.rxjava3.core.Flowable
import pl.cyfrowypolsat.cpdata.api.concurrentaccess.request.ForcePlaybackStop
import pl.cyfrowypolsat.cpdata.api.concurrentaccess.request.PlaybackStart

interface ConcurrentAccessApi {

    @Receive
    fun observeWebSocketEvent(): Flowable<WebSocket.Event>

    @Receive
    fun observeForcePlaybackStop(): Flowable<ForcePlaybackStop>

    @Send
    fun sendPlaybackStart(playbackStart: PlaybackStart)

}