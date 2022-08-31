package com.alexjprog.deezerforandroid.ui.mvp.contract

import com.alexjprog.deezerforandroid.presenter.MVPPresenter
import com.alexjprog.deezerforandroid.ui.mvp.BaseView

interface UserInfoContract {
    interface View : BaseView {
        fun showUserInfo(
            firstname: String,
            lastname: String,
            birthday: String?,
            inscriptionDate: String,
            email: String,
            country: String,
            bigPictureLink: String,
            linkToDeezer: String
        )
    }

    interface Presenter : MVPPresenter<View> {
        fun loadUserInfo()
    }
}
