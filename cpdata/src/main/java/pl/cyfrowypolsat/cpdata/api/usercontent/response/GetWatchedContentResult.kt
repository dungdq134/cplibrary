package pl.cyfrowypolsat.cpdata.api.usercontent.response

import java.util.*

data class GetWatchedContentResult(val mediaId: MediaId,
                                   val lastWatchTime: Date,
                                   val lastDuration: Int,
                                   val lastPercent: Float,
                                   val seenDuration: Int,
                                   val seenPercent: Float) {

    companion object {
        fun List<GetWatchedContentResult>?.findLastWatchedPercent(mediaId: String?): Int {
            mediaId ?: return 0
            return this?.firstOrNull { it.mediaId.id == mediaId }?.lastPercent?.toInt() ?: 0
        }
    }
}

data class MediaId(val cpid: Int,
                   val id: String)