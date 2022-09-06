package com.alexjprog.deezerforandroid.ui.mvp.contract

import com.alexjprog.deezerforandroid.model.UserInfoDisplayable
import com.alexjprog.deezerforandroid.presenter.MVPPresenter
import com.alexjprog.deezerforandroid.ui.mvp.BaseView

interface UserInfoContract {
    interface View : BaseView {
        fun showUserInfo(userInfo: UserInfoDisplayable)
        fun showErrorAndClose()
    }

    interface Presenter : MVPPresenter<View> {
        fun loadUserInfo()
    }
}
