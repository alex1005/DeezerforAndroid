package com.alexjprog.deezerforandroid.data.storage.db

import androidx.room.*
import com.alexjprog.deezerforandroid.data.storage.db.model.MediaCacheEntity
import com.alexjprog.deezerforandroid.domain.model.params.ContentCategoryParam
import kotlinx.coroutines.flow.Flow

@Dao
interface MediaCacheDao {
    @Query("SELECT * FROM media_cache WHERE category = :category")
    fun getCacheForCategory(category: ContentCategoryParam): Flow<List<MediaCacheEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCache(cache: List<MediaCacheEntity>)

    @Query("DELETE FROM media_cache WHERE category = :category")
    suspend fun deleteCacheForCategory(category: ContentCategoryParam)

    @Transaction
    suspend fun rewriteCache(cache: List<MediaCacheEntity>) {
        val category = cache.firstOrNull()?.category ?: return
        deleteCacheForCategory(category)
        insertCache(cache)
    }
}