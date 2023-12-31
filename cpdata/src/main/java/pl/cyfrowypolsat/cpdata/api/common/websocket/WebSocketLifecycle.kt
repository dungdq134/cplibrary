package pl.cyfrowypolsat.cpdata.api.common.websocket

import com.tinder.scarlet.Lifecycle
import com.tinder.scarlet.ShutdownReason
import com.tinder.scarlet.lifecycle.LifecycleRegistry


class WebSocketLifecycle(private val lifecycleRegistry: LifecycleRegistry = LifecycleRegistry()
) : Lifecycle by lifecycleRegistry {

    fun start() {
        lifecycleRegistry.onNext(Lifecycle.State.Started)
    }

    fun stop() {
        lifecycleRegistry.onNext(Lifecycle.State.Stopped.WithReason(ShutdownReason.GRACEFUL))
    }

}
