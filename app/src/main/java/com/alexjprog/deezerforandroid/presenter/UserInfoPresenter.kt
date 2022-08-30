package com.alexjprog.deezerforandroid.presenter

import com.alexjprog.deezerforandroid.ui.mvp.contract.UserInfoContract
import javax.inject.Inject

class UserInfoPresenter @Inject constructor(
) : BasePresenter<UserInfoContract.View>(),
    UserInfoContract.Presenter {

}