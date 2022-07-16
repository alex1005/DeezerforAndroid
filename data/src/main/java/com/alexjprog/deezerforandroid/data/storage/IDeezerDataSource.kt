package com.alexjprog.deezerforandroid.data.storage

import com.alexjprog.deezerforandroid.data.api.model.SearchHistoryResultApiData
import com.alexjprog.deezerforandroid.data.api.model.TrackApiData
import com.alexjprog.deezerforandroid.domain.model.SearchSuggestionModel
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow

interface IDeezerDataSource {
    fun getCharts(): Flow<List<TrackApiData>>

    fun getSearchHistory(query: String): Observable<List<SearchHistoryResultApiData>>
}