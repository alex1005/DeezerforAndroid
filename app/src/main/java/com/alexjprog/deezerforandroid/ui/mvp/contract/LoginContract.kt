package com.alexjprog.deezerforandroid.ui.mvp.contract

import com.alexjprog.deezerforandroid.presenter.MVPPresenter
import com.alexjprog.deezerforandroid.ui.mvp.BaseView

interface LoginContract {
    interface View: BaseView {
        fun showLoginButton()
        fun onSuccessfulLogin()
    }
    interface Presenter: MVPPresenter<View> {
        fun checkAndSaveUserToken(token: String?)
    }
}