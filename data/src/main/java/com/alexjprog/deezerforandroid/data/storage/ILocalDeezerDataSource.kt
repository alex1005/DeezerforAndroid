package com.alexjprog.deezerforandroid.data.storage

import com.alexjprog.deezerforandroid.data.storage.db.model.MediaCacheEntity
import com.alexjprog.deezerforandroid.data.storage.db.model.QueryHistoryEntity
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow

interface ILocalDeezerDataSource {
    fun getLocalHistoryForQuery(query: String): Observable<List<QueryHistoryEntity>>
    fun addSearchQueryToLocalHistory(query: QueryHistoryEntity)

    fun getEditorialSelectionCache(): Flow<List<MediaCacheEntity>>
    suspend fun rewriteEditorialSelectionCache(cache: List<MediaCacheEntity>)
}