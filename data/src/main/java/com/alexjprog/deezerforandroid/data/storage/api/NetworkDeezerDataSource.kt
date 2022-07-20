package com.alexjprog.deezerforandroid.data.storage.api

import com.alexjprog.deezerforandroid.data.storage.IDeezerDataSource
import com.alexjprog.deezerforandroid.data.storage.api.model.SearchHistoryResultApiData
import com.alexjprog.deezerforandroid.data.storage.api.model.TrackApiData
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

    override fun getChartsPage(page: Int, amount: Int): Flow<List<TrackApiData>> = flow {
        val response = api.getCharts(page, amount)
        if (!response.isSuccessful) emit(listOf())
        emit(response.body()?.data ?: listOf())
    }.catch { emit(listOf()) }
        .flowOn(apiCoroutineContext)

    override fun getSearchHistory(): Observable<List<SearchHistoryResultApiData>> =
        api.getSearchHistory().map { it.data }
            .onErrorReturn { listOf() }
            .toObservable()

    override fun getSearchResultsForQuery(query: String): Observable<List<TrackApiData>> =
        api.getSearchResultsForQuery(query).map { it.data }
            .onErrorReturn { listOf() }
            .toObservable()
}