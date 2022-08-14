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
import com.alexjprog.deezerforandroid.util.MediaPlayerNotificationHelper
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
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

    private val metadata: MediaMetadataCompat
        get() = MediaMetadataCompat.Builder()
            .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE, currentTrack?.title)
            .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE, currentTrack?.subtitle)
            .build()

    private val state: PlaybackStateCompat
        get() {
            val actions =
                if (player.isPlaying) PlaybackStateCompat.ACTION_PAUSE else PlaybackStateCompat.ACTION_PLAY
            val state =
                if (player.isPlaying) PlaybackStateCompat.STATE_PLAYING else PlaybackStateCompat.STATE_PAUSED

            return PlaybackStateCompat.Builder()
                .setActions(actions)
                .setState(
                    state,
                    player.currentPosition.toLong(),
                    1.0f,
                    SystemClock.elapsedRealtime()
                ).build()
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

        mediaSession = MediaSessionCompat(this, MEDIA_PLAYER_SESSION_TOKEN).apply {
            setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS
                        or MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
            )
            setCallback(object : MediaSessionCompat.Callback() {
                override fun onPlay() {
                    playMedia()
                }

                override fun onPause() {
                    pauseMedia()
                }
            })
            isActive = true
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == "android.intent.action.MEDIA_BUTTON") {
            val keyEvent: KeyEvent? =
                intent.extras?.get("android.intent.extra.KEY_EVENT") as KeyEvent?
            if (keyEvent?.keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE) {
                pauseMedia()
            } else {
                playMedia()
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    var playlistSource: MediaItemModel? by Delegates.observable(null) { _, oldValue, newValue ->
        if (newValue?.id == oldValue?.id || newValue == null) {
            pushState()
            return@observable
        }
        when (newValue) {
            is TrackModel -> getTrackInfoUseCase(newValue.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { track ->
                    playlist.clear()
                    playlist += track
                    currentTrackIndex = INITIAL_PLAYLIST_INDEX
                    nextTrack()
                }
            is AlbumModel -> getAlbumInfoUseCase(newValue.id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { album ->
                    album.trackList?.let { tracks ->
                        playlist.clear()
                        playlist += tracks.map { it }
                    }
                    currentTrackIndex = INITIAL_PLAYLIST_INDEX
                    nextTrack()
                }
            null -> {}
        }
    }

    private fun playMedia() {
        try {
            player.start()
        } catch (e: UninitializedPropertyAccessException) {
        }
        startProgressUpdater()
        pushOnPlayEvent()
        notificationHelper.updateNotification(metadata, state, mediaSession.sessionToken)
    }

    private fun pauseMedia() {
        try {
            player.pause()
        } catch (e: UninitializedPropertyAccessException) {
        }
        stopProgressUpdater()
        pushOnPauseEvent()
        notificationHelper.updateNotification(metadata, state, mediaSession.sessionToken)
    }

    private fun stopMedia() {
        try {
            player.stop()
            player.release()
        } catch (e: UninitializedPropertyAccessException) {
        }
        stopProgressUpdater()
        stopForeground(false)
    }

    fun startSeek() {
        stopProgressUpdater()
    }

    fun endSeek(progress: Int) {
        player.seekTo(progress)
    }

    private fun changeTrack(goBackwards: Boolean) {
        val nextIndex = if (goBackwards) --currentTrackIndex else ++currentTrackIndex
        val nextTrack = playlist.getOrNull(nextIndex)
        stopMedia()
        nextTrack?.let { track ->
            player = MediaPlayer().also { newPlayer ->
                newPlayer.setAudioAttributes(
                    AudioAttributes.Builder()
                        .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                        .build()
                )
                newPlayer.setDataSource(track.trackLink)
                newPlayer.prepareAsync()
                newPlayer.setOnPreparedListener {
                    mediaSession.setMetadata(metadata)
                    startForeground(
                        MediaPlayerNotificationHelper.NOTIFICATION_ID,
                        notificationHelper.getNotification(
                            metadata,
                            state,
                            mediaSession.sessionToken
                        )
                    )
                    pushOnUpdateCurrentTrackEvent()
                    playMedia()
                }
                newPlayer.setOnCompletionListener {
                    pauseMedia()
                    pushProgressUpdateEvent()
                }
                newPlayer.setOnSeekCompleteListener { startProgressUpdater() }
            }
        }
    }

    fun previousTrack() = changeTrack(true)

    fun nextTrack() = changeTrack(false)

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

    private fun pushOnUpdateCurrentTrackEvent() {
        pushEventForAll { listener ->
            val currentTrack = playlist[currentTrackIndex].copy(duration = player.duration)
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
                    if (player.duration - player.currentPosition < PROGRESS_TAIL)
                        player.duration
                    else player.currentPosition
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
        stopForeground(true)
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
            true -> pauseMedia()
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
    }

    companion object {
        private const val INITIAL_PLAYLIST_INDEX = -1
        private const val PROGRESS_UPDATE_DELAY = 100L
        private const val PROGRESS_TAIL = 300L

        private const val MEDIA_PLAYER_SESSION_TOKEN = "media_session_token"
    }
}