package com.alexjprog.deezerforandroid.domain.usecase

import com.alexjprog.deezerforandroid.domain.repository.UserRepository
import javax.inject.Inject

class GetAccessTokenUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(): String? = repository.getUserAccessToken()
}
