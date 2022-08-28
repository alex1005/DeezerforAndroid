package com.alexjprog.deezerforandroid.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.alexjprog.deezerforandroid.domain.model.MediaItemModel
import com.alexjprog.deezerforandroid.domain.usecase.GetSearchResultsUseCase
import com.alexjprog.deezerforandroid.util.SEARCH_PAGE_SIZE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchResultsViewModel @Inject constructor(
    private val getSearchResultsUseCase: GetSearchResultsUseCase,
) : ViewModel() {

    private var _resultsFlow: Flow<PagingData<MediaItemModel>>? = null
    val resultsFlow: Flow<PagingData<MediaItemModel>>?
        get() = _resultsFlow

    fun loadResults(query: String) {
        _resultsFlow = getSearchResultsUseCase(SEARCH_PAGE_SIZE, query)
            .map { pagingData -> pagingData.map { it } }
            .cachedIn(viewModelScope)
            .flowOn(Dispatchers.Default)
    }
}