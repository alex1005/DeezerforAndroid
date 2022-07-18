package com.alexjprog.deezerforandroid.domain.repository

import com.alexjprog.deezerforandroid.domain.model.SearchSuggestionModel
import com.alexjprog.deezerforandroid.domain.model.TrackModel
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow

interface MediaRepository {
    fun getCharts(): Flow<List<TrackModel>>

    fun getSearchResultsForQuery(query: String): Observable<List<SearchSuggestionModel>>
}