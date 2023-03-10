package com.alexjprog.deezerforandroid.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.alexjprog.deezerforandroid.data.mapper.IApiMapper
import com.alexjprog.deezerforandroid.data.mapper.IDbMapper
import com.alexjprog.deezerforandroid.data.storage.IDeezerDataSource
import com.alexjprog.deezerforandroid.data.storage.db.LocalDeezerDataSource
import com.alexjprog.deezerforandroid.data.storage.paging.MediaPagingSource
import com.alexjprog.deezerforandroid.data.storage.paging.SearchResultsPagingSource
import com.alexjprog.deezerforandroid.domain.model.AlbumModel
import com.alexjprog.deezerforandroid.domain.model.MediaItemModel
import com.alexjprog.deezerforandroid.domain.model.SearchSuggestionModel
import com.alexjprog.deezerforandroid.domain.model.TrackModel
import com.alexjprog.deezerforandroid.domain.model.params.ContentCategoryParam
import com.alexjprog.deezerforandroid.domain.repository.MediaRepository
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor(
    private val deezerSource: IDeezerDataSource,
    private val localDeezerSource: LocalDeezerDataSource,
    private val apiMapper: IApiMapper,
    private val dbMapper: IDbMapper
) : MediaRepository {
    override fun getChartsPreview(amount: Int): Flow<List<TrackModel>> =
        deezerSource.getChartsPage(0, amount)
            .map { list -> list.map { apiMapper.fromTrackApiData(it) } }

    override fun getFlowPreview(amount: Int): Flow<List<TrackModel>> =
        deezerSource.getFlowPage(0, amount)
            .map { list -> list.map { apiMapper.fromTrackApiData(it) } }

    override fun getRecommendationsPreview(amount: Int): Flow<List<TrackModel>> =
        deezerSource.getRecommendationsPage(0, amount)
            .map { list -> list.map { apiMapper.fromTrackApiData(it) } }

    override fun getEditorialSelectionPage(): Flow<List<AlbumModel>> =
        deezerSource.getEditorialSelectionPage()
            .map { list -> list.map { apiMapper.fromAlbumApiData(it) } }
            .onEach { newData ->
                localDeezerSource.rewriteEditorialCategoryCache(newData.map {
                    dbMapper.toMediaCacheEntity(it, ContentCategoryParam.EDIT_SELECTION)
                })
            }

    override fun getEditorialReleasesPage(): Flow<List<AlbumModel>> =
        deezerSource.getEditorialReleasesPreview()
            .map { list -> list.map { apiMapper.fromAlbumApiData(it) } }
            .onEach { newData ->
                localDeezerSource.rewriteEditorialCategoryCache(newData.map {
                    dbMapper.toMediaCacheEntity(it, ContentCategoryParam.EDIT_RELEASES)
                })
            }

    override fun getEditorialSelectionCache(): Flow<List<AlbumModel>> =
        localDeezerSource.getEditorialSelectionCache()
            .map { cacheList -> cacheList.map { dbMapper.fromMediaCacheEntity(it) as AlbumModel } }

    override fun getEditorialReleasesCache(): Flow<List<AlbumModel>> =
        localDeezerSource.getEditorialReleasesCache()
            .map { cacheList -> cacheList.map { dbMapper.fromMediaCacheEntity(it) as AlbumModel } }

    override fun getCategoryContent(
        pageSize: Int,
        category: ContentCategoryParam
    ): Flow<PagingData<MediaItemModel>> =
        Pager(PagingConfig(pageSize = pageSize, initialLoadSize = pageSize)) {
            MediaPagingSource(category, deezerSource, apiMapper)
        }.flow

    override fun getSearchSuggestionsForQuery(query: String): Observable<List<SearchSuggestionModel>> =
        deezerSource.getSearchSuggestionsForQuery(query)
            .map { list ->
                list.map { apiMapper.fromSearchResultApiData(it) }
                    .distinctBy { it.title.uppercase() }
            }

    override fun getSearchResultsForQuery(
        pageSize: Int,
        query: String
    ): Flow<PagingData<MediaItemModel>> =
        Pager(PagingConfig(pageSize = pageSize, initialLoadSize = pageSize)) {
            SearchResultsPagingSource(query, deezerSource, apiMapper)
        }.flow

    override fun getTrackInfo(id: Int): Observable<TrackModel> =
        deezerSource.getTrackInfo(id).map { apiMapper.fromTrackApiData(it) }

    override fun getAlbumInfo(id: Int): Observable<AlbumModel> =
        deezerSource.getAlbumInfo(id).map { apiMapper.fromAlbumApiData(it) }
}