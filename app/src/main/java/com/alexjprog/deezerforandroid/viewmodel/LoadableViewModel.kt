package com.alexjprog.deezerforandroid.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alexjprog.deezerforandroid.ui.adapter.complex.ComplexListItem

abstract class LoadableViewModel : ViewModel() {
    protected val _isLoading = MutableLiveData<Boolean?>()
    val isLoading: LiveData<Boolean?> by ::_isLoading

    protected fun MutableLiveData<List<ComplexListItem>>.postDataOrError(
        isError: Boolean,
        data: List<ComplexListItem>?
    ) {
        if (isError) _isLoading.postValue(null)
        else {
            this.postValue(data)
            _isLoading.postValue(false)
        }
    }
}