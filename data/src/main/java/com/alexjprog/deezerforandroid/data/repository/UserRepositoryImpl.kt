package com.alexjprog.deezerforandroid.data.repository

import com.alexjprog.deezerforandroid.data.mapper.IApiMapper
import com.alexjprog.deezerforandroid.data.mapper.IDbMapper
import com.alexjprog.deezerforandroid.data.storage.IDeezerDataSource
import com.alexjprog.deezerforandroid.data.storage.ILocalDeezerDataSource
import com.alexjprog.deezerforandroid.data.storage.sharedprefs.LoginStore
import com.alexjprog.deezerforandroid.domain.model.SearchSuggestionModel
import com.alexjprog.deezerforandroid.domain.repository.UserRepository
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val loginStore: LoginStore,
    private val deezerSource: IDeezerDataSource,
    private val localDeezerSource: ILocalDeezerDataSource,
    private val apiMapper: IApiMapper,
    private val dbMapper: IDbMapper
): UserRepository {
    override fun getUserAccessToken(): String? = loginStore.userToken

    override fun saveUserAccessToken(token: String) {
        loginStore.userToken = token
    }

    override fun getSearchHistory(query: String): Observable<List<SearchSuggestionModel>> {
        val liveHistory = deezerSource.getSearchHistory().map { list ->
            list.map { apiMapper.fromSearchHistoryResultApiData(it) }
        }
        val localHistory = localDeezerSource.getLocalHistoryForQuery(query).map { list ->
            list.map { dbMapper.fromQueryHistoryEntity(it) }
        }
        return Observable.zip(liveHistory, localHistory) { liveList, localList ->
            liveList + localList
        }
    }

    override fun addSearchQueryToLocalHistory(query: String) {
        localDeezerSource.addSearchQueryToLocalHistory(dbMapper.toQueryHistoryEntity(query))
    }
}