package com.alexjprog.deezerforandroid.data.storage.api

import com.alexjprog.deezerforandroid.data.storage.IDeezerDataSource
import com.alexjprog.deezerforandroid.data.storage.api.model.AlbumApiData
import com.alexjprog.deezerforandroid.data.storage.api.model.SearchHistoryResultApiData
import com.alexjprog.deezerforandroid.data.storage.api.model.TrackApiData
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow
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

    /* [pageSize] must be constant when changing [page] argument!!!
    * It happens because the API accepts queries with item offset and not page offset.
    * So to calculate page offset we multiply page number by page size where page size is constant */
    override fun getChartsPage(page: Int, pageSize: Int): Flow<List<TrackApiData>> = flow {
        val pageOffset = page * pageSize
        val response = api.getCharts(pageOffset, pageSize)
        val body = response.body()
        if (!response.isSuccessful || body == null) throw NoSuchElementException()
        if (isOAuthException(body.error)) emit(emptyList())
        else if (body.data == null) throw NoSuchElementException()
        else emit(body.data)
    }.flowOn(apiCoroutineContext)

    override fun getFlowPage(page: Int, pageSize: Int): Flow<List<TrackApiData>> = flow {
        val pageOffset = page * pageSize
        val response = api.getFlow(pageOffset, pageSize)
        val body = response.body()
        if (!response.isSuccessful || body == null) throw NoSuchElementException()
        if (isOAuthException(body.error)) emit(emptyList())
        else if (body.data == null) throw NoSuchElementException()
        else emit(body.data)
    }.flowOn(apiCoroutineContext)

    override fun getRecommendationsPage(page: Int, pageSize: Int): Flow<List<TrackApiData>> = flow {
        val pageOffset = page * pageSize
        val response = api.getRecommendations(pageOffset, pageSize)
        val body = response.body()
        if (!response.isSuccessful || body == null) throw NoSuchElementException()
        if (isOAuthException(body.error)) emit(emptyList())
        else if (body.data == null) throw NoSuchElementException()
        else emit(body.data)
    }.flowOn(apiCoroutineContext)

    override fun getEditorialSelectionPage(): Flow<List<AlbumApiData>> =
        flow {
            val response = api.getEditorialSelection()
            val body = response.body()
            if (!response.isSuccessful || body == null) throw NoSuchElementException()
            if (isOAuthException(body.error)) emit(emptyList())
            else if (body.data == null) throw NoSuchElementException()
            else emit(body.data)
        }.flowOn(apiCoroutineContext)

    override fun getSearchHistory(): Observable<List<SearchHistoryResultApiData>> =
        api.getSearchHistory().map { it.data ?: emptyList() }
            .onErrorReturn { listOf() }
            .toObservable()

    override fun getSearchSuggestionsForQuery(query: String): Observable<List<TrackApiData>> =
        api.getSearchSuggestionsForQuery(query).map { it.data ?: emptyList() }
            .onErrorReturn { listOf() }
            .toObservable()

    override fun getSearchResultsForQuery(page: Int, pageSize: Int, query: String) =
        flow {
            val pageOffset = page * pageSize
            val response = api.getSearchResultsForQuery(query, pageOffset, pageSize)
            val body = response.body()
            if (!response.isSuccessful || body == null) throw NoSuchElementException()
            if (isOAuthException(body.error)) emit(emptyList())
            else if (body.data == null) throw NoSuchElementException()
            else emit(body.data)
        }.flowOn(apiCoroutineContext)


    override fun getTrackInfo(id: Int): Observable<TrackApiData> =
        api.getTrackInfo(id).doOnSuccess {
            if (it.error != null) throw NoSuchElementException()
        }.toObservable()

    override fun getAlbumInfo(id: Int): Observable<AlbumApiData> =
        api.getAlbumInfo(id).doOnSuccess {
            if (it.error != null) throw NoSuchElementException()
        }.toObservable()

    private fun isOAuthException(errorMap: Map<String, String>?) =
        errorMap?.get("type") == "OAuthException"
}