package com.alexjprog.deezerforandroid.data.storage.db

import com.alexjprog.deezerforandroid.data.storage.ILocalDeezerDataSource
import com.alexjprog.deezerforandroid.data.storage.db.model.MediaCacheEntity
import com.alexjprog.deezerforandroid.data.storage.db.model.QueryHistoryEntity
import com.alexjprog.deezerforandroid.domain.model.params.ContentCategoryParam
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import javax.inject.Inject

class LocalDeezerDataSource @Inject constructor(
    private val queryHistoryDao: QueryHistoryDao,
    private val mediaCacheDao: MediaCacheDao
) : ILocalDeezerDataSource {
    override fun getLocalHistoryForQuery(query: String): Observable<List<QueryHistoryEntity>> =
        queryHistoryDao.getHistoryForQuery(query).toObservable()

    override fun addSearchQueryToLocalHistory(query: QueryHistoryEntity) {
        queryHistoryDao.addQueryToHistory(query).subscribeOn(Schedulers.io()).subscribe()
    }

    override fun getEditorialSelectionCache() =
        mediaCacheDao.getCacheForCategory(ContentCategoryParam.EDIT_SELECTION)

    override suspend fun rewriteEditorialSelectionCache(cache: List<MediaCacheEntity>) =
        mediaCacheDao.rewriteCache(cache)
}