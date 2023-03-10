package com.alexjprog.deezerforandroid.presenter

import com.alexjprog.deezerforandroid.domain.usecase.AddSearchQueryToLocalHistoryUseCase
import com.alexjprog.deezerforandroid.domain.usecase.GetSearchSuggestionsUseCase
import com.alexjprog.deezerforandroid.ui.mvp.contract.SearchContract
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SearchPresenter @Inject constructor(
    private val getSearchSuggestionsUseCase: GetSearchSuggestionsUseCase,
    private val addSearchQueryToLocalHistoryUseCase: AddSearchQueryToLocalHistoryUseCase
): BasePresenter<SearchContract.View>(), SearchContract.Presenter {
    private val searchInputEvents = PublishSubject.create<String>()

    override fun subscribeToSearchInput() {
        searchInputEvents
            .observeOn(Schedulers.io())
            .debounce(DEBOUNCE_INTERVAL_LENGTH, DEBOUNCE_INTERVAL_UNITS)
            .switchMap { getSearchSuggestionsUseCase(it) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                view?.updateSearchSuggestions(it)
            }.addDisposable()
    }

    override fun postSearchQuery(query: String) {
        searchInputEvents.onNext(query)
    }

    override fun saveQueryToHistory(query: String) {
        addSearchQueryToLocalHistoryUseCase(query)
    }

    companion object {
        private const val DEBOUNCE_INTERVAL_LENGTH = 200L
        private val DEBOUNCE_INTERVAL_UNITS = TimeUnit.MILLISECONDS
    }

}