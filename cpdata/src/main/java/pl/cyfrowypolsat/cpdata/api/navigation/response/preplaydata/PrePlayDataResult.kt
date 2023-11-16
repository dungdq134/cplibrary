package pl.cyfrowypolsat.cpdata.api.navigation.response.preplaydata

import com.google.gson.annotations.SerializedName
import pl.cyfrowypolsat.cpdata.api.common.model.ImageSourceResult

data class PrePlayDataResult(val userAgent: String,
                             val mediaItem: MediaItem,
                             val ads: Ads?,
                             val reporting: Reporting?,
                             val playbackQueue: PlaybackQueue?,
                             val watchedContentData: WatchedContentData?)

// MediaItem
data class MediaItem(val cpid: Int,
                     val id: String,
                     val playback: Playback,
                     val displayInfo: DisplayInfo)

data class DisplayInfo(val title: String,
                       val ageGroup: Int?,
                       val underageClassification: List<String>?,
                       val accessibilityFeatures: List<String>?,
                       val subtitles: List<Subtitle>?,
                       val thumbnails: List<ImageSourceResult>?,
                       @SerializedName("playerOverlays") val playerOverlays: PlayerOverlays?)

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
                       val url: String?,
                       val authorizationServices: AuthorizationServices?) {

    companion object {
        const val DASH_ACCESS_METHOD = "dash"
        const val DIRECT_ACCESS_METHOD = "direct"
        const val HLS_ACCESS_METHOD = "hls"
        const val HLS_TIMESHIFT_ACCESS_METHOD = "hls_timeshift"
    }
}

data class MediaId(val cpid: Int,
                   val id: String)

data class AuthorizationServices(val widevine: WidevineAuthorizationService?,
                                 val pseudo: PseudoAuthorizationService?)

data class WidevineAuthorizationService(val getWidevineLicenseUrl: String?)

data class PseudoAuthorizationService(val getPseudoLicenseUrl: String?)


// Ads
data class Ads(val adserver: String,
               val placements: List<Placement>,
               val tags: List<String>?,
               val extraData: List<ExtraData>?)

data class Placement(val id: String,
                     val position: String)

data class ExtraData(val name: String,
                     val value: String)


// WatchedContentData
data class WatchedContentData(val lastDuration: Int,
                              val seenPercent: Float)


// Reporting
data class Reporting(
        var gprism: GemiusPrism?,
        var playerEvents: PlayerEvents?,
        var gastream: GemiusAudienceStream?,
        var appsFlyer: AppsFlyer?)

data class GemiusPrism(val service: String,
                       val accounts: List<String>,  //minItems(1)
                       val categoryPath: String,
                       val distributor: String,
                       val title: String,
                       val mediaType: String,
                       val duration: Int? = 0,
                       val extraData: Map<String, String>?)

data class PlayerEvents(val service: String,
                        val authToken: String,
                        val originator: String,
                        val interval: Int,
                        val intervalType: String,
                        val authTokenService: VersionedService)

data class AppsFlyer(val devKey: String)

data class VersionedService(@SerializedName("1.0") val firstVersion: Service)

data class Service(val url: String)

data class GemiusAudienceStream(val service: String,
                                val accounts: List<String>, //minItems(1)
                                val mediaId: MediaId,
                                val mediaType: String,
                                val title: String,
                                val distributor: String,
                                val premiereDate: String,
                                val series: String,
                                val typology: String,
                                val functionalCategory: String,
                                val categoryPath: String,
                                val partner: String,
                                val duration: Int? = 0)

data class PlaybackQueue(val successors: List<MediaId>)

// PlayerOverlays
data class PlayerOverlays(val teravoltPlayerOverlay: TeravoltPlayerOverlay?)

data class TeravoltPlayerOverlay(val isEnabled: Boolean,
                                 val autoStart: Boolean,
                                 val type: String)