package com.alexjprog.deezerforandroid.domain.usecase

import com.alexjprog.deezerforandroid.domain.repository.MediaRepository
import javax.inject.Inject

class GetEditorialSelectionUseCase @Inject constructor(
    private val mediaRepository: MediaRepository
) {
    operator fun invoke() = mediaRepository.getEditorialSelectionPage()
}
