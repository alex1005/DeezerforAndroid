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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.flow.map
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

            val recommendationsPart = getRecommendationsUseCase(RECOMMENDATIONS_PREVIEW_SIZE)
                .map {
                    getNewFeedCategoryWithMoreAction(
                        ContentCategory.RECOMMENDATIONS,
                        it
                    )
                }.catch { isError = true }
                .flowOn(dataCoroutineContext)

            val chartsPart = getChartsUseCase(CHARTS_PREVIEW_SIZE)
                .map {
                    getNewFeedCategoryWithMoreAction(
                        ContentCategory.CHARTS,
                        it
                    )
                }.catch { isError = true }
                .flowOn(dataCoroutineContext)

            val flowPart = getFlowUseCase(FLOW_PREVIEW_SIZE)
                .map {
                    getNewFeedCategoryWithMoreAction(
                        ContentCategory.FLOW,
                        it
                    )
                }.catch { isError = true }
                .flowOn(dataCoroutineContext)

            newFeed.addNewFeedCategory(
                recommendationsPart.lastOrNull(),
                chartsPart.lastOrNull(),
                flowPart.lastOrNull()
            )

            _feed.postDataOrError(
                isError,
                newFeed
            )
        }
    }
}