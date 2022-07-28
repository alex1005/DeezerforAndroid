package com.alexjprog.deezerforandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexjprog.deezerforandroid.domain.usecase.GetEditorialSelection
import com.alexjprog.deezerforandroid.model.ContentCategory
import com.alexjprog.deezerforandroid.ui.adapter.complex.ComplexListItem
import com.alexjprog.deezerforandroid.util.EDIT_SELECTION_PREVIEW_SIZE
import com.alexjprog.deezerforandroid.util.addNewFeedCategory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class EditorialViewModel @Inject constructor(
    private val getEditorialSelection: GetEditorialSelection,
) : ViewModel() {
    private val _feed = MutableLiveData<List<ComplexListItem>>()
    val feed: LiveData<List<ComplexListItem>> by this::_feed

    init {
        loadFeed()
    }

    fun loadFeed() {
        viewModelScope.launch {
            val newFeed = mutableListOf<ComplexListItem>()
            getEditorialSelection(EDIT_SELECTION_PREVIEW_SIZE).collectLatest { content ->
                newFeed.addNewFeedCategory(ContentCategory.EDIT_SELECTION, content)
            }
            _feed.postValue(newFeed)
        }
    }
}