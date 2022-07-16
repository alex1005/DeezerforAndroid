package com.alexjprog.deezerforandroid.data.api

import com.alexjprog.deezerforandroid.data.api.model.SearchHistoryResultApiData
import com.alexjprog.deezerforandroid.data.api.model.TrackApiData
import com.alexjprog.deezerforandroid.data.storage.IDeezerDataSource
import com.alexjprog.deezerforandroid.domain.model.SearchSuggestionModel
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.CoroutineContext

class NetworkDeezerDataSource @Inject constructor(
    private val api: DeezerService,
    @Named("api")
    private val apiCoroutineContext: CoroutineContext
): IDeezerDataSource {

    override fun getCharts(): Flow<List<TrackApiData>> = flow {
        val response = api.getCharts()
        if (!response.isSuccessful) emit(listOf())
        emit(response.body()?.data ?: listOf())
    }.catch { emit(listOf()) }
        .flowOn(apiCoroutineContext)

    override fun getSearchHistory(query: String): Observable<List<SearchHistoryResultApiData>> =
        api.getSearchHistory(query).map { it.data }
            .onErrorReturn { listOf() }
            .toObservable()
}