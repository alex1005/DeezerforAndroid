package com.alexjprog.deezerforandroid.data.storage.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.alexjprog.deezerforandroid.data.mapper.IApiMapper
import com.alexjprog.deezerforandroid.data.storage.IDeezerDataSource
import com.alexjprog.deezerforandroid.domain.model.MediaItemModel
import com.alexjprog.deezerforandroid.domain.model.params.ContentCategoryParam
import kotlinx.coroutines.flow.lastOrNull

class MediaPagingSource(
    private val categoryParam: ContentCategoryParam,
    private val dataSource: IDeezerDataSource,
    private val apiMapper: IApiMapper
) : PagingSource<Int, MediaItemModel>() {
    override fun getRefreshKey(state: PagingState<Int, MediaItemModel>): Int? =
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MediaItemModel> {
        return try {
            val page = params.key ?: FIRST_PAGE
            val trackPage = when (categoryParam) {
                ContentCategoryParam.CHARTS -> dataSource.getChartsPage(page, params.loadSize)
                    .lastOrNull()?.map { apiMapper.fromTrackApiData(it) } ?: listOf()
                ContentCategoryParam.FLOW -> dataSource.getFlowPage(page, params.loadSize)
                    .lastOrNull()?.map { apiMapper.fromTrackApiData(it) } ?: listOf()
                ContentCategoryParam.RECOMMENDATIONS ->
                    dataSource.getRecommendationsPage(page, params.loadSize)
                        .lastOrNull()?.map { apiMapper.fromTrackApiData(it) } ?: listOf()
                else -> throw IllegalArgumentException("Cannot page this category")
            }

            LoadResult.Page(
                trackPage,
                prevKey = if (page == FIRST_PAGE) null else page - 1,
                nextKey = if (trackPage.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    companion object {
        private const val FIRST_PAGE = 0
    }

}