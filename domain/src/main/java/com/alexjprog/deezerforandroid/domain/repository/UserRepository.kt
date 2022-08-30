package com.alexjprog.deezerforandroid.domain.repository

import com.alexjprog.deezerforandroid.domain.model.SearchSuggestionModel
import com.alexjprog.deezerforandroid.domain.model.UserInfoModel
import io.reactivex.rxjava3.core.Observable

interface UserRepository {
    fun getUserAccessToken(): String?
    fun saveUserAccessToken(token: String)
    fun deleteUserAccessToken()

    fun getSearchHistory(query: String): Observable<List<SearchSuggestionModel>>
    fun addSearchQueryToLocalHistory(query: String)

    fun getUserInfo(): Observable<UserInfoModel>
    fun getUserInfoWithToken(token: String): Observable<UserInfoModel>
}