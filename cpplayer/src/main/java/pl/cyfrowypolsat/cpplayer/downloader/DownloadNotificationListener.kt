package pl.cyfrowypolsat.cpplayer.downloader

import android.app.Notification
import android.content.Context
import androidx.media3.exoplayer.offline.Download
import androidx.media3.exoplayer.offline.DownloadManager
import androidx.media3.common.util.NotificationUtil


class DownloadNotificationListener(private val context: Context,
                                   private val notificationHelper: DownloadNotificationHelper,
                                   private val notificationId: Int) : DownloadManager.Listener {

    private var completedNotificationId = notificationId + 1
    private var failedNotificationId = notificationId + 2

    override fun onDownloadChanged(
            downloadManager: DownloadManager, download: Download, finalException: Exception?) {

        var nextNotificationId = completedNotificationId
        val notification: Notification = if (download.state == Download.STATE_COMPLETED && downloadManager.currentDownloads.isEmpty()) {
            nextNotificationId = completedNotificationId
            notificationHelper.buildDownloadCompletedNotification(context)
        } else if (download.state == Download.STATE_FAILED) {
            nextNotificationId = failedNotificationId++
            notificationHelper.buildDownloadFailedNotification(context, download)
        } else {
            return
        }
        NotificationUtil.setNotification(context, nextNotificationId, notification)
    }
}