package com.alexjprog.deezerforandroid.domain.usecase

import com.alexjprog.deezerforandroid.domain.model.TrackModel
import com.alexjprog.deezerforandroid.domain.repository.MediaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetChartsUseCase @Inject constructor(private val mediaRepository: MediaRepository) {
    operator fun invoke(amount: Int): Flow<List<TrackModel>> =
        mediaRepository.getChartsPreview(amount)
}