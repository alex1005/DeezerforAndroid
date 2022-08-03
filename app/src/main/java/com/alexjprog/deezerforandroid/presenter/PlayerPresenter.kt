package com.alexjprog.deezerforandroid.presenter

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.IBinder
import com.alexjprog.deezerforandroid.service.MediaPlayerService
import com.alexjprog.deezerforandroid.ui.mvp.contract.PlayerContract
import javax.inject.Inject

class PlayerPresenter @Inject constructor(

) : BasePresenter<PlayerContract.View>(), PlayerContract.Presenter,
    MediaPlayerService.MediaPlayerListener {

    private var mediaPlayerService: MediaPlayerService? = null

    override val playerConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            mediaPlayerService = (service as MediaPlayerService.MediaPlayerBinder)
                .getMediaPlayerService()
            mediaPlayerService?.addMediaPlayerListener(this@PlayerPresenter)
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            mediaPlayerService?.removeMediaPlayerListener(this@PlayerPresenter)
            mediaPlayerService = null
        }
    }

    override fun playMedia() {
        mediaPlayerService?.playMedia()
    }

    override fun pauseMedia() {
        mediaPlayerService?.pauseMedia()
    }

    override fun onPlay() {
        view?.isPlaying = true
    }

    override fun onPause() {
        view?.isPlaying = false
    }
}
