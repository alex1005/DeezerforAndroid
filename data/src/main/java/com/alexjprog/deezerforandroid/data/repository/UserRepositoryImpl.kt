package com.alexjprog.deezerforandroid.data.repository

import com.alexjprog.deezerforandroid.data.mapper.IApiMapper
import com.alexjprog.deezerforandroid.data.storage.IDeezerDataSource
import com.alexjprog.deezerforandroid.data.storage.sharedprefs.LoginStore
import com.alexjprog.deezerforandroid.domain.model.SearchSuggestionModel
import com.alexjprog.deezerforandroid.domain.repository.UserRepository
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val loginStore: LoginStore,
    private val deezerSource: IDeezerDataSource,
    private val apiMapper: IApiMapper
): UserRepository {
    override fun getUserAccessToken(): String? = loginStore.userToken

    override fun saveUserAccessToken(token: String) {
        loginStore.userToken = token
    }

    override fun getSearchHistory(): Observable<List<SearchSuggestionModel>> =
        deezerSource.getSearchHistory().map { list ->
            list.map { apiMapper.mapSearchHistoryResult(it) }
        }
}