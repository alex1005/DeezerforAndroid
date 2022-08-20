package com.alexjprog.deezerforandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.alexjprog.deezerforandroid.domain.usecase.GetChartsUseCase
import com.alexjprog.deezerforandroid.domain.usecase.GetFlowUseCase
import com.alexjprog.deezerforandroid.domain.usecase.GetRecommendationsUseCase
import com.alexjprog.deezerforandroid.model.ContentCategory
import com.alexjprog.deezerforandroid.ui.adapter.complex.ComplexListItem
import com.alexjprog.deezerforandroid.util.CHARTS_PREVIEW_SIZE
import com.alexjprog.deezerforandroid.util.FLOW_PREVIEW_SIZE
import com.alexjprog.deezerforandroid.util.RECOMMENDATIONS_PREVIEW_SIZE
import com.alexjprog.deezerforandroid.util.addNewFeedCategoryWithMoreAction
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val getChartsUseCase: GetChartsUseCase,
    private val getFlowUseCase: GetFlowUseCase,
    private val getRecommendationsUseCase: GetRecommendationsUseCase,
) : LoadableViewModel() {
    private val _feed = MutableLiveData<List<ComplexListItem>>()
    val feed: LiveData<List<ComplexListItem>> by this::_feed

    init {
        loadFeed()
    }

    fun loadFeed() {
        viewModelScope.launch {
            _isLoading.postValue(true)
            var isError = false
            val newFeed = mutableListOf<ComplexListItem>()
            getRecommendationsUseCase(RECOMMENDATIONS_PREVIEW_SIZE).catch { isError = true }
                .collectLatest { content ->
                    newFeed.addNewFeedCategoryWithMoreAction(
                        ContentCategory.RECOMMENDATIONS,
                        content
                    )
                }
            getChartsUseCase(CHARTS_PREVIEW_SIZE).catch { isError = true }
                .collectLatest { content ->
                    newFeed.addNewFeedCategoryWithMoreAction(
                        ContentCategory.CHARTS,
                        content
                    )
                }
            getFlowUseCase(FLOW_PREVIEW_SIZE).catch { isError = true }.collectLatest { content ->
                newFeed.addNewFeedCategoryWithMoreAction(
                    ContentCategory.FLOW,
                    content
                )
            }
            _feed.postDataOrError(isError, newFeed)
        }
    }
}