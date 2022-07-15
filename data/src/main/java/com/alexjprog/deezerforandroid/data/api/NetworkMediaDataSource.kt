package com.alexjprog.deezerforandroid.data.api

import com.alexjprog.deezerforandroid.data.api.model.TrackApiData
import com.alexjprog.deezerforandroid.data.storage.IMediaDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.CoroutineContext

class NetworkMediaDataSource @Inject constructor(
    private val api: DeezerService,
    @Named("api")
    private val apiCoroutineContext: CoroutineContext
): IMediaDataSource {

    override fun getCharts(): Flow<List<TrackApiData>> = flow {
        val response = api.getCharts()
        if (!response.isSuccessful) emit(listOf())
        emit(response.body()?.data ?: listOf())
    }.catch { emit(listOf()) }
        .flowOn(apiCoroutineContext)
}