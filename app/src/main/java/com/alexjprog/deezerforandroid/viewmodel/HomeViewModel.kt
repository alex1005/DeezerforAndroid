package com.alexjprog.deezerforandroid.viewmodel

import androidx.lifecycle.*
import com.alexjprog.deezerforandroid.data.api.MediaService
import com.alexjprog.deezerforandroid.data.api.NetworkMediaDataSource
import com.alexjprog.deezerforandroid.data.mapper.DefaultMediaMapper
import com.alexjprog.deezerforandroid.data.repository.MediaRepositoryImpl
import com.alexjprog.deezerforandroid.domain.repository.MediaRepository
import com.alexjprog.deezerforandroid.domain.usecase.GetChartsUseCase
import com.alexjprog.deezerforandroid.ui.adapter.complex.ComplexListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val getChartsUseCase: GetChartsUseCase) : ViewModel() {
    private val _feed = MutableLiveData<List<ComplexListItem>>()
    val feed: LiveData<List<ComplexListItem>> by this::_feed

    init {
        loadFeed()
    }

    fun loadFeed() {
        viewModelScope.launch {
            getChartsUseCase().collect { list ->
                val newFeed = mutableListOf<ComplexListItem>()
                newFeed.add(ComplexListItem.TitleItem("Charts"))
                newFeed.add(ComplexListItem.HorizontalTrackListItem(list))
                _feed.value = newFeed
            }
        }
    }
}