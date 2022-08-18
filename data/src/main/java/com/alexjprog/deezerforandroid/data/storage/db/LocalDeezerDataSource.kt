package com.alexjprog.deezerforandroid.data.storage.db

import com.alexjprog.deezerforandroid.data.storage.ILocalDeezerDataSource
import com.alexjprog.deezerforandroid.data.storage.db.model.QueryHistoryEntity
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class LocalDeezerDataSource @Inject constructor(
    private val queryHistoryDao: QueryHistoryDao
) : ILocalDeezerDataSource {
    override fun getLocalHistoryForQuery(query: String): Observable<List<QueryHistoryEntity>> =
        queryHistoryDao.getHistoryForQuery(query).toObservable()

    override fun addSearchQueryToLocalHistory(query: QueryHistoryEntity) {
        queryHistoryDao.addQueryToHistory(query).subscribeOn(Schedulers.io()).subscribe()
    }
}