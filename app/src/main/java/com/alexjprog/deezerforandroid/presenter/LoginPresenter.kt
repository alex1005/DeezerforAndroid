package com.alexjprog.deezerforandroid.presenter

import android.net.Uri
import com.alexjprog.deezerforandroid.domain.usecase.GetAccessTokenUseCase
import com.alexjprog.deezerforandroid.domain.usecase.LoginUseCase
import com.alexjprog.deezerforandroid.ui.mvp.contract.LoginContract
import javax.inject.Inject

class LoginPresenter @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val getAccessTokenUseCase: GetAccessTokenUseCase
): BasePresenter<LoginContract.View>(),
    LoginContract.Presenter {
    override fun extractAndSaveUserToken(authUri: Uri) {
        val token = authUri.fragment
            ?.split("&")?.associate {
                it.split("=").zipWithNext().firstOrNull() ?: ("" to "")
            }?.get(TOKEN_PARAM_KEY)
        if(!token.isNullOrEmpty()) {
            loginUseCase(token)
            checkLoginState()
        }
    }

    override fun checkLoginState() {
        if(getAccessTokenUseCase() == null)
            view?.showLoginButton()
        else
            view?.onSuccessfulLogin()
    }

    companion object {
        const val TOKEN_PARAM_KEY = "access_token"
    }
}