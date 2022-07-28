package com.alexjprog.deezerforandroid.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.alexjprog.deezerforandroid.domain.model.MediaItemModel
import com.alexjprog.deezerforandroid.domain.usecase.GetMoreContentUseCase
import com.alexjprog.deezerforandroid.model.ContentCategory
import com.alexjprog.deezerforandroid.util.CONTENT_PAGE_SIZE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MoreContentViewModel @Inject constructor(
    private val getMoreContentUseCase: GetMoreContentUseCase,
) : ViewModel() {

    private var _contentFlow: Flow<PagingData<MediaItemModel>>? = null
    val contentFlow: Flow<PagingData<MediaItemModel>>?
        get() = _contentFlow

    fun loadCategory(category: ContentCategory) {
        _contentFlow = getMoreContentUseCase(CONTENT_PAGE_SIZE, category.toCategoryParam())
            .map { pagingData -> pagingData.map { it as MediaItemModel } }
            .cachedIn(viewModelScope)
            .flowOn(Dispatchers.Default)
    }
}