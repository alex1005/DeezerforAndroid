package com.alexjprog.deezerforandroid.domain.repository

import com.alexjprog.deezerforandroid.domain.model.SearchSuggestionModel
import io.reactivex.rxjava3.core.Observable

interface UserRepository {
    fun getUserAccessToken(): String?
    fun saveUserAccessToken(token: String)

    fun getSearchHistory(query: String): Observable<List<SearchSuggestionModel>>
}