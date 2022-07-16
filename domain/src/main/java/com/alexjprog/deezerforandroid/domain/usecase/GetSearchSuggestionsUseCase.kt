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
        return userRepository.getSearchHistory(query)
    }
}