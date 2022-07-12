package com.alexjprog.deezerforandroid.domain.repository

import com.alexjprog.deezerforandroid.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface MediaRepository {
    fun getCharts(): Flow<List<Track>>
}