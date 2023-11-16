package pl.cyfrowypolsat.cpplayer.concurrentaccess

interface ConcurrentAccessListener {

    fun forcePlaybackStop(reason: String)
    fun openConnectionFailed(throwable: Throwable)

}