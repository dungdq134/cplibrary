package pl.cyfrowypolsat.cpplayerexternal.data.model


data class PrePlayData(val mediaItem: MediaItem,
                       val userAgent: String?,
                       val ads: Ads?)

// MediaItem
data class MediaItem(val cpid: Int,
                     val id: String,
                     val playback: Playback,
                     val displayInfo: DisplayInfo)

data class DisplayInfo(val title: String,
                       val ageGroup: Int?,
                       val subtitles: List<Subtitle>?)

data class Subtitle(val src: String,
                    val format: String,
                    val name: String)

data class Playback(val mediaSources: List<MediaSource>,
                    val mediaType: String,
                    val duration: Int?,
                    val timeline: List<TimelineResult>?)

data class TimelineResult(val type: String,
                          val start: Int,
                          val stop: Int)

data class MediaSource(val id: String,
                       val accessMethod: String,
                       val fileFormat: String,
                       val keyId: String?,
                       val url: String?) {

    companion object {
        const val DASH_ACCESS_METHOD = "dash"
        const val DIRECT_ACCESS_METHOD = "direct"
        const val HLS_ACCESS_METHOD = "hls"
        const val HLS_TIMESHIFT_ACCESS_METHOD = "hls_timeshift"
    }
}

// Ads
data class Ads(val adserver: String,
               val placements: List<Placement>,
               val tags: List<String>?,
               val extraData: List<ExtraData>?)

data class Placement(val id: String,
                     val position: String)

data class ExtraData(val name: String,
                     val value: String)