package com.alexjprog.deezerforandroid.data.storage

import com.alexjprog.deezerforandroid.data.storage.api.model.SearchHistoryResultApiData
import com.alexjprog.deezerforandroid.data.storage.api.model.TrackApiData
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow

interface IDeezerDataSource {
    fun getChartsPage(page: Int, amount: Int): Flow<List<TrackApiData>>

    fun getSearchHistory(): Observable<List<SearchHistoryResultApiData>>
    fun getSearchResultsForQuery(query: String): Observable<List<TrackApiData>>
}