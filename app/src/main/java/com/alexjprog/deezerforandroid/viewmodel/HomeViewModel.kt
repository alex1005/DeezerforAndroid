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
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.CoroutineContext

class HomeViewModel @Inject constructor(
    private val getChartsUseCase: GetChartsUseCase,
    private val getFlowUseCase: GetFlowUseCase,
    private val getRecommendationsUseCase: GetRecommendationsUseCase,
    @Named("data")
    private val dataCoroutineContext: CoroutineContext
) : LoadableViewModel() {
    private val _feed = MutableLiveData<List<ComplexListItem>?>()
    val feed: LiveData<List<ComplexListItem>?> by this::_feed

    init {
        loadFeed()
    }

    fun loadFeed() {
        viewModelScope.launch(dataCoroutineContext) {
            _feed.startLoading()
            var isError = false
            val newFeed = mutableListOf<ComplexListItem>()
            val recommendationsJob = launch {
                getRecommendationsUseCase(RECOMMENDATIONS_PREVIEW_SIZE).catch { isError = true }
                    .collectLatest { content ->
                        newFeed.addNewFeedCategoryWithMoreAction(
                            ContentCategory.RECOMMENDATIONS,
                            content
                        )
                    }
            }
            val chartsJob = launch {
                getChartsUseCase(CHARTS_PREVIEW_SIZE).catch { isError = true }
                    .collectLatest { content ->
                        newFeed.addNewFeedCategoryWithMoreAction(
                            ContentCategory.CHARTS,
                            content
                        )
                    }
            }
            val flowJob = launch {
                getFlowUseCase(FLOW_PREVIEW_SIZE).catch { isError = true }
                    .collectLatest { content ->
                        newFeed.addNewFeedCategoryWithMoreAction(
                            ContentCategory.FLOW,
                            content
                        )
                    }
            }
            joinAll(recommendationsJob, chartsJob, flowJob)
            _feed.postDataOrError(isError, newFeed)
        }
    }
}