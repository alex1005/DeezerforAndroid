package com.alexjprog.deezerforandroid.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.alexjprog.deezerforandroid.R
import com.alexjprog.deezerforandroid.domain.model.AlbumModel
import com.alexjprog.deezerforandroid.ui.MainActivity

class UpdatesNotificationHelper(private val context: Context) {

    private val notificationManager =
        context.getSystemService(NotificationManager::class.java) as NotificationManager

    private val openEditorialScreen = NavDeepLinkBuilder(context)
        .setComponentName(MainActivity::class.java)
        .setGraph(R.navigation.nav_graph)
        .setDestination(R.id.editorialFragment)
        .createPendingIntent()

    fun pushEditorialWeeklySelectionUpdateNotification(newMedia: List<AlbumModel>) {
        val title = context.getString(R.string.weekly_selection_update_title)
        val longEnding = " " + context.getString(R.string.others)
        val contentMedia = newMedia.take(MAX_CONTENT_NAMES_IN_EDIT_SELECTION_UPDATE)
            .joinToString(
                postfix =
                if (newMedia.size > MAX_CONTENT_NAMES_IN_EDIT_SELECTION_UPDATE) longEnding else " "
            ) { "${it.title} - ${it.subtitle}" }
        val content = context.getString(R.string.weekly_selection_update_content, contentMedia)
        notificationManager.notify(
            WEEKLY_SELECTION_NOTIFICATION_ID,
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
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(contentIntent)
            .setStyle(NotificationCompat.BigTextStyle().bigText(content))
            .setContentTitle(title)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val name: CharSequence = "DeezerUpdates"
        val description = "Deezer's update notifications channel"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
        mChannel.description = description
        notificationManager.createNotificationChannel(mChannel)
    }

    companion object {
        private const val WEEKLY_SELECTION_NOTIFICATION_ID = 2001
        private const val CHANNEL_ID = "com.alexjprog.deezerforandroid.updateschannel"
    }
}
