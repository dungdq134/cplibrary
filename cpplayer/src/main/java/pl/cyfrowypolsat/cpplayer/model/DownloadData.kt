package pl.cyfrowypolsat.cpplayer.model

import androidx.media3.exoplayer.offline.Download
import pl.cyfrowypolsat.cpdata.api.navigation.response.media.GetMediaResult
import pl.cyfrowypolsat.cpplayercore.configuration.MediaType
import pl.cyfrowypolsat.cpplayercore.configuration.PlayerConfig
import pl.cyfrowypolsat.cpplayercore.configuration.SubtitleInfo

data class DownloadData(val id: String,
                        val cpid: Int,
                        val url: String,
                        val mediaType: MediaType,
                        val title: String,
                        val ageGroup: Int? = null,
                        val userAgent: String,
                        val images: List<PlayerImageSource>,
                        var userId: Int = -1,
                        var downloadDateTimestamp: Long = 0,
                        var publicationDateTimestamp: Long = 0,
                        var episodeNumber: Int? = null,
                        var downloadState: DownloadState = DownloadState.UNKNOWN,
                        var downloadProgress: Float = 0f,
                        var errorMessage: String? = null,
                        var bytesSize: Long = 0,
                        var downloadMaxQuality: Int = 0,
                        var licenseDuration: Long? = null,
                        var licenseEndDateTimestamp: Long? = null,
                        var downloadProductId: DownloadProductId? = null,
                        var downloadCategory: DownloadCategoryData? = null,
                        var downloadSeries: DownloadSeriesData? = null,
                        var downloadMediaResult: GetMediaResult? = null,
                        var subtitles: List<DownloadSubtitleInfo>? = listOf(),
                        var reporting: DownloadReportingData? = null)

data class DownloadCategoryData(val categoryId: String,
                                val categoryName: String,
                                val categoryImages: List<PlayerImageSource>,
                                val hasSeries: Boolean,
                                val reporting: DownloadReportingData?)

data class DownloadSeriesData(val seriesId: String,
                              val seriesName: String,
                              val seriesImages: List<PlayerImageSource>,
                              val seasonNumber: Int? = null)

data class DownloadProductId(val id: String,
                             val subtype: String,
                             val type: String)

data class DownloadReportingData(val activityEventsData: ActivityEventsData? = null)

data class ActivityEventsData(val contentItem: ContentItem) {

    data class ContentItem(val type: String,
                           val value: String = "")
}

enum class DownloadState {
    UNKNOWN,
    QUEUED,
    STOPPED,
    DOWNLOADING,
    COMPLETED,
    FAILED,
    REMOVING,
    RESTARTING;

    companion object {

        fun fromExoDownloadState(@Download.State state: Int): DownloadState {
            return when (state) {
                Download.STATE_QUEUED -> QUEUED
                Download.STATE_STOPPED -> STOPPED
                Download.STATE_DOWNLOADING -> DOWNLOADING
                Download.STATE_COMPLETED -> COMPLETED
                Download.STATE_FAILED -> FAILED
                Download.STATE_REMOVING -> REMOVING
                Download.STATE_RESTARTING -> RESTARTING
                else -> UNKNOWN
            }
        }
    }
}

fun DownloadData.toPlayerConfig(): PlayerConfig {
    return PlayerConfig(id = id,
            url = url,
            mediaType = mediaType,
            title = title,
            ageGroup = ageGroup,
            userAgent = userAgent,
            subtitles = subtitles?.let { subtitle -> subtitle.map { SubtitleInfo(it.localFilePath, it.mimeType, it.language) }} ?: listOf())
}