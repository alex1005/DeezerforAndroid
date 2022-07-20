package com.alexjprog.deezerforandroid.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.alexjprog.deezerforandroid.data.mapper.IApiMapper
import com.alexjprog.deezerforandroid.data.storage.IDeezerDataSource
import com.alexjprog.deezerforandroid.data.storage.paging.TrackPagingSource
import com.alexjprog.deezerforandroid.domain.model.SearchSuggestionModel
import com.alexjprog.deezerforandroid.domain.model.TrackModel
import com.alexjprog.deezerforandroid.domain.model.params.ContentCategoryParam
import com.alexjprog.deezerforandroid.domain.repository.MediaRepository
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor(
    private val deezerSource: IDeezerDataSource,
    private val apiMapper: IApiMapper
): MediaRepository {
    override fun getChartsPreview(amount: Int): Flow<List<TrackModel>> =
        deezerSource.getChartsPage(0, amount)
            .map { list -> list.map { apiMapper.mapTrack(it) } }

    override fun getCategoryContent(
        pageSize: Int,
        category: ContentCategoryParam
    ): Flow<PagingData<TrackModel>> =
        Pager(PagingConfig(pageSize)) { TrackPagingSource(category, deezerSource) }
            .flow
            .map { pagingData -> pagingData.map { apiMapper.mapTrack(it) } }

    override fun getSearchResultsForQuery(query: String): Observable<List<SearchSuggestionModel>> =
        deezerSource.getSearchResultsForQuery(query)
            .map { list -> list.map { apiMapper.mapSearchResult(it) } }
}