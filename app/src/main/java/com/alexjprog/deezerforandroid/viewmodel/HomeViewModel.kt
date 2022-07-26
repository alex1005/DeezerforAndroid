package com.alexjprog.deezerforandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexjprog.deezerforandroid.domain.model.TrackModel
import com.alexjprog.deezerforandroid.domain.usecase.GetChartsUseCase
import com.alexjprog.deezerforandroid.domain.usecase.GetFlowUseCase
import com.alexjprog.deezerforandroid.domain.usecase.GetRecommendationsUseCase
import com.alexjprog.deezerforandroid.model.ContentCategory
import com.alexjprog.deezerforandroid.ui.adapter.complex.ComplexListItem
import com.alexjprog.deezerforandroid.util.CHARTS_PREVIEW_SIZE
import com.alexjprog.deezerforandroid.util.FLOW_PREVIEW_SIZE
import com.alexjprog.deezerforandroid.util.RECOMMENDATIONS_PREVIEW_SIZE
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val getChartsUseCase: GetChartsUseCase,
    private val getFlowUseCase: GetFlowUseCase,
    private val getRecommendationsUseCase: GetRecommendationsUseCase
) : ViewModel() {
    private val _feed = MutableLiveData<List<ComplexListItem>>()
    val feed: LiveData<List<ComplexListItem>> by this::_feed

    init {
        loadFeed()
    }

    fun loadFeed() {
        viewModelScope.launch {
            val newFeed = mutableListOf<ComplexListItem>()
            getRecommendationsUseCase(RECOMMENDATIONS_PREVIEW_SIZE).collectLatest { content ->
                newFeed.addNewFeedCategory(ContentCategory.RECOMMENDATIONS, content)
            }
            getChartsUseCase(CHARTS_PREVIEW_SIZE).collectLatest { content ->
                newFeed.addNewFeedCategory(ContentCategory.CHARTS, content)
            }
            getFlowUseCase(FLOW_PREVIEW_SIZE).collectLatest { content ->
                newFeed.addNewFeedCategory(ContentCategory.FLOW, content)
            }
            _feed.postValue(newFeed)
        }
    }

    private fun MutableList<ComplexListItem>.addNewFeedCategory(
        category: ContentCategory,
        content: List<TrackModel>
    ) {
        if (content.isEmpty()) return
        val titleItem = ComplexListItem.TitleItem(category)
        val contentItem = ComplexListItem.HorizontalTrackListItem(content)
        addAll(listOf(titleItem, contentItem))
    }
}