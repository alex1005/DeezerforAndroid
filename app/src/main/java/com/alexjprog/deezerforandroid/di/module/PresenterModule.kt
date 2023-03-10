package com.alexjprog.deezerforandroid.di.module

import com.alexjprog.deezerforandroid.presenter.LoginPresenter
import com.alexjprog.deezerforandroid.presenter.PlayerPresenter
import com.alexjprog.deezerforandroid.presenter.SearchPresenter
import com.alexjprog.deezerforandroid.presenter.UserInfoPresenter
import com.alexjprog.deezerforandroid.ui.mvp.contract.LoginContract
import com.alexjprog.deezerforandroid.ui.mvp.contract.PlayerContract
import com.alexjprog.deezerforandroid.ui.mvp.contract.SearchContract
import com.alexjprog.deezerforandroid.ui.mvp.contract.UserInfoContract
import dagger.Binds
import dagger.Module

@Module(includes = [PresenterModule.PresenterBinds::class])
class PresenterModule {
    @Module
    interface PresenterBinds {
        @Binds
        fun bindSearchPresenter(searchPresenter: SearchPresenter): SearchContract.Presenter

        @Binds
        fun bindPlayerPresenter(playerPresenter: PlayerPresenter): PlayerContract.Presenter

        @Binds
        fun bindLoginPresenter(loginPresenter: LoginPresenter): LoginContract.Presenter

        @Binds
        fun bindUserInfoPresenter(userInfoPresenter: UserInfoPresenter): UserInfoContract.Presenter
    }
}