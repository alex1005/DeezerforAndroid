package com.alexjprog.deezerforandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.alexjprog.deezerforandroid.domain.usecase.GetEditorialReleasesUseCase
import com.alexjprog.deezerforandroid.domain.usecase.GetEditorialSelectionUseCase
import com.alexjprog.deezerforandroid.model.ContentCategory
import com.alexjprog.deezerforandroid.ui.adapter.complex.ComplexListItem
import com.alexjprog.deezerforandroid.util.addNewFeedCategory
import com.alexjprog.deezerforandroid.util.getNewFeedCategory
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.CoroutineContext

class EditorialViewModel @Inject constructor(
    private val getEditorialSelectionUseCase: GetEditorialSelectionUseCase,
    private val getEditorialReleasesUseCase: GetEditorialReleasesUseCase,
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
            val weeklySelectionPart = async {
                val content = getEditorialSelectionUseCase()
                    .catch { isError = true }
                    .lastOrNull()
                getNewFeedCategory(
                    ContentCategory.EDIT_SELECTION,
                    content
                )
            }
            val releasesPart = async {
                val content = getEditorialReleasesUseCase()
                    .catch { isError = true }
                    .lastOrNull()
                getNewFeedCategory(
                    ContentCategory.EDIT_RELEASES,
                    content
                )
            }
            newFeed.addNewFeedCategory(
                weeklySelectionPart.await(),
                releasesPart.await()
            )
            _feed.postDataOrError(isError, newFeed)
        }
    }
}