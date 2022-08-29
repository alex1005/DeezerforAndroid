package com.alexjprog.deezerforandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.alexjprog.deezerforandroid.domain.usecase.GetChartsUseCase
import com.alexjprog.deezerforandroid.domain.usecase.GetFlowUseCase
import com.alexjprog.deezerforandroid.domain.usecase.GetRecommendationsUseCase
import com.alexjprog.deezerforandroid.model.ContentCategory
import com.alexjprog.deezerforandroid.ui.adapter.complex.ComplexListItem
import com.alexjprog.deezerforandroid.util.*
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.lastOrNull
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
            val recommendationsPart = async {
                val content =
                    getRecommendationsUseCase(RECOMMENDATIONS_PREVIEW_SIZE).catch { isError = true }
                        .lastOrNull()
                getNewFeedCategoryWithMoreAction(
                    ContentCategory.RECOMMENDATIONS,
                    content
                )
            }
            val chartsPart = async {
                val content = getChartsUseCase(CHARTS_PREVIEW_SIZE).catch { isError = true }
                    .lastOrNull()
                getNewFeedCategoryWithMoreAction(
                    ContentCategory.CHARTS,
                    content
                )
            }
            val flowPart = async {
                val content = getFlowUseCase(FLOW_PREVIEW_SIZE).catch { isError = true }
                    .lastOrNull()
                getNewFeedCategoryWithMoreAction(
                    ContentCategory.FLOW,
                    content
                )
            }
            newFeed.addNewFeedCategory(
                recommendationsPart.await(),
                chartsPart.await(),
                flowPart.await()
            )
            _feed.postDataOrError(
                isError,
                newFeed
            )
        }
    }
}