package pl.cyfrowypolsat.cpplayercore.core.download

import android.content.Context
import androidx.media3.exoplayer.offline.Download
import androidx.media3.exoplayer.offline.DownloadManager
import androidx.media3.exoplayer.offline.DownloadRequest
import androidx.media3.exoplayer.offline.DownloadService
import io.reactivex.rxjava3.core.Observable
import pl.cyfrowypolsat.cpplayercore.configuration.PlayerConfig


class DownloadController(private val downloadManager: DownloadManager,
                         private val downloadServiceClass: Class<out DownloadService>,
                         private val context: Context) {

    fun checkIfDownloadExists(downloadId: String): Observable<Boolean> {
        return Observable.fromCallable {
            val download = downloadManager.downloadIndex.getDownload(downloadId)
            download?.let {
                true
            } ?: false
        }
    }

    fun loadDownload(downloadId: String): Observable<Download> {
        return Observable.fromCallable { downloadManager.downloadIndex.getDownload(downloadId) }
    }

    fun loadDownloads(): Observable<List<Download>> {
        return Observable.fromCallable {
            val result: MutableList<Download> = mutableListOf()

            downloadManager.downloadIndex.getDownloads().use { loadedDownloads ->
                while (loadedDownloads.moveToNext()) {
                    val download: Download = loadedDownloads.download
                    result.add(download)
                }
            }
            result
        }
    }

    fun buildDownloadRequest(playerConfig: PlayerConfig,
                             maxQuality: Int): Observable<DownloadRequest> {
        val buildDownloadRequestUseCase = BuildDownloadRequestUseCase(playerConfig, maxQuality, context)
        return buildDownloadRequestUseCase.buildDownloadRequest()
    }

    fun startDownload(downloadRequest: DownloadRequest): Observable<Boolean> {
        DownloadService.sendAddDownload(context, downloadServiceClass, downloadRequest, false)
        return Observable.just(true)
    }

    fun restartDownload(downloadId: String): Observable<Boolean> {
        val download = downloadManager.downloadIndex.getDownload(downloadId)
        download?.let {
            DownloadService.sendAddDownload(context, downloadServiceClass, it.request, false)
            return Observable.just(true)
        }
        return Observable.just(false)
    }

    fun removeDownload(downloadId: String): Observable<Boolean> {
        DownloadService.sendRemoveDownload(context, downloadServiceClass, downloadId, false)
        return Observable.just(true)
    }

    fun removeDownloads(downloadIdList: List<String>): Observable<Boolean> {
        downloadIdList.forEach { downloadId ->
            DownloadService.sendRemoveDownload(context, downloadServiceClass, downloadId, false)
        }
        return Observable.just(true)
    }

    fun removeAllDownloads(): Observable<Boolean> {
        downloadManager.removeAllDownloads()
        return Observable.just(true)
    }

    fun pauseDownloads(): Observable<Boolean> {
        downloadManager.pauseDownloads()
        return Observable.just(true)
    }

    fun resumeDownloads(): Observable<Boolean> {
        downloadManager.resumeDownloads()
        return Observable.just(true)
    }

}