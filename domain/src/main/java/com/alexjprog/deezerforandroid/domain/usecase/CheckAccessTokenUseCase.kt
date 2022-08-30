package com.alexjprog.deezerforandroid.domain.usecase

import com.alexjprog.deezerforandroid.domain.repository.UserRepository
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class CheckAccessTokenUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(token: String): Observable<Boolean> =
        repository.getUserInfoWithToken(token).map { true }.onErrorReturn { false }
}
