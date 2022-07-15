package com.alexjprog.deezerforandroid.domain.usecase

import com.alexjprog.deezerforandroid.domain.repository.UserRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(token: String) {
        repository.saveUserAccessToken(token)
    }
}