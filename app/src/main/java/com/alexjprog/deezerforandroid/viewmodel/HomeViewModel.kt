package com.alexjprog.deezerforandroid.viewmodel

import androidx.lifecycle.*
import com.alexjprog.deezerforandroid.domain.usecase.GetChartsUseCase
import com.alexjprog.deezerforandroid.ui.mvvm.MoreContentFragment
import com.alexjprog.deezerforandroid.ui.adapter.complex.ComplexListItem
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
            getChartsUseCase().collect { list ->
                val newFeed = mutableListOf<ComplexListItem>()
                newFeed.add(ComplexListItem.TitleItem(MoreContentFragment.ContentCategory.CHARTS))
                newFeed.add(ComplexListItem.HorizontalTrackListItem(list))
                _feed.value = newFeed
            }
        }
    }
}