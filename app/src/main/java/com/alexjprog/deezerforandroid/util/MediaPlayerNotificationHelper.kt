package com.alexjprog.deezerforandroid.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.os.Build
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.media.session.MediaButtonReceiver
import androidx.navigation.NavDeepLinkBuilder
import com.alexjprog.deezerforandroid.R
import com.alexjprog.deezerforandroid.service.MediaPlayerService
import com.alexjprog.deezerforandroid.ui.MainActivity


class MediaPlayerNotificationHelper(
    private val mediaPlayerService: MediaPlayerService
) {
    private val pauseAction: NotificationCompat.Action = NotificationCompat.Action(
        R.drawable.ic_baseline_pause_24,
        mediaPlayerService.getString(R.string.pause),
        MediaButtonReceiver.buildMediaButtonPendingIntent(
            mediaPlayerService,
            PlaybackStateCompat.ACTION_PAUSE
        )
    )
    private val playAction: NotificationCompat.Action = NotificationCompat.Action(
        R.drawable.ic_baseline_play_arrow_24,
        mediaPlayerService.getString(R.string.play),
        MediaButtonReceiver.buildMediaButtonPendingIntent(
            mediaPlayerService,
            PlaybackStateCompat.ACTION_PLAY
        )
    )
    private val notificationManager: NotificationManager =
        mediaPlayerService.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager

    private val playerDeepLink = NavDeepLinkBuilder(mediaPlayerService)
        .setComponentName(MainActivity::class.java)
        .setGraph(R.navigation.nav_graph)
        .setDestination(R.id.playerFragment)
        .createPendingIntent()

    init {
        notificationManager.cancelAll()
    }

    fun getNotification(
        metadata: MediaMetadataCompat?,
        state: PlaybackStateCompat,
        token: MediaSessionCompat.Token?
    ): Notification {

        val isPlaying = state.state == PlaybackStateCompat.STATE_PLAYING
        val description = metadata?.description
        return notificationBuilder(token, isPlaying, description).build()
    }

    private fun notificationBuilder(
        token: MediaSessionCompat.Token?,
        isPlaying: Boolean,
        description: MediaDescriptionCompat?
    ): NotificationCompat.Builder {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createChannel()
        return NotificationCompat.Builder(mediaPlayerService, CHANNEL_ID)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(token)
                    .setShowActionsInCompactView(0)
                    .setShowCancelButton(true)
                    .setCancelButtonIntent(
                        MediaButtonReceiver.buildMediaButtonPendingIntent(
                            mediaPlayerService,
                            PlaybackStateCompat.ACTION_STOP
                        )
                    )
            )
            .setSmallIcon(R.mipmap.ic_launcher)
            .setColor(ContextCompat.getColor(mediaPlayerService, R.color.color_primary))
            .setContentIntent(playerDeepLink)
            .setContentTitle(description?.title)
            .setContentText(description?.subtitle)
            .setDeleteIntent(
                MediaButtonReceiver.buildMediaButtonPendingIntent(
                    mediaPlayerService, PlaybackStateCompat.ACTION_PAUSE
                )
            )
            .addAction(if (isPlaying) pauseAction else playAction)
    }

    fun updateNotification(
        metadata: MediaMetadataCompat?,
        state: PlaybackStateCompat,
        token: MediaSessionCompat.Token?
    ) =
        notificationManager.notify(NOTIFICATION_ID, getNotification(metadata, state, token))

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        if (notificationManager.getNotificationChannel(CHANNEL_ID) != null) return
        val name: CharSequence = "DeezerPlayer"
        val description = "Deezer's media player notification channel"
        val importance = NotificationManager.IMPORTANCE_LOW
        val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
        mChannel.description = description
        notificationManager.createNotificationChannel(mChannel)
    }

    companion object {
        const val NOTIFICATION_ID = 1001
        private const val CHANNEL_ID = "com.alexjprog.deezerforandroid.mediachannel"
    }
}