package com.alexjprog.deezerforandroid.domain.usecase

import com.alexjprog.deezerforandroid.domain.model.TrackModel
import com.alexjprog.deezerforandroid.domain.repository.MediaRepository
import kotlinx.coroutines.flow.Flow

class GetChartsUseCase(private val repository: MediaRepository) {
    operator fun invoke(): Flow<List<TrackModel>> = repository.getCharts()
}