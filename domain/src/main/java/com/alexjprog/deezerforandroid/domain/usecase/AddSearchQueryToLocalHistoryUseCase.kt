package com.alexjprog.deezerforandroid.domain.usecase

import com.alexjprog.deezerforandroid.domain.repository.UserRepository
import javax.inject.Inject

class AddSearchQueryToLocalHistoryUseCase @Inject constructor(
    val userRepository: UserRepository
) {
    operator fun invoke(query: String) = userRepository.addSearchQueryToLocalHistory(query)
}