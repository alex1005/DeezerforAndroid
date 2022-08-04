package com.alexjprog.deezerforandroid.ui.mvp.contract

import android.content.ServiceConnection
import com.alexjprog.deezerforandroid.presenter.MVPPresenter
import com.alexjprog.deezerforandroid.ui.mvp.BaseView

interface PlayerContract {
    interface View : BaseView {
        fun setPlayButtonState(playing: Boolean?)
        fun setPreviousButtonAvailability(enabled: Boolean)
        fun setNextButtonAvailability(enabled: Boolean)
    }

    interface Presenter : MVPPresenter<View> {
        fun playOrPauseMedia()
        fun nextTrack()
        fun previousTrack()

        val playerConnection: ServiceConnection
    }
}