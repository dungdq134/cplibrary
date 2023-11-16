package pl.cyfrowypolsat.cpplayer.downloader

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.media3.exoplayer.offline.Download
import androidx.media3.common.util.Util
import com.google.gson.Gson
import pl.cyfrowypolsat.cpplayer.R
import pl.cyfrowypolsat.cpplayer.model.DownloadData

/** Modified com.google.android.exoplayer2.ui.DownloadNotificationHelper for our purpose */
/** Foreground services must display a Notification (this is why, 'Removing' notification is showing)
 * https://developer.android.com/guide/components/services#Types-of-services */

class DownloadNotificationHelper(private val context: Context,
                                 private val channelId: String?,
                                 @DrawableRes private val progressIcon: Int = R.drawable.cppl_ic_download_notification_progress,
                                 @DrawableRes private val completedIcon: Int = R.drawable.cppl_ic_download_notification_completed,
                                 @DrawableRes private val errorIcon: Int = R.drawable.cppl_ic_download_notification_error,
                                 @ColorRes private val accentColor: Int? = null,
                                 private val notificationIntentBuilder: NotificationIntentBuilder) {

    interface NotificationIntentBuilder {
        fun buildShowDownloadsPendingIntent(context: Context): PendingIntent

        fun buildCancelDownloadPendingIntent(context: Context, downloadId: String): PendingIntent

        fun buildRetryDownloadPendingIntent(context: Context, downloadId: String): PendingIntent
    }

    /**
     * Returns a progress notification for the given downloads.
     *
     * @param context A context.
     * @param downloads The downloads.
     * @return The notification.
     */
    fun buildProgressNotification(context: Context, downloads: List<Download>): Notification {

        val downloadInProgress = downloads.firstOrNull { d -> d.state == Download.STATE_DOWNLOADING }

        downloadInProgress?.let {
            val downloadData = Gson().fromJson(Util.fromUtf8Bytes(it.request.data), DownloadData::class.java)
            val downloadedMegabytes = it.bytesDownloaded / (1024 * 1024)
            val totalMegabytes = it.contentLength / (1024 * 1024)
            val action = NotificationCompat.Action(0, context.getString(R.string.download_notification_action_cancel), notificationIntentBuilder.buildCancelDownloadPendingIntent(context, downloadData.id))

            val title = context.getString(R.string.download_notification_downloading)
            val message = "${downloadData.title}\n${it.percentDownloaded.toInt()}% ($downloadedMegabytes MB/$totalMegabytes MB)"
            val progress = it.percentDownloaded.toInt()

            return buildNotification(
                    context = context,
                    smallIcon = progressIcon,
                    title = title,
                    accentColor = accentColor,
                    message = message,
                    contentIntent = notificationIntentBuilder.buildShowDownloadsPendingIntent(context),
                    actions = listOf(action),
                    maxProgress = 100,
                    currentProgress = progress,
                    ongoing = true,
                    showWhen = false)
        }

        val isDownloadRemovingInProgress = downloads.any { d -> d.state == Download.STATE_REMOVING }

        if (isDownloadRemovingInProgress) {
            return buildDownloadRemovingNotification(context)
        }

        return buildEmptyNotification(context)
    }

    /**
     * Returns a notification for a completed download.
     *
     * @param context A context.
     * @return The notification.
     */
    fun buildDownloadCompletedNotification(context: Context): Notification {
        return buildNotification(
                context = context,
                smallIcon = completedIcon,
                accentColor = accentColor,
                title = context.getString(R.string.download_notification_completed),
                message = null,
                contentIntent = notificationIntentBuilder.buildShowDownloadsPendingIntent(context))
    }

    /**
     * Returns a notification for a failed download.
     *
     * @param context A context.
     * @param download An optional message to display on the notification.
     * @return The notification.
     */
    fun buildDownloadFailedNotification(context: Context, download: Download): Notification {

        val downloadData = Gson().fromJson(Util.fromUtf8Bytes(download.request.data), DownloadData::class.java)
        val actionCancel = NotificationCompat.Action(0, context.getString(R.string.download_notification_action_cancel), notificationIntentBuilder.buildCancelDownloadPendingIntent(context, downloadData.id))
        val actionRetry = NotificationCompat.Action(0, context.getString(R.string.download_notification_action_retry), notificationIntentBuilder.buildRetryDownloadPendingIntent(context, downloadData.id))

        return buildNotification(
                context = context,
                smallIcon = errorIcon,
                accentColor = accentColor,
                title = context.getString(R.string.download_notification_failed),
                message = downloadData.title,
                contentIntent = notificationIntentBuilder.buildShowDownloadsPendingIntent(context),
                actions = listOf(actionCancel, actionRetry))
    }

    /**
     * Returns a notification for a removing download.
     *
     * @param context A context.
     * @return The notification.
     */
    fun buildDownloadRemovingNotification(context: Context): Notification {
        return buildNotification(
                context = context,
                smallIcon = progressIcon,
                accentColor = accentColor,
                title = context.getString(R.string.download_notification_removing),
                message = null,
                indeterminateProgress = true,
                contentIntent = notificationIntentBuilder.buildShowDownloadsPendingIntent(context))
    }

    /**
     * Returns a empty notification for cases when we cant determine state (imo this is impossible, but i have to return something in buildProgressNotification().
     *
     * @param context A context.
     * @return The notification.
     */
    fun buildEmptyNotification(context: Context): Notification {
        return buildNotification(
                context = context,
                smallIcon = progressIcon,
                accentColor = accentColor,
                title = null,
                message = null,
                indeterminateProgress = true,
                contentIntent = notificationIntentBuilder.buildShowDownloadsPendingIntent(context))
    }

    private fun buildNotification(
            context: Context,
            @DrawableRes smallIcon: Int,
            title: String?,
            @ColorRes accentColor: Int?,
            message: String?,
            contentIntent: PendingIntent,
            actions: List<NotificationCompat.Action>? = null,
            maxProgress: Int = 0,
            currentProgress: Int = 0,
            indeterminateProgress: Boolean = false,
            ongoing: Boolean = false,
            showWhen: Boolean = true): Notification {
        val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(context.applicationContext, channelId!!)
        notificationBuilder.setSmallIcon(smallIcon)
        title?.let { notificationBuilder.setContentTitle(title) }

        accentColor?.let {
            notificationBuilder.color = ContextCompat.getColor(context, it)
        }

        message?.let {
            notificationBuilder.setStyle(NotificationCompat.BigTextStyle().bigText(message))
        }

        notificationBuilder.setContentIntent(contentIntent)
        notificationBuilder.setProgress(maxProgress, currentProgress, indeterminateProgress)
        notificationBuilder.setOngoing(ongoing)
        notificationBuilder.setShowWhen(showWhen)
        actions?.let {
            actions.forEach {
                notificationBuilder.addAction(it)
            }
        }
        return notificationBuilder.build()
    }
}
