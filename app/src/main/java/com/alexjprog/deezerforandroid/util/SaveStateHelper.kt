package com.alexjprog.deezerforandroid.util

import android.os.Bundle
import com.alexjprog.deezerforandroid.domain.model.SearchSuggestionModel

object SaveStateHelper {
    fun saveSearchSuggestionModelList(outState: Bundle, data: List<SearchSuggestionModel>?) {
        if (data == null) return
        val titles = data.map { it.title }
        val historyMarks = data.map { it.isInHistory }
        outState.putStringArray(SEARCH_SUGGESTION_TITLES, titles.toTypedArray())
        outState.putBooleanArray(
            SEARCH_SUGGESTION_HISTORY_MARKS,
            historyMarks.toTypedArray().toBooleanArray()
        )
    }

    fun restoreSearchSuggestionModelList(outState: Bundle?): List<SearchSuggestionModel> {
        val titles = outState?.getStringArray(SEARCH_SUGGESTION_TITLES)
        val historyMarks = outState?.getBooleanArray(SEARCH_SUGGESTION_HISTORY_MARKS)
        return if (titles != null && historyMarks != null) {
            titles.zip(historyMarks.toTypedArray())
                .map { (title, isHistory) -> SearchSuggestionModel(title, isHistory) }
        } else listOf()
    }

    private const val SEARCH_SUGGESTION_TITLES = "search_titles"
    private const val SEARCH_SUGGESTION_HISTORY_MARKS = "search_history"
}