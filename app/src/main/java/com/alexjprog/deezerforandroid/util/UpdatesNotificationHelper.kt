package com.alexjprog.deezerforandroid.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.alexjprog.deezerforandroid.R
import com.alexjprog.deezerforandroid.domain.model.AlbumModel
import com.alexjprog.deezerforandroid.domain.model.MediaItemModel
import com.alexjprog.deezerforandroid.ui.MainActivity

class UpdatesNotificationHelper(private val context: Context) {

    private val notificationManager =
        context.getSystemService(NotificationManager::class.java) as NotificationManager

    private val openEditorialScreen = NavDeepLinkBuilder(context)
        .setComponentName(MainActivity::class.java)
        .setGraph(R.navigation.nav_graph)
        .setDestination(R.id.editorialFragment)
        .createPendingIntent()

    fun sendEditorialWeeklySelectionUpdateNotification(newMedia: List<AlbumModel>) {
        sendCommonEditorialUpdateNotification(
            R.string.weekly_selection_update_title,
            R.string.and_others,
            R.string.weekly_selection_update_content,
            newMedia,
            MAX_CONTENT_NAMES_IN_EDIT_SELECTION_UPDATE,
            WEEKLY_SELECTION_NOTIFICATION_ID
        )
    }

    fun sendEditorialReleasesUpdateNotification(newMedia: List<AlbumModel>) {
        sendCommonEditorialUpdateNotification(
            R.string.releases_update_title,
            R.string.and_more,
            R.string.releases_update_content,
            newMedia,
            MAX_CONTENT_NAMES_IN_EDIT_RELEASES_UPDATE,
            RELEASES_NOTIFICATION_ID
        )
    }

    private fun sendCommonEditorialUpdateNotification(
        @StringRes titleRes: Int,
        @StringRes longEndingRes: Int,
        @StringRes updateContentRes: Int,
        media: List<MediaItemModel>,
        maxContent: Int,
        notificationId: Int
    ) {
        val title = context.getString(titleRes)
        val longEnding = " " + context.getString(longEndingRes)
        val contentMedia = media.take(maxContent)
            .joinToString(
                postfix =
                if (media.size > maxContent) longEnding else " "
            ) { "${it.title} - ${it.subtitle}" }
        val content = context.getString(updateContentRes, contentMedia)
        notificationManager.notify(
            notificationId,
            notificationBuilder(title, content, openEditorialScreen).build()
        )
    }

    private fun notificationBuilder(
        title: String,
        content: String,
        contentIntent: PendingIntent
    ): NotificationCompat.Builder {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createChannel()
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_small_notification)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(contentIntent)
            .setStyle(NotificationCompat.BigTextStyle().bigText(content))
            .setContentTitle(title)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val name: CharSequence = "DeezerUpdates"
        val description = "Deezer media updates notifications channel"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
        mChannel.description = description
        notificationManager.createNotificationChannel(mChannel)
    }

    companion object {
        private const val WEEKLY_SELECTION_NOTIFICATION_ID = 2001
        private const val RELEASES_NOTIFICATION_ID = 2002
        private const val CHANNEL_ID = "com.alexjprog.deezerforandroid.updateschannel"
    }
}
