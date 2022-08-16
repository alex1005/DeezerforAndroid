package com.alexjprog.deezerforandroid.domain.repository

import androidx.paging.PagingData
import com.alexjprog.deezerforandroid.domain.model.AlbumModel
import com.alexjprog.deezerforandroid.domain.model.MediaItemModel
import com.alexjprog.deezerforandroid.domain.model.SearchSuggestionModel
import com.alexjprog.deezerforandroid.domain.model.TrackModel
import com.alexjprog.deezerforandroid.domain.model.params.ContentCategoryParam
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow

interface MediaRepository {
    fun getChartsPreview(amount: Int): Flow<List<TrackModel>>
    fun getFlowPreview(amount: Int): Flow<List<TrackModel>>
    fun getRecommendationsPreview(amount: Int): Flow<List<TrackModel>>
    fun getEditorialSelectionPreview(): Flow<List<AlbumModel>>

    fun getCategoryContent(pageSize: Int, category: ContentCategoryParam):
            Flow<PagingData<MediaItemModel>>

    fun getSearchSuggestionsForQuery(query: String): Observable<List<SearchSuggestionModel>>
    fun getSearchResultsForQuery(pageSize: Int, query: String): Flow<PagingData<MediaItemModel>>

    fun getTrackInfo(id: Int): Observable<TrackModel>
    fun getAlbumInfo(id: Int): Observable<AlbumModel>
}