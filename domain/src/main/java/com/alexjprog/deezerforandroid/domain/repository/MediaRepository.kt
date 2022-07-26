package com.alexjprog.deezerforandroid.domain.repository

import androidx.paging.PagingData
import com.alexjprog.deezerforandroid.domain.model.SearchSuggestionModel
import com.alexjprog.deezerforandroid.domain.model.TrackModel
import com.alexjprog.deezerforandroid.domain.model.params.ContentCategoryParam
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow

interface MediaRepository {
    fun getChartsPreview(amount: Int): Flow<List<TrackModel>>
    fun getFlowPreview(amount: Int): Flow<List<TrackModel>>
    fun getRecommendationsPreview(amount: Int): Flow<List<TrackModel>>

    fun getCategoryContent(pageSize: Int, category: ContentCategoryParam):
            Flow<PagingData<TrackModel>>

    fun getSearchResultsForQuery(query: String): Observable<List<SearchSuggestionModel>>
}