package com.alexjprog.deezerforandroid.ui.mvp.contract

import android.net.Uri
import com.alexjprog.deezerforandroid.presenter.MVPPresenter
import com.alexjprog.deezerforandroid.ui.mvp.BaseView

interface LoginContract {
    interface View: BaseView {
        fun showLoginButton()
        fun onSuccessfulLogin()
    }
    interface Presenter: MVPPresenter<View> {
        fun extractAndSaveUserToken(authUri: Uri)
        fun checkLoginState()
    }
}