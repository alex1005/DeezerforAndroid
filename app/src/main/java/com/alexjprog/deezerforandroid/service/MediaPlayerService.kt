package com.alexjprog.deezerforandroid.service

import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import com.alexjprog.deezerforandroid.app.DeezerApplication
import com.alexjprog.deezerforandroid.domain.model.AlbumModel
import com.alexjprog.deezerforandroid.domain.model.MediaItemModel
import com.alexjprog.deezerforandroid.domain.model.TrackModel
import com.alexjprog.deezerforandroid.domain.usecase.GetAlbumInfoUseCase
import com.alexjprog.deezerforandroid.domain.usecase.GetTrackInfoUseCase
import com.alexjprog.deezerforandroid.model.MediaTypeParam
import com.alexjprog.deezerforandroid.util.MEDIA_ID_KEY
import com.alexjprog.deezerforandroid.util.MEDIA_TYPE_KEY
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject
import kotlin.properties.Delegates

class MediaPlayerService : Service() {

    private val binder = MediaPlayerBinder(this)
    private val mediaPlayerListeners: MutableList<MediaPlayerListener> = mutableListOf()

    @Inject
    lateinit var getTrackInfoUseCase: GetTrackInfoUseCase

    @Inject
    lateinit var getAlbumInfoUseCase: GetAlbumInfoUseCase

    val isPlaying: Boolean?
        get() = try {
            player.isPlaying
        } catch (e: UninitializedPropertyAccessException) {
            null
        }
    private val hasNextTrack: Boolean
        get() = currentTrackIndex < playlist.lastIndex
    private val hasPreviousTrack: Boolean
        get() = currentTrackIndex > 0

    private lateinit var player: MediaPlayer
    private val playlist: MutableList<TrackModel> = mutableListOf()
    private var currentTrackIndex = INITIAL_PLAYLIST_INDEX

    override fun onCreate() {
        super.onCreate()
        (applicationContext as DeezerApplication).appComponent.inject(this)
    }

    private var playlistSource: MediaItemModel? by Delegates.observable(null) { _, _, newValue ->
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

    fun playMedia() {
        try {
            player.start()
        } catch (e: UninitializedPropertyAccessException) {
        }
        pushOnPlayEvent()
    }

    fun pauseMedia() {
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
        pushOnPauseEvent()
    }

    private fun changeTrack(goBackwards: Boolean) {
        val nextIndex = if (goBackwards) --currentTrackIndex else ++currentTrackIndex
        val nextTrack = playlist.getOrNull(nextIndex)
        stopMedia()
        nextTrack?.let { track ->
            pushOnUpdateCurrentTrackEvent()
            player = MediaPlayer().also { newPlayer ->
                newPlayer.setAudioAttributes(
                    AudioAttributes.Builder()
                        .setLegacyStreamType(AudioManager.STREAM_MUSIC)
                        .build()
                )
                newPlayer.setDataSource(track.trackLink)
                newPlayer.prepareAsync()
                newPlayer.setOnPreparedListener { playMedia() }
                newPlayer.setOnCompletionListener { pushOnPauseEvent() }
            }
        }
    }

    fun previousTrack() = changeTrack(true)

    fun nextTrack() = changeTrack(false)

    private fun pushOnPlayEvent() {
        mediaPlayerListeners.forEach { listener ->
            listener.onPlay()
        }
    }

    private fun pushOnPauseEvent() {
        mediaPlayerListeners.forEach { listener ->
            listener.onPause()
        }
    }

    private fun pushOnUpdateCurrentTrackEvent() {
        mediaPlayerListeners.forEach { listener ->
            listener.updateCurrentTrack(
                hasPreviousTrack,
                hasNextTrack,
                playlist[currentTrackIndex]
            )
        }
    }

    fun addMediaPlayerListener(listener: MediaPlayerListener) {
        mediaPlayerListeners += listener
    }

    fun removeMediaPlayerListener(listener: MediaPlayerListener) {
        mediaPlayerListeners.remove(listener)
    }

    override fun onBind(intent: Intent): IBinder {
        val type = intent.getSerializableExtra(MEDIA_TYPE_KEY) as? MediaTypeParam
        val id = intent.getIntExtra(MEDIA_ID_KEY, -1)
        when (type) {
            MediaTypeParam.TRACK -> TrackModel(id = id)
            MediaTypeParam.ALBUM -> AlbumModel(id = id)
            else -> null
        }?.let { playlistSource = it }
        return binder
    }

    override fun onDestroy() {
        super.onDestroy()
        stopMedia()
        binder.releaseService()
    }

    class MediaPlayerBinder(private var mediaPlayerService: MediaPlayerService?) : Binder() {
        fun getMediaPlayerService() = mediaPlayerService
        fun releaseService() {
            mediaPlayerService = null
        }
    }

    interface MediaPlayerListener {
        fun onPlay()
        fun onPause()

        fun updateCurrentTrack(hasPrevious: Boolean, hasNext: Boolean, currentTrack: TrackModel)
    }

    companion object {
        const val INITIAL_PLAYLIST_INDEX = -1
    }
}