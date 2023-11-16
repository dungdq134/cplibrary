package pl.cyfrowypolsat.cpdata.api.common.websocket

import com.tinder.scarlet.retry.BackoffStrategy

class WebSocketBackoffStrategy() : BackoffStrategy {

    override fun backoffDurationMillisAt(retryCount: Int): Long {
        return 5000
    }

}