package com.alexjprog.deezerforandroid.ui.mvp.contract

import android.content.ServiceConnection
import com.alexjprog.deezerforandroid.presenter.MVPPresenter
import com.alexjprog.deezerforandroid.ui.mvp.BaseView

interface PlayerContract {
    interface View : BaseView {
        var isPlaying: Boolean
    }

    interface Presenter : MVPPresenter<View> {
        fun playMedia()
        fun pauseMedia()

        val playerConnection: ServiceConnection
    }
}