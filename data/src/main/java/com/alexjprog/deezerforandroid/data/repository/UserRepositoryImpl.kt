package com.alexjprog.deezerforandroid.data.repository

import com.alexjprog.deezerforandroid.data.sharedprefs.LoginStore
import com.alexjprog.deezerforandroid.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val loginStore: LoginStore
): UserRepository {
    override fun getUserAccessToken(): String? = loginStore.userToken

    override fun saveUserAccessToken(token: String) {
        loginStore.userToken = token
    }

}