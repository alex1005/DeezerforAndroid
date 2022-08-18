package com.alexjprog.deezerforandroid.ui.mvp.contract

import com.alexjprog.deezerforandroid.domain.model.SearchSuggestionModel
import com.alexjprog.deezerforandroid.presenter.MVPPresenter
import com.alexjprog.deezerforandroid.ui.mvp.BaseView

interface SearchContract {
    interface View : BaseView {
        fun updateSearchSuggestions(data: List<SearchSuggestionModel>)
    }

    interface Presenter : MVPPresenter<View> {
        fun subscribeToSearchInput()
        fun postSearchQuery(query: String)
        fun saveQueryToHistory(query: String)
    }
}