package com.alexjprog.deezerforandroid.service

import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Binder
import android.os.Handler
import android.os.IBinder
import android.os.Looper
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

    @Inject
    lateinit var getTrackInfoUseCase: GetTrackInfoUseCase

    @Inject
    lateinit var getAlbumInfoUseCase: GetAlbumInfoUseCase

    private val isPlaying: Boolean?
        get() = try {
            player.isPlaying
        } catch (e: UninitializedPropertyAccessException) {
            null
        } catch (e: IllegalStateException) {
            false
        }
    private val hasNextTrack: Boolean
        get() = currentTrackIndex < playlist.lastIndex
    private val hasPreviousTrack: Boolean
        get() = currentTrackIndex > 0

    private lateinit var player: MediaPlayer
    private lateinit var notificationHelper: MediaPlayerNotificationHelper
    private val playlist: MutableList<TrackModel> = mutableListOf()
    private var currentTrackIndex = INITIAL_PLAYLIST_INDEX

    override fun onCreate() {
        super.onCreate()
        (applicationContext as DeezerApplication).appComponent.inject(this)
        notificationHelper = MediaPlayerNotificationHelper(this)

    }

    var playlistSource: MediaItemModel? by Delegates.observable(null) { _, oldValue, newValue ->
        if (newValue?.id == oldValue?.id) {
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
        startForeground(
            MediaPlayerNotificationHelper.NOTIFICATION_ID,
            notificationHelper.getNotification()
        )
    }

    private fun playMedia() {
        try {
            player.start()
        } catch (e: UninitializedPropertyAccessException) {
        }
        startProgressUpdater()
        pushOnPlayEvent()
    }

    private fun pauseMedia() {
        try {
            player.pause()
        } catch (e: UninitializedPropertyAccessException) {
        }
        pushOnPauseEvent()
    }

    private fun stopMedia() {
        try {
            player.stop()
            player.release()
        } catch (e: UninitializedPropertyAccessException) {
        }
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
                    pushOnUpdateCurrentTrackEvent()
                    playMedia()
                }
                newPlayer.setOnCompletionListener { pushOnPauseEvent() }

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
        pushEventForAll { listener -> updater.post { listener.onProgressChanged(player.currentPosition) } }
    }

    private fun startProgressUpdater() {
        val updateRunnable = object : Runnable {
            override fun run() {
                pushProgressUpdateEvent()
                if (isPlaying == true) updater.postDelayed(this, PROGRESS_UPDATE_DELAY)
            }
        }
        updater.postDelayed(updateRunnable, PROGRESS_UPDATE_DELAY)
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
        fun updateCurrentTrack(hasPrevious: Boolean, hasNext: Boolean, currentTrack: TrackModel)
    }

    companion object {
        private const val INITIAL_PLAYLIST_INDEX = -1
        private const val PROGRESS_UPDATE_DELAY = 100L
    }
}