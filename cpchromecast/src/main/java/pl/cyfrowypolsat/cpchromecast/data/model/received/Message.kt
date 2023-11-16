package pl.cyfrowypolsat.cpchromecast.data.model.received

interface Message {
    val type: String
}

data class Error(override val type: String,
                 val data: ErrorData? = null) : Message

data class ErrorData(val code: Int = 0,
                     val userInfo: String? = null)

data class LinearAdPodEnded(override val type: String) : Message

data class LinearAdPodStarted(override val type: String) : Message

data class LinearAdStarted(override val type: String) : Message

data class PlaybackReady(override val type: String,
                         val data: PlaybackReadyData? = null) : Message

data class PlaybackReadyData(val media: Media? = null)

data class Media(val sources: List<Source?>? = null,
                 val title: String? = null)

data class Source(val mediaId: String? = null,
                  val cpid: Int = 0)

data class ReceiverEnvironment(override val type: String,
                               val data: ReceiverEnvironmentData? = null) : Message

data class ReceiverEnvironmentData(val isStableEnvironment: Boolean? = null)