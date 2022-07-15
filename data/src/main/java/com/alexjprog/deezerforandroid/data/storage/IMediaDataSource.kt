package com.alexjprog.deezerforandroid.data.storage

import com.alexjprog.deezerforandroid.data.api.model.TrackApiData
import kotlinx.coroutines.flow.Flow

interface IMediaDataSource {
    fun getCharts(): Flow<List<TrackApiData>>
}