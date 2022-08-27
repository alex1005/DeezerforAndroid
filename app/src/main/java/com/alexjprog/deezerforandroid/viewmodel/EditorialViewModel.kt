package com.alexjprog.deezerforandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.alexjprog.deezerforandroid.domain.usecase.GetEditorialSelection
import com.alexjprog.deezerforandroid.model.ContentCategory
import com.alexjprog.deezerforandroid.ui.adapter.complex.ComplexListItem
import com.alexjprog.deezerforandroid.util.addNewFeedCategory
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.CoroutineContext

class EditorialViewModel @Inject constructor(
    private val getEditorialSelection: GetEditorialSelection,
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
            getEditorialSelection()
                .catch { isError = true }.collectLatest { content ->
                    newFeed.addNewFeedCategory(ContentCategory.EDIT_SELECTION, content)
                }
            _feed.postDataOrError(isError, newFeed)
        }
    }
}