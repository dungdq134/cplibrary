package pl.cyfrowypolsat.cpplayer.downloader

import androidx.media3.common.util.Util
import com.google.gson.Gson
import io.reactivex.rxjava3.core.Observable
import pl.cyfrowypolsat.cpdata.api.navigation.NavigationService
import pl.cyfrowypolsat.cpdata.api.navigation.request.category.GetCategoryParams
import pl.cyfrowypolsat.cpdata.api.navigation.request.media.GetMediaParams
import pl.cyfrowypolsat.cpdata.api.navigation.response.category.GetCategoryResult
import pl.cyfrowypolsat.cpdata.api.navigation.response.media.GetMediaResult
import pl.cyfrowypolsat.cpdata.common.manager.AccountManager
import pl.cyfrowypolsat.cpdata.common.manager.AppDataManager
import pl.cyfrowypolsat.cpdata.files.FilesManager
import pl.cyfrowypolsat.cpplayer.GetPlayerDataUseCase
import pl.cyfrowypolsat.cpplayer.model.*
import pl.cyfrowypolsat.cpplayercore.core.download.DownloadController
import pl.cyfrowypolsat.cpplayercore.core.download.OfflineLicenseController
import pl.cyfrowypolsat.cpplayercore.core.extensions.copyWithData
import timber.log.Timber
import java.util.*

class DownloadsUseCase(private val downloadController: DownloadController,
                       private val offlineLicenseController: OfflineLicenseController,
                       private val getPlayerDataUseCase: GetPlayerDataUseCase,
                       private val accountManager: AccountManager,
                       private val appDataManager: AppDataManager,
                       private val navigationService: NavigationService,
                       private val filesManager: FilesManager,
                       private val downloadDataMapper: DownloadDataMapper) {

    fun startDownload(mediaId: String,
                      cpid: Int): Observable<DownloadData> {
        val getMediaParams = GetMediaParams(mediaId, cpid)
        val maxQuality = DownloadQuality.from(appDataManager.downloadQuality).maxHeight
        return navigationService.getMedia(getMediaParams).flatMap { mediaResult ->
            mediaResult.category?.keyCategoryId?.let {
                val getCategoryParams = GetCategoryParams(it)
                navigationService.getCategory(getCategoryParams).flatMap { keyCategoryResult ->
                    startDownload(mediaId, cpid, maxQuality, mediaResult, keyCategoryResult)
                }
            } ?: startDownload(mediaId, cpid, maxQuality, mediaResult, null)
        }
    }

    fun retryDownload(downloadData: DownloadData): Observable<DownloadData> {
        val getMediaParams = GetMediaParams(downloadData.id, downloadData.cpid)
        return navigationService.getMedia(getMediaParams).flatMap { mediaResult ->
            mediaResult.category?.keyCategoryId?.let {
                val getCategoryParams = GetCategoryParams(it)
                navigationService.getCategory(getCategoryParams).flatMap { keyCategoryResult ->
                    startDownload(downloadData.id, downloadData.cpid, downloadData.downloadMaxQuality, mediaResult, keyCategoryResult, downloadData.downloadDateTimestamp)
                }
            }
                    ?: startDownload(downloadData.id, downloadData.cpid, downloadData.downloadMaxQuality, mediaResult, null, downloadData.downloadDateTimestamp)
        }
    }

    private fun startDownload(mediaId: String, cpid: Int, maxQuality: Int, mediaResult: GetMediaResult, keyCategoryResult: GetCategoryResult?, downloadDateTimestamp: Long = Date().time): Observable<DownloadData> {
        return getPlayerDataUseCase.getPlayerData(mediaId, cpid, null, true)
                .flatMap { playerData ->
                    downloadController.buildDownloadRequest(playerData.playerConfig, maxQuality)
                            .flatMap { downloadRequest ->
                                val licenseInfo = offlineLicenseController.getLicenseDurationInfo(downloadRequest)
                                val userId = accountManager.getUserId()!!

                                val downloadData = downloadDataMapper.map(playerData = playerData,
                                        mediaResult = mediaResult,
                                        keyCategoryResult = keyCategoryResult,
                                        downloadDateTimestamp = downloadDateTimestamp,
                                        downloadMaxQuality = maxQuality,
                                        userId = userId,
                                        licenseInfo = licenseInfo)

                                val data = Util.getUtf8Bytes(Gson().toJson(downloadData))
                                downloadController.startDownload(downloadRequest.copyWithData(data)).flatMap {
                                    downloadData.downloadState = DownloadState.QUEUED
                                    startDownloadSubtitles(downloadData.subtitles)
                                    Observable.just(downloadData)
                                }
                            }
                }

    }

    private fun startDownloadSubtitles(subtitles: List<DownloadSubtitleInfo>?) {
        subtitles?.forEach {
            filesManager.startDownload(it.url, it.localFilePath)
        }
    }

    fun removeDownload(mediaId: String): Observable<Boolean> {
        return removeSubtitles(mediaId)
                .doOnNext {
                    downloadController.removeDownload(mediaId)
                }
    }

    fun removeDownloads(mediaIdList: List<String>): Observable<Boolean> {
        return removeSubtitles(mediaIdList)
                .doOnNext {
                    downloadController.removeDownloads(mediaIdList)
                }
    }

    fun removeAllDownloads(): Observable<Boolean> {
        return removeAllSubtitles()
                .doOnNext {
                    downloadController.removeAllDownloads()
                }
    }

    private fun removeAllSubtitles(): Observable<Boolean> {
        return getDownloads()
                .flatMap { downloads ->
                    removeSubtitles(downloads.map { it.id })
                }
    }

    private fun removeSubtitles(mediaIdList: List<String>): Observable<Boolean> {
        return Observable.fromIterable(mediaIdList)
                .flatMap { mediaId ->
                    removeSubtitles(mediaId)
                }
                .toList()
                .toObservable()
                .map { true }
    }

    private fun removeSubtitles(mediaId: String): Observable<Boolean> {
        return getDownload(mediaId)
                .map { downloadData ->
                    downloadData.subtitles?.forEach { subtitleInfo ->
                        filesManager.removeFile(subtitleInfo.localFilePath)
                    }
                    true
                }
    }

    fun restartDownload(mediaId: String): Observable<Boolean> {
        return downloadController.restartDownload(mediaId)
    }

    fun getDownload(mediaId: String): Observable<DownloadData> {
        if (!accountManager.isUserLogged()) {
            return Observable.error(UserUnloggedException())
        }

        return downloadController.checkIfDownloadExists(mediaId).flatMap {
            if (it) {
                downloadController.loadDownload(mediaId)
                        .map {
                            val downloadData = downloadDataMapper.map(it)
                            if (downloadData.userId != accountManager.getUserId() || downloadData.downloadState == DownloadState.REMOVING) {
                                throw NoDownloadDataException(mediaId)
                            }
                            downloadData
                        }
            } else {
                throw NoDownloadDataException(mediaId)
            }
        }
    }

    fun hasAnyDownloads(): Observable<Boolean> {
        return downloadController.loadDownloads()
                .map { list ->
                    list.isNotEmpty()
                }
    }

    fun getDownloads(): Observable<List<DownloadData>> {
        if (!accountManager.isUserLogged()) {
            return Observable.just(listOf())
        }

        return downloadController.loadDownloads()
                .map { list ->
                    val result = list.map { it ->
                        downloadDataMapper.map(it)
                    }.filter {
                        it.userId == accountManager.getUserId() && it.downloadState != DownloadState.REMOVING
                    }.sortedByDescending { it.downloadDateTimestamp }
                    result
                }
    }

    fun getDownloadsFromCategory(categoryId: String): Observable<List<DownloadData>> {
        if (!accountManager.isUserLogged()) {
            return Observable.just(listOf())
        }

        return downloadController.loadDownloads()
                .map { list ->
                    val result = list.map { it ->
                        downloadDataMapper.map(it)
                    }.filter {
                        it.downloadCategory?.categoryId == categoryId && it.userId == accountManager.getUserId() && it.downloadState != DownloadState.REMOVING
                    }.sortedWith(compareBy<DownloadData, Int?>(nullsLast()) { it.downloadSeries?.seasonNumber }.thenBy(nullsLast()) { it.episodeNumber })
                    result
                }
    }

    fun getDownloadsInProgress(): Observable<List<DownloadData>> {
        return getDownloads().map { downloads ->
            downloads.filter {
                it.downloadState == DownloadState.DOWNLOADING || it.downloadState == DownloadState.QUEUED
            }
        }
    }

    fun getDownloadedPlayerData(mediaId: String): Observable<PlayerData> {
        if (!accountManager.isUserLogged()) {
            return Observable.error(UserUnloggedException())
        }

        return downloadController.checkIfDownloadExists(mediaId).flatMap {
            if (it) {
                downloadController.loadDownload(mediaId)
                        .map {
                            val downloadData = Gson().fromJson(Util.fromUtf8Bytes(it.request.data), DownloadData::class.java)
                            if (downloadData.userId != accountManager.getUserId()) {
                                throw NoDownloadDataException(mediaId)
                            }
                            PlayerData(playerConfig = downloadData.toPlayerConfig(),
                                    images = downloadData.images,
                                    downloadRequest = it.request,
                                    successor = null)
                        }
            } else {
                throw NoDownloadDataException(mediaId)
            }
        }
    }

    fun setDownloadOwnerUserId(userId: Int?): Observable<Boolean> {
        val currentDownloadOwnerUserId = appDataManager.downloadOwnerUserId
        Timber.d("currentDownloadOwnerUserId: $currentDownloadOwnerUserId newUserId: $userId")
        userId?.let {
            if (currentDownloadOwnerUserId != it) {
                appDataManager.downloadOwnerUserId = it
                return downloadController.removeAllDownloads()
            }
        }
        return Observable.just(true)
    }

    fun downloadParametersChanged(hasWifiConnection: Boolean, onlyWifiDownload: Boolean): Observable<Boolean> {
        return if (onlyWifiDownload) {
            if (hasWifiConnection) {
                downloadController.resumeDownloads()
            } else {
                downloadController.pauseDownloads()
            }
        } else {
            downloadController.resumeDownloads()
        }
    }
}