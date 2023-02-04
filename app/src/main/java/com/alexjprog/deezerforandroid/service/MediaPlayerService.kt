package com.alexjprog.deezerforandroid.service

import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.*
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.view.KeyEvent
import com.alexjprog.deezerforandroid.app.DeezerApplication
import com.alexjprog.deezerforandroid.domain.model.AlbumModel
import com.alexjprog.deezerforandroid.domain.model.MediaItemModel
import com.alexjprog.deezerforandroid.domain.model.TrackModel
import com.alexjprog.deezerforandroid.domain.usecase.GetAlbumInfoUseCase
import com.alexjprog.deezerforandroid.domain.usecase.GetTrackInfoUseCase
import com.alexjprog.deezerforandroid.util.ImageHelper
import com.alexjprog.deezerforandroid.util.MediaPlayerNotificationHelper
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.io.FileNotFoundException
import javax.inject.Inject
import kotlin.properties.Delegates


class MediaPlayerService : Service() {

    private val binder = MediaPlayerBinder(this)
    private val mediaPlayerListeners: MutableList<MediaPlayerListener> = mutableListOf()
    private val updater = Handler(Looper.getMainLooper())
    private val updateProgressRunnable = object : Runnable {
        override fun run() {
            pushProgressUpdateEvent()
            updater.postDelayed(this, PROGRESS_UPDATE_DELAY)
        }
    }
    @Inject
    lateinit var getTrackInfoUseCase: GetTrackInfoUseCase

    @Inject
    lateinit var getAlbumInfoUseCase: GetAlbumInfoUseCase

    val isPlaying: Boolean?
        get() = try {
            player.isPlaying
        } catch (e: UninitializedPropertyAccessException) {
            null
        } catch (e: IllegalStateException) {
            false
        }

    private var isStopped: Boolean = true
    private val duration: Int
        get() = try {
            player.duration
        } catch (e: Exception) {
            0
        }
    private val currentPosition: Int
        get() = try {
            player.currentPosition
        } catch (e: Exception) {
            0
        }

    private val metadata: MediaMetadataCompat
        get() = MediaMetadataCompat.Builder()
            .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, currentTrack?.title)
            .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, currentTrack?.subtitle)
            .build()

    private val state: PlaybackStateCompat
        get() {
            val actions =
                if (isPlaying == true) PlaybackStateCompat.ACTION_PLAY else PlaybackStateCompat.ACTION_PAUSE
            val state =
                if (isPlaying == true) PlaybackStateCompat.STATE_PLAYING else PlaybackStateCompat.STATE_PAUSED
            val extras = Bundle()
            extras.putBoolean(MediaPlayerNotificationHelper.HAS_NEXT_TRACK_KEY, hasNextTrack)
            extras.putBoolean(
                MediaPlayerNotificationHelper.HAS_PREVIOUS_TRACK_KEY,
                hasPreviousTrack
            )

            return PlaybackStateCompat.Builder()
                .setActions(actions)
                .setState(
                    state,
                    currentPosition.toLong(),
                    1.0f,
                    SystemClock.elapsedRealtime()
                ).setExtras(extras)
                .build()
        }
    private val hasNextTrack: Boolean
        get() = currentTrackIndex < playlist.lastIndex
    private val hasPreviousTrack: Boolean
        get() = currentTrackIndex > 0
    private val currentTrack: TrackModel?
        get() = playlist.getOrNull(currentTrackIndex)

    private lateinit var player: MediaPlayer
    private lateinit var notificationHelper: MediaPlayerNotificationHelper
    private lateinit var mediaSession: MediaSessionCompat
    private val playlist: MutableList<TrackModel> = mutableListOf()
    private var currentTrackIndex = INITIAL_PLAYLIST_INDEX

    override fun onCreate() {
        super.onCreate()
        (applicationContext as DeezerApplication).appComponent.inject(this)
        notificationHelper = MediaPlayerNotificationHelper(this)

        mediaSession = MediaSessionCompat(this, MEDIA_PLAYER_SESSION_TOKEN)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == "android.intent.action.MEDIA_BUTTON") {
            val keyEvent: KeyEvent? =
                if (Build.VERSION.SDK_INT >= 33) {
                    intent.extras?.getParcelable(
                        "android.intent.extra.KEY_EVENT",
                        KeyEvent::class.java
                    )
                } else {
                    intent.extras?.getParcelable("android.intent.extra.KEY_EVENT")
                }
            when (keyEvent?.keyCode) {
                KeyEvent.KEYCODE_MEDIA_PAUSE -> pauseMedia(false)
                KeyEvent.KEYCODE_MEDIA_PLAY -> playMedia()
                KeyEvent.KEYCODE_MEDIA_NEXT -> nextTrack()
                KeyEvent.KEYCODE_MEDIA_PREVIOUS -> previousTrack()
                KeyEvent.KEYCODE_MEDIA_STOP -> {
                    pauseMedia(true)
                    notificationHelper.cancelNotification()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    var playlistSource: MediaItemModel? by Delegates.vetoable(null) { _, oldValue, newValue ->
        if (!isStopped && ((newValue == oldValue) || newValue == null)) {
            pushState()
            return@vetoable false
        }
        stopMedia()
        when (newValue) {
            is TrackModel -> getTrackInfoUseCase(newValue.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSuccess@{ track ->
                    playlist.clear()
                    playlist += track
                    currentTrackIndex = INITIAL_PLAYLIST_INDEX
                    nextTrack()
                }, onError@{})
            is AlbumModel -> getAlbumInfoUseCase(newValue.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(onSuccess@{ album ->
                    album.trackList?.let { tracks ->
                        playlist.clear()
                        playlist += tracks.map { it }
                    }
                    currentTrackIndex = INITIAL_PLAYLIST_INDEX
                    nextTrack()
                }, onError@{})
            null -> {}
        }
        return@vetoable true
    }

    private fun playMedia() {
        startForegroundNotification()
        if (isStopped) {
            prepareMediaSession()
            loadTrack(currentTrack)
            isStopped = false
            return
        }
        try {
            player.start()
        } catch (e: UninitializedPropertyAccessException) {
        }
        mediaSession.isActive = true
        startProgressUpdater()
        pushOnPlayEvent()
        updateForegroundNotification()
    }

    private fun pauseMedia(stopForeground: Boolean) {
        try {
            player.pause()
        } catch (e: UninitializedPropertyAccessException) {
        }
        mediaSession.isActive = false
        stopProgressUpdater()
        pushOnPauseEvent()
        if (stopForeground) {
            stopForeground(true)
            notificationHelper.cancelNotification()
        } else updateForegroundNotification()
    }

    private fun stopMedia() {
        try {
            player.stop()
            player.release()
        } catch (e: UninitializedPropertyAccessException) {
        } catch (e: IllegalStateException) {
            return
        }
        isStopped = true
        mediaSession.isActive = false
        pushOnStopEvent()
        stopProgressUpdater()
        stopForeground(true)
        notificationHelper.cancelNotification()
    }

    fun startSeek() {
        stopProgressUpdater()
    }

    fun endSeek(progress: Int) {
        player.seekTo(progress)
    }

    private fun prepareMediaSession() {
        mediaSession.setMetadata(metadata)
        mediaSession.setPlaybackState(state)
    }

    private fun startForegroundNotification() {
        startForeground(
            MediaPlayerNotificationHelper.NOTIFICATION_ID,
            notificationHelper.getNotification(
                metadata,
                state,
                mediaSession.sessionToken,
                null
            )
        )
    }

    private fun updateForegroundNotification() {
        val iconUri = currentTrack?.pictureLink
        ImageHelper.loadLargeIconForNotification(this, iconUri) { bitmap ->
            notificationHelper.updateNotification(
                metadata,
                state,
                mediaSession.sessionToken,
                bitmap
            )
        }
    }

    private fun loadTrack(nextTrack: TrackModel?) {
        stopMedia()
        nextTrack?.let { track ->
            player = MediaPlayer().also { newPlayer ->
                newPlayer.setAudioAttributes(
                    AudioAttributes.Builder()
                        .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                        .build()
                )
                try {
                    newPlayer.setDataSource(track.trackLink)
                } catch (e: FileNotFoundException) {
                    return
                }
                newPlayer.prepareAsync()
                newPlayer.setOnPreparedListener {
                    pushOnUpdateCurrentTrackEvent()
                    playMedia()
                }
                newPlayer.setOnCompletionListener {
                    pauseMedia(false)
                    pushProgressUpdateEvent()
                }
                newPlayer.setOnSeekCompleteListener { startProgressUpdater() }
            }
        }
    }

    fun previousTrack() = loadTrack(playlist.getOrNull(--currentTrackIndex))

    fun nextTrack() = loadTrack(playlist.getOrNull(++currentTrackIndex))

    private fun pushEventForAll(eventAction: (MediaPlayerListener) -> Unit) =
        mediaPlayerListeners.forEach(eventAction)

    private fun pushState() {
        pushOnUpdateCurrentTrackEvent()
        pushProgressUpdateEvent()
        when (isPlaying) {
            true -> pushOnPlayEvent()
            false -> pushOnPauseEvent()
            null -> {}
        }
    }

    private fun pushOnPlayEvent() {
        pushEventForAll { listener -> updater.post { listener.onPlayMedia() } }
    }

    private fun pushOnPauseEvent() {
        pushEventForAll { listener -> updater.post { listener.onPauseMedia() } }
    }

    private fun pushOnStopEvent() {
        pushEventForAll { listener -> updater.post { listener.onStopMedia() } }
    }

    private fun pushOnUpdateCurrentTrackEvent() {
        pushEventForAll { listener ->
            val currentTrack = playlist[currentTrackIndex].copy(duration = duration)
            updater.post {
                listener.updateCurrentTrack(
                    hasPreviousTrack,
                    hasNextTrack,
                    currentTrack
                )
            }
        }
    }

    private fun pushProgressUpdateEvent() {
        pushEventForAll { listener ->
            updater.post {
                listener.onProgressChanged(
                    if (duration - currentPosition < PROGRESS_TAIL)
                        duration
                    else currentPosition
                )
            }
        }
    }

    private fun startProgressUpdater() {
        updater.postDelayed(updateProgressRunnable, PROGRESS_UPDATE_DELAY)
    }

    private fun stopProgressUpdater() {
        updater.removeCallbacks(updateProgressRunnable)
    }

    fun addMediaPlayerListener(listener: MediaPlayerListener) {
        mediaPlayerListeners += listener
    }

    fun removeMediaPlayerListener(listener: MediaPlayerListener) {
        mediaPlayerListeners.remove(listener)
    }

    override fun onBind(intent: Intent): IBinder = binder

    override fun onTaskRemoved(rootIntent: Intent?) {
        super.onTaskRemoved(rootIntent)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopMedia()
        mediaPlayerListeners.clear()
        binder.releaseService()
    }

    fun playStopTrack() {
        when (isPlaying) {
            true -> pauseMedia(false)
            false -> playMedia()
            else -> {}
        }
    }

    class MediaPlayerBinder(private var mediaPlayerService: MediaPlayerService?) : Binder() {
        fun getMediaPlayerService() = mediaPlayerService
        fun releaseService() {
            mediaPlayerService = null
        }
    }

    interface MediaPlayerListener {
        fun onPlayMedia()
        fun onPauseMedia()
        fun onProgressChanged(progress: Int)
        fun updateCurrentTrack(hasPrevious: Boolean, hasNext: Boolean, currentTrack: TrackModel?)
        fun onStopMedia()
    }

    companion object {
        private const val INITIAL_PLAYLIST_INDEX = -1
        private const val PROGRESS_UPDATE_DELAY = 100L
        private const val PROGRESS_TAIL = 300L

        private const val MEDIA_PLAYER_SESSION_TOKEN = "media_session_token"
    }
}