package com.alexjprog.deezerforandroid.util

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.graphics.Bitmap
import android.os.Build
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.media.session.MediaButtonReceiver
import androidx.navigation.NavDeepLinkBuilder
import com.alexjprog.deezerforandroid.R
import com.alexjprog.deezerforandroid.service.MediaPlayerService
import com.alexjprog.deezerforandroid.ui.MainActivity


class MediaPlayerNotificationHelper(
    private val mediaPlayerService: MediaPlayerService
) {
    private val pauseAction: NotificationCompat.Action = NotificationCompat.Action(
        R.drawable.pause,
        mediaPlayerService.getString(R.string.pause),
        MediaButtonReceiver.buildMediaButtonPendingIntent(
            mediaPlayerService,
            PlaybackStateCompat.ACTION_PAUSE
        )
    )
    private val playAction: NotificationCompat.Action = NotificationCompat.Action(
        R.drawable.play,
        mediaPlayerService.getString(R.string.play),
        MediaButtonReceiver.buildMediaButtonPendingIntent(
            mediaPlayerService,
            PlaybackStateCompat.ACTION_PLAY
        )
    )

    private val stopAction: NotificationCompat.Action = NotificationCompat.Action(
        R.drawable.cancel,
        mediaPlayerService.getString(R.string.stop),
        MediaButtonReceiver.buildMediaButtonPendingIntent(
            mediaPlayerService,
            PlaybackStateCompat.ACTION_STOP
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
        token: MediaSessionCompat.Token?,
        bitmap: Bitmap?
    ): Notification {

        val isPlaying = state.state == PlaybackStateCompat.STATE_PLAYING
        val hasNextTrack = state.extras?.getBoolean(HAS_NEXT_TRACK_KEY) ?: false
        val hasPreviousTrack = state.extras?.getBoolean(HAS_PREVIOUS_TRACK_KEY) ?: false
        val description = metadata?.description
        return notificationBuilder(
            token,
            isPlaying,
            hasNextTrack,
            hasPreviousTrack,
            description,
            bitmap
        ).build()
    }

    private fun notificationBuilder(
        token: MediaSessionCompat.Token?,
        isPlaying: Boolean,
        hasNextTrack: Boolean,
        hasPreviousTrack: Boolean,
        description: MediaDescriptionCompat?,
        bitmap: Bitmap?
    ): NotificationCompat.Builder {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createChannel()
        return NotificationCompat.Builder(mediaPlayerService, CHANNEL_ID)
            .setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(token)
                    .setShowActionsInCompactView(0, 1, 2, 4)
                    .setShowCancelButton(true)
                    .setCancelButtonIntent(
                        MediaButtonReceiver.buildMediaButtonPendingIntent(
                            mediaPlayerService,
                            PlaybackStateCompat.ACTION_STOP
                        )
                    )
            )
            .setSmallIcon(R.drawable.ic_small_notification)
            .setContentIntent(playerDeepLink)
            .setContentTitle(description?.title)
            .setContentText(description?.subtitle)
            .addAction(previousTrackAction(hasPreviousTrack))
            .addAction(if (isPlaying) pauseAction else playAction)
            .addAction(nextTrackAction(hasNextTrack))
            .addAction(stopAction)
            .apply {
                if (bitmap != null) setLargeIcon(bitmap)
            }
    }

    fun updateNotification(
        metadata: MediaMetadataCompat?,
        state: PlaybackStateCompat,
        token: MediaSessionCompat.Token?,
        bitmap: Bitmap?
    ) =
        notificationManager.notify(NOTIFICATION_ID, getNotification(metadata, state, token, bitmap))

    fun cancelNotification() =
        notificationManager.cancel(NOTIFICATION_ID)

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val name: CharSequence = "DeezerPlayer"
        val description = "Deezer media player notification channel"
        val importance = NotificationManager.IMPORTANCE_LOW
        val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
        mChannel.description = description
        notificationManager.createNotificationChannel(mChannel)
    }

    @SuppressLint("WrongConstant")
    private fun nextTrackAction(hasNextTrack: Boolean) = NotificationCompat.Action(
        R.drawable.skip_next,
        mediaPlayerService.getString(R.string.skip_next),
        MediaButtonReceiver.buildMediaButtonPendingIntent(
            mediaPlayerService,
            if (hasNextTrack) PlaybackStateCompat.ACTION_SKIP_TO_NEXT else 0
        )
    )

    @SuppressLint("WrongConstant")
    private fun previousTrackAction(hasPreviousTrack: Boolean) = NotificationCompat.Action(
        R.drawable.skip_previous,
        mediaPlayerService.getString(R.string.skip_previous),
        MediaButtonReceiver.buildMediaButtonPendingIntent(
            mediaPlayerService,
            if (hasPreviousTrack) PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS else 0
        )
    )

    companion object {
        const val NOTIFICATION_ID = 1001
        private const val CHANNEL_ID = "com.alexjprog.deezerforandroid.mediachannel"

        const val HAS_NEXT_TRACK_KEY = "has_next_track"
        const val HAS_PREVIOUS_TRACK_KEY = "has_prev_track"
    }
}