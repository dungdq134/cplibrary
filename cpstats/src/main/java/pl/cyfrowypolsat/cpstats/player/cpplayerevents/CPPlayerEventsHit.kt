package pl.cyfrowypolsat.cpstats.player.cpplayerevents

import com.google.gson.annotations.SerializedName
import java.util.*

data class CPPlayerEventsHit(@SerializedName("data") val data: HitData?,
                             @SerializedName("jwt") val jwt: String,
                             @SerializedName("portalId") val portal: String,
                             @SerializedName("originator") val originator: String,
                             @SerializedName("version") val apiVersion: String,
                             @SerializedName("eventDate") val eventDate: Date,
                             @SerializedName("type") val eventType: EventType,
                             @SerializedName("eventId") val eventId: String,
                             @SerializedName("traceId") val traceId: String) {

    enum class EventType {
        @SerializedName("PlayerInitialized")
        INITIALIZED,

        @SerializedName("PlayerBeganPlay")
        BEGAN_PLAY,

        @SerializedName("PlayerBeganDrm")
        BEGAN_DRM,

        @SerializedName("PlayerCycleHit")
        CYCLE,

        @SerializedName("PlayerQualityChanged")
        QUALITY_CHANGED,

        @SerializedName("PlayerEndedPlay")
        ENDED_PLAY,

        @SerializedName("PlayerClosed")
        CLOSED,

        @SerializedName("PlayerInterrupted")
        INTERRUPTED,

        @SerializedName("PlayerSeek")
        SEEK,

        @SerializedName("PlayerPaused")
        PAUSED,

        @SerializedName("PlayerUnPaused")
        UNPAUSED,

        @SerializedName("PlayerStartedBuffering")
        BUFFERING_STARTED,

        @SerializedName("PlayerStoppedBuffering")
        BUFFERING_STOPPED,

        @SerializedName("PlayerGenericError")
        GENERIC_ERROR,

        @SerializedName("PlayerLicenseRequestStarted")
        LICENSE_REQUEST_STARTED,

        @SerializedName("PlayerLicenseRequestCompleted")
        LICENSE_REQUEST_COMPLETED,

        @SerializedName("PlayerBeganAdvertBlock")
        CONTENT_PAUSE_REQUESTED,

        @SerializedName("PlayerEndedAdvertBlock")
        CONTENT_RESUME_REQUESTED,

        @SerializedName("PlayerOverlayStateChanged")
        OVERLAY_STATE_CHANGED,

        @SerializedName("AdvertBeganBlock")
        BEGAN_AD_BLOCK,

        @SerializedName("AdvertEndedBlock")
        ENDED_AD_BLOCK,

        @SerializedName("AdvertBegan")
        BEGAN_AD,

        @SerializedName("AdvertEnded")
        ENDED_AD,

        @SerializedName("AdvertGenericError")
        GENERIC_ADVERT_ERROR,

        @SerializedName("AdvertPaused")
        AD_PAUSED,

        @SerializedName("AdvertUnPaused")
        AD_UNPAUSED,

        @SerializedName("AdvertFirstQuartile")
        AD_FIRST_QUARTILE,

        @SerializedName("AdvertMidPoint")
        AD_MID_POINT,

        @SerializedName("AdvertThirdQuartile")
        AD_THIRD_QUARTILE
    }

    interface HitData

    data class DefaultHitData(@SerializedName("userAgentData") val userAgentData: UserAgentData,
                              @SerializedName("ipData") val ipData: IpData,
                              @SerializedName("status") val status: Status,
                              @SerializedName("errorCode") val errorCode: String?,
                              @SerializedName("deviceExtraData") val deviceExtraData: DeviceExtraData,
                              @SerializedName("playbackTraceId") val playbackTraceId: String,
                              @SerializedName("media") val media: MediaData,
                              @SerializedName("source") val source: SourceData,
                              @SerializedName("sellModel") val sellModel: String?,
                              @SerializedName("licenseId") val licenseId: String?,
                              @SerializedName("score") val score: Double,
                              @SerializedName("durationSeconds") val durationSeconds: Int,
                              @SerializedName("positionSeconds") val positionSeconds: Int,
                              @SerializedName("quality") val quality: String,
                              @SerializedName("bitrate") val bitrate: Int,
                              @SerializedName("frames") val frames: FramesData,
                              @SerializedName("bytesLoaded") val bytesLoaded: BytesData,
                              @SerializedName("volume") val volume: Int,
                              @SerializedName("clientId") val clientId: String,
                              @SerializedName("deviceId") val deviceId: DeviceId,
                              @SerializedName("profileId") val profileId: String?,
                              @SerializedName("context") val playerContext: PlayerContextData?,
                              @SerializedName("advertsRequestUrl") val advertsRequestUrl: String?,
                              @SerializedName("advertsCuePoints") val advertsCuePoints: List<Float>?,
                              @SerializedName("advertBlockData") val advertBlockData: AdvertBlockData?,
                              @SerializedName("activeOverlays") val activeOverlays: List<ActiveOverlay>?) : HitData

    data class AdvertHitData(@SerializedName("userAgentData") val userAgentData: UserAgentData,
                    @SerializedName("ipData") val ipData: IpData,
                    @SerializedName("status") val status: Status,
                    @SerializedName("errorCode") val errorCode: String?,
                    @SerializedName("deviceExtraData") val deviceExtraData: DeviceExtraData,
                    @SerializedName("playbackTraceId") val playbackTraceId: String,
                    @SerializedName("clientId") val clientId: String,
                    @SerializedName("deviceId") val deviceId: DeviceId,
                    @SerializedName("profileId") val profileId: String?,
                    @SerializedName("advertData") val advertData: AdvertData?,
                    @SerializedName("advertBlockData") val advertBlockData: AdvertBlockData?) : HitData

    data class UserAgentData(@SerializedName("portal") val portal: String,
                             @SerializedName("deviceType") val deviceType: String,
                             @SerializedName("application") val application: String,
                             @SerializedName("player") val player: String,
                             @SerializedName("build") val build: Int,
                             @SerializedName("os") val os: String,
                             @SerializedName("osInfo") val osInfo: String,
                             @SerializedName("widevine") val widevine: Boolean)

    data class IpData(@SerializedName("ip") val ip: String,
                      @SerializedName("country") val country: String?,
                      @SerializedName("isEu") val isEu: Boolean?,
                      @SerializedName("isVpn") val isVpn: Boolean?,
                      @SerializedName("isp") val isp: String?,
                      @SerializedName("continent") val continent: String?)

    enum class Status {

        @SerializedName("success")
        SUCCESS,

        @SerializedName("failed")
        FAILED
    }

    data class MediaData(@SerializedName("cpid") val cpid: Int,
                         @SerializedName("id") val id: String,
                         @SerializedName("type") val type: String)

    data class SourceData(@SerializedName("id") val id: String,
                          @SerializedName("accessMethod") val accessMethod: String,
                          @SerializedName("fileFormat") val fileFormat: String,
                          @SerializedName("drmType") val drmType: String?)

    data class FramesData(@SerializedName("dropped") val dropped: Long,
                          @SerializedName("total") val total: Long)

    data class BytesData(@SerializedName("audio") val audio: Long,
                         @SerializedName("video") val video: Long,
                         @SerializedName("total") val total: Long)

    data class DeviceExtraData(@SerializedName("manufacturer") val manufacturer: String,
                               @SerializedName("model") val model: String,
                               @SerializedName("screenSize") val screenSize: ScreenSize?)

    data class ScreenSize(@SerializedName("height") val height: Int,
                          @SerializedName("width") val width: Int,
                          @SerializedName("diagonal") val diagonal: Double)

    data class PlayerContextData(@SerializedName("type") val type: String,
                                 @SerializedName("value") val value: String?)

    data class DeviceId(@SerializedName("type") val type: String,
                        @SerializedName("value") val value: String)

    data class AdvertData(
            @SerializedName("id") val id: String,
            @SerializedName("indexInBlock") val indexInBlock: Int,
            @SerializedName("durationSeconds") val durationSeconds: Int)

    enum class AdvertBlockType {

        @SerializedName("preroll")
        PREROLL,
        @SerializedName("midroll")
        MIDROLL,
        @SerializedName("postroll")
        POSTROLL;
    }

    data class AdvertBlockData(
            @SerializedName("blockType") val blockType: AdvertBlockType,
            @SerializedName("adsInBlock") val totalAdsCount: Int,
            @SerializedName("blockIndex") val blockIndex: Int,
            @SerializedName("timeOffsetSeconds") val timeOffsetSeconds: Double)

    data class ActiveOverlay(
        @SerializedName("type") val type: OverlayType,
        @SerializedName("autoStart") val autoStart: Boolean,
    )

    enum class OverlayType {
        @SerializedName("teravolt")
        TERAVOLT;
    }
}