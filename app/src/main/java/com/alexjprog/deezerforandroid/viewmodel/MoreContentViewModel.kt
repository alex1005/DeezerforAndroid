package com.alexjprog.deezerforandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexjprog.deezerforandroid.domain.model.TrackModel
import com.alexjprog.deezerforandroid.domain.usecase.GetChartsUseCase
import com.alexjprog.deezerforandroid.ui.MoreContentFragment
import kotlinx.coroutines.launch
import javax.inject.Inject

class MoreContentViewModel @Inject constructor(
    private val getChartsUseCase: GetChartsUseCase
) : ViewModel() {
    private val _content = MutableLiveData<List<TrackModel>>()
    val content: LiveData<List<TrackModel>> by this::_content

    fun loadCategory(category: MoreContentFragment.ContentCategory) {
        when(category) {
            MoreContentFragment.ContentCategory.CHARTS -> loadCharts()
        }
    }

    fun loadCharts() {
        viewModelScope.launch {
            getChartsUseCase().collect { list ->
                _content.value = list
            }
        }
    }
}