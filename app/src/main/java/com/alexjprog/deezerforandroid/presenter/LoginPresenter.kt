package com.alexjprog.deezerforandroid.presenter

import com.alexjprog.deezerforandroid.domain.usecase.CheckAccessTokenUseCase
import com.alexjprog.deezerforandroid.domain.usecase.GetAccessTokenUseCase
import com.alexjprog.deezerforandroid.domain.usecase.LoginUseCase
import com.alexjprog.deezerforandroid.ui.mvp.contract.LoginContract
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class LoginPresenter @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
    private val checkLoginTokenUseCase: CheckAccessTokenUseCase
): BasePresenter<LoginContract.View>(),
    LoginContract.Presenter {

    override fun checkAndSaveUserToken(token: String?) {
        if (token != null) {
            checkLoginTokenUseCase(token)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (it) {
                        view?.onSuccessfulLogin()
                        loginUseCase(token)
                    } else {
                        view?.showLoginButton()
                        //TODO: show error
                    }
                }
        } else {
            if (isLoggedIn()) view?.onSuccessfulLogin()
            else view?.showLoginButton()
        }
    }

    private fun isLoggedIn(): Boolean =
        getAccessTokenUseCase() != null
}