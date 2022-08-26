package com.alexjprog.deezerforandroid.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.alexjprog.deezerforandroid.R
import com.alexjprog.deezerforandroid.domain.model.params.MediaTypeParam
import com.alexjprog.deezerforandroid.ui.MainActivity

class FirebasePushNotificationHelper(private val context: Context) {

    private val notificationManager =
        context.getSystemService(NotificationManager::class.java) as NotificationManager

    private val emptyPendingIntent =
        PendingIntent.getActivity(context, 0, Intent(), PendingIntent.FLAG_IMMUTABLE)

    private fun getOpenPlayerPendingIntent(mediaId: Int, mediaType: MediaTypeParam) =
        NavDeepLinkBuilder(context)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.nav_graph)
            .setArguments(Bundle().putPlayerArgs(mediaId, mediaType))
            .setDestination(R.id.playerFragment)
            .createPendingIntent()

    fun sendFirebaseNotification(
        title: String,
        textContent: String,
        mediaId: Int?,
        mediaType: MediaTypeParam
    ) {
        notificationManager.notify(
            FIREBASE_PUSH_NOTIFICATION_ID,
            notificationBuilder(
                title,
                textContent,
                mediaId?.let { getOpenPlayerPendingIntent(it, mediaType) }).build()
        )
    }

    private fun notificationBuilder(
        title: String,
        content: String,
        contentIntent: PendingIntent?
    ): NotificationCompat.Builder {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createChannel()
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(contentIntent ?: emptyPendingIntent)
            .setStyle(NotificationCompat.BigTextStyle().bigText(content))
            .setContentTitle(title)
            .setAutoCancel(true)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val name: CharSequence = "DeezerPush"
        val description = "Deezer push notifications channel"
        val importance = NotificationManager.IMPORTANCE_HIGH
        val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
        mChannel.description = description
        notificationManager.createNotificationChannel(mChannel)
    }

    companion object {
        private const val FIREBASE_PUSH_NOTIFICATION_ID = 3001
        private const val CHANNEL_ID = "com.alexjprog.deezerforandroid.firebasepushchannel"
    }
}
