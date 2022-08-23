package com.alexjprog.deezerforandroid.data.storage.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.alexjprog.deezerforandroid.data.mapper.IApiMapper
import com.alexjprog.deezerforandroid.data.storage.IDeezerDataSource
import com.alexjprog.deezerforandroid.domain.model.MediaItemModel
import kotlinx.coroutines.flow.lastOrNull

class SearchResultsPagingSource(
    private val query: String,
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
            val trackPage = dataSource.getSearchResultsForQuery(page, params.loadSize, query)
                .lastOrNull()?.map { apiMapper.fromTrackApiData(it) } ?: emptyList()

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