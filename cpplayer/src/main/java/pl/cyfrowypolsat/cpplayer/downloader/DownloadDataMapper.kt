package pl.cyfrowypolsat.cpplayer.downloader

import android.content.Context
import android.webkit.URLUtil
import androidx.media3.exoplayer.offline.Download
import androidx.media3.common.util.Util
import com.google.gson.Gson
import pl.cyfrowypolsat.cpdata.api.common.model.cpidobject.CategoryResult
import pl.cyfrowypolsat.cpdata.api.navigation.response.category.GetCategoryResult
import pl.cyfrowypolsat.cpdata.api.navigation.response.media.GetMediaResult
import pl.cyfrowypolsat.cpplayer.R
import pl.cyfrowypolsat.cpplayer.model.*
import pl.cyfrowypolsat.cpplayercore.configuration.SubtitleInfo
import java.io.File
import java.util.*

class DownloadDataMapper(private val context: Context) {

    fun map(playerData: PlayerData,
            mediaResult: GetMediaResult,
            keyCategoryResult: GetCategoryResult?,
            downloadDateTimestamp: Long = Date().time,
            downloadMaxQuality: Int,
            userId: Int,
            licenseInfo: Pair<Long, Long>): DownloadData {

        val downloadData = playerData.toDownloadData()
        downloadData.downloadMaxQuality = downloadMaxQuality
        downloadData.userId = userId
        if (licenseInfo.second > 0) {
            downloadData.licenseDuration = licenseInfo.second
            downloadData.licenseEndDateTimestamp = Date().time.plus(licenseInfo.first * 1000)
        }
        downloadData.downloadDateTimestamp = downloadDateTimestamp
        downloadData.publicationDateTimestamp = mediaResult.publicationDate?.time
                ?: 0
        downloadData.episodeNumber = mediaResult.episodeNumber
        keyCategoryResult?.let {
            var hasSeries = false

            mediaResult.category?.let { seriesCategory ->
                if (keyCategoryResult.id != seriesCategory.id) {
                    hasSeries = true
                    downloadData.downloadSeries = mapDownloadSeries(seriesCategory)
                }
            }
            downloadData.downloadCategory = mapDownloadCategory(it, hasSeries)
        }
        mediaResult.product?.let {
            downloadData.downloadProductId = DownloadProductId(it.id, it.subType, it.type)
        }
        downloadData.downloadMediaResult = mediaResult
        downloadData.subtitles = mapSubtitles(playerData.playerConfig.subtitles)
        downloadData.reporting = mapDownloadReporting(mediaResult)

        return downloadData
    }

    fun map(download: Download): DownloadData {
        val downloadData = Gson().fromJson(Util.fromUtf8Bytes(download.request.data), DownloadData::class.java)
        val downloadState = if (isLicenseExpiredError(downloadData)) {
            DownloadState.FAILED
        } else {
            DownloadState.fromExoDownloadState(download.state)
        }

        downloadData.downloadState = downloadState
        downloadData.downloadProgress = download.percentDownloaded
        downloadData.bytesSize = download.contentLength
        downloadData.errorMessage = getDownloadErrorText(downloadData)
        return downloadData
    }

    private fun mapDownloadCategory(keyCategoryResult: GetCategoryResult, hasSeries: Boolean): DownloadCategoryData? {
        return DownloadCategoryData(categoryId = keyCategoryResult.id,
                categoryName = keyCategoryResult.name,
                categoryImages = keyCategoryResult.thumbnails?.map { PlayerImageSource(it.size.width, it.size.height, it.src) }
                        ?: listOf(),
                hasSeries = hasSeries,
                reporting = mapDownloadCategoryReporting(keyCategoryResult))
    }

    private fun mapDownloadSeries(seriesCategoryResult: CategoryResult?): DownloadSeriesData? {
        return seriesCategoryResult?.let {
            DownloadSeriesData(seriesId = seriesCategoryResult.id,
                    seriesName = seriesCategoryResult.name,
                    seriesImages = seriesCategoryResult.thumbnails.map { PlayerImageSource(it.size.width, it.size.height, it.src) },
                    seasonNumber = seriesCategoryResult.seasonNumber)
        }
    }

    private fun getDownloadErrorText(downloadData: DownloadData): String? {
        if (downloadData.downloadState == DownloadState.FAILED) {
            if (isLicenseExpiredError(downloadData)) {
                return context.getString(R.string.download_license_expired_error)
            }
            return context.getString(R.string.download_generic_error)
        }
        return null
    }


    private fun mapSubtitles(subtitles: List<SubtitleInfo>): List<DownloadSubtitleInfo> {
        return subtitles.map {
            DownloadSubtitleInfo(url = it.url,
                    localFilePath = getLocalFilePath(it.url),
                    mimeType = it.mimeType,
                    language = it.language)
        }
    }

    private fun isLicenseExpiredError(downloadData: DownloadData): Boolean {
        downloadData.licenseEndDateTimestamp?.let {
            if (it <= Date().time) {
                return true
            }
        }
        return false
    }

    private fun getLocalFilePath(url: String): String {
        val fileName = URLUtil.guessFileName(url, null, null)
        val separator = if (context.filesDir.absolutePath.endsWith(File.separator)) {
            ""
        } else {
            File.separator
        }
        return context.filesDir.absolutePath + separator + fileName
    }

    private fun mapDownloadReporting(mediaResult: GetMediaResult): DownloadReportingData {
//        val contentItem = mediaResult.reporting?.activityEvents?.let { ActivityEventsData.ContentItem(it.contentItem.type, it.contentItem.value) }
//                ?: return DownloadReportingData() //todo waiting for https://jira.polsatc/browse/GM-4820

        val contentItem = ActivityEventsData.ContentItem(mediaResult.mediaType, mediaResult.id)
        return DownloadReportingData(ActivityEventsData(contentItem))
    }

    private fun mapDownloadCategoryReporting(keyCategoryResult: GetCategoryResult): DownloadReportingData {
        val contentItem = keyCategoryResult.reporting?.activityEvents?.let { ActivityEventsData.ContentItem(it.contentItem.type, it.contentItem.value) }
                ?: return DownloadReportingData()
        return DownloadReportingData(ActivityEventsData(contentItem))
    }
}