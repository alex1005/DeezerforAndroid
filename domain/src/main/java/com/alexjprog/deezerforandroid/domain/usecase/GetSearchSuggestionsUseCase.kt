package com.alexjprog.deezerforandroid.domain.usecase

import com.alexjprog.deezerforandroid.domain.model.SearchSuggestionModel
import com.alexjprog.deezerforandroid.domain.repository.MediaRepository
import com.alexjprog.deezerforandroid.domain.repository.UserRepository
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class GetSearchSuggestionsUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val mediaRepository: MediaRepository
) {
    operator fun invoke(query: String): Observable<List<SearchSuggestionModel>> {
        val history = userRepository.getSearchHistory()
            .map { list -> list.filter { it.title.contains(query) } }
        val search = mediaRepository.getSearchResultsForQuery(query)
        return Observable.zip(
            history,
            search
        ) { historyList, searchList -> historyList + searchList }
    }
}