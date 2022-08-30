package com.alexjprog.deezerforandroid.data.storage

import com.alexjprog.deezerforandroid.data.storage.api.model.AlbumApiData
import com.alexjprog.deezerforandroid.data.storage.api.model.SearchHistoryResultApiData
import com.alexjprog.deezerforandroid.data.storage.api.model.TrackApiData
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow

interface IDeezerDataSource {
    fun getChartsPage(page: Int, pageSize: Int): Flow<List<TrackApiData>>
    fun getFlowPage(page: Int, pageSize: Int): Flow<List<TrackApiData>>
    fun getRecommendationsPage(page: Int, pageSize: Int): Flow<List<TrackApiData>>
    fun getEditorialSelectionPage(): Flow<List<AlbumApiData>>
    fun getEditorialReleasesPreview(): Flow<List<AlbumApiData>>

    fun getSearchHistory(): Observable<List<SearchHistoryResultApiData>>
    fun getSearchSuggestionsForQuery(query: String): Observable<List<TrackApiData>>

    fun getSearchResultsForQuery(page: Int, pageSize: Int, query: String): Flow<List<TrackApiData>>
    fun getTrackInfo(id: Int): Observable<TrackApiData>
    fun getAlbumInfo(id: Int): Observable<AlbumApiData>
}