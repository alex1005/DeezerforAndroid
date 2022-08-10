package com.alexjprog.deezerforandroid.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.alexjprog.deezerforandroid.R
import com.alexjprog.deezerforandroid.service.MediaPlayerService

class MediaPlayerNotificationHelper(
    private val mediaPlayerService: MediaPlayerService
) {
    private val notificationManager: NotificationManager =
        mediaPlayerService.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager

    init {
        notificationManager.cancelAll()
    }

    fun getNotification(): Notification = notificationBuilder().build()

    private fun notificationBuilder(): NotificationCompat.Builder {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createChannel()
        return NotificationCompat.Builder(mediaPlayerService, CHANNEL_ID)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Demo")
            .setContentText("DemoText")
            .setPriority(NotificationCompat.PRIORITY_MIN)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        if (notificationManager.getNotificationChannel(CHANNEL_ID) != null) return
        val name: CharSequence = "DeezerPlayer"
        val description = "Deezer's media player notification channel"
        val importance = NotificationManager.IMPORTANCE_MIN
        val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
        mChannel.description = description
        notificationManager.createNotificationChannel(mChannel)
    }

    companion object {
        const val NOTIFICATION_ID = 1001
        private const val CHANNEL_ID = "com.alexjprog.deezerforandroid.mediachannel"
    }
}