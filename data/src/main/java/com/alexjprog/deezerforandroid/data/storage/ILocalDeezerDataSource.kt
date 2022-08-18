package com.alexjprog.deezerforandroid.data.storage

import com.alexjprog.deezerforandroid.data.storage.db.model.QueryHistoryEntity
import io.reactivex.rxjava3.core.Observable

interface ILocalDeezerDataSource {
    fun getLocalHistoryForQuery(query: String): Observable<List<QueryHistoryEntity>>
    fun addSearchQueryToLocalHistory(query: QueryHistoryEntity)
}