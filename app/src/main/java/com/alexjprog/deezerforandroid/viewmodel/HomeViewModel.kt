package com.alexjprog.deezerforandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexjprog.deezerforandroid.domain.usecase.GetChartsUseCase
import com.alexjprog.deezerforandroid.model.ContentCategory
import com.alexjprog.deezerforandroid.ui.adapter.complex.ComplexListItem
import com.alexjprog.deezerforandroid.util.CHARTS_PREVIEW_AMOUNT
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val getChartsUseCase: GetChartsUseCase
) : ViewModel() {
    private val _feed = MutableLiveData<List<ComplexListItem>>()
    val feed: LiveData<List<ComplexListItem>> by this::_feed

    init {
        loadFeed()
    }

    fun loadFeed() {
        viewModelScope.launch {
            getChartsUseCase(CHARTS_PREVIEW_AMOUNT).collect { list ->
                val newFeed = mutableListOf<ComplexListItem>()
                newFeed.add(ComplexListItem.TitleItem(ContentCategory.CHARTS))
                newFeed.add(ComplexListItem.HorizontalTrackListItem(list))
                _feed.value = newFeed
            }
        }
    }
}