package com.alexjprog.deezerforandroid.data.storage.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alexjprog.deezerforandroid.data.storage.db.model.QueryHistoryEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

@Dao
interface QueryHistoryDao {
    @Query("SELECT * FROM query_history WHERE query LIKE '%' || :query || '%'")
    fun getHistoryForQuery(query: String): Single<List<QueryHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addQueryToHistory(entity: QueryHistoryEntity): Completable
}