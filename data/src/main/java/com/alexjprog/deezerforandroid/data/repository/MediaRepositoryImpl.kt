package com.alexjprog.deezerforandroid.data.repository

import com.alexjprog.deezerforandroid.data.mapper.IApiMapper
import com.alexjprog.deezerforandroid.data.storage.IDeezerDataSource
import com.alexjprog.deezerforandroid.domain.model.SearchSuggestionModel
import com.alexjprog.deezerforandroid.domain.model.TrackModel
import com.alexjprog.deezerforandroid.domain.repository.MediaRepository
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor(
    private val deezerSource: IDeezerDataSource,
    private val apiMapper: IApiMapper
): MediaRepository {
    override fun getCharts(): Flow<List<TrackModel>> =
        deezerSource.getCharts()
            .map { list -> list.map { apiMapper.mapTrack(it) } }

    override fun getSearchResultsForQuery(query: String): Observable<List<SearchSuggestionModel>> =
        deezerSource.getSearchResultsForQuery(query)
            .map { list -> list.map { apiMapper.mapSearchResult(it) } }
}