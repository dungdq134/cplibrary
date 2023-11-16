package pl.cyfrowypolsat.cpplayercore.core

import androidx.media3.common.PlaybackException

class PlayerException(cause: Throwable,
                      val code: Int) : Exception(cause) {

    companion object {

        fun create(e: PlaybackException): PlayerException {
            return PlayerException(cause = e, code = e.errorCode)
        }
    }

}
