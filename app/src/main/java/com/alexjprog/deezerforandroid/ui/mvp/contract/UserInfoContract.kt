package com.alexjprog.deezerforandroid.ui.mvp.contract

import com.alexjprog.deezerforandroid.presenter.MVPPresenter
import com.alexjprog.deezerforandroid.ui.mvp.BaseView

interface UserInfoContract {
    interface View : BaseView {

    }

    interface Presenter : MVPPresenter<View> {

    }
}
