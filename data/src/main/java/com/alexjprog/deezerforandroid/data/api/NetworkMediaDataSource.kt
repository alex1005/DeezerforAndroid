package com.alexjprog.deezerforandroid.data.api

import com.alexjprog.deezerforandroid.data.api.model.TrackApiData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class NetworkMediaDataSource(
    private val api: MediaService,
    private val apiDispatcher: CoroutineDispatcher
):IMediaDataSource {

    override fun getCharts(): Flow<List<TrackApiData>> = flow {
        val response = api.getCharts()
        if (!response.isSuccessful) emit(listOf())
        emit(response.body() ?: listOf())
    }.catch { emit(listOf()) }
        .flowOn(apiDispatcher)
}