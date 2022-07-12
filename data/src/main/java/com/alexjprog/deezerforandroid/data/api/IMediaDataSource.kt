package com.alexjprog.deezerforandroid.data.api

import com.alexjprog.deezerforandroid.data.api.model.TrackApiData
import kotlinx.coroutines.flow.Flow

interface IMediaDataSource {
    fun getCharts(): Flow<List<TrackApiData>>
}