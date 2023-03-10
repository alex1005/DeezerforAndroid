package com.alexjprog.deezerforandroid.data.storage.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.alexjprog.deezerforandroid.data.storage.db.model.MediaCacheEntity
import com.alexjprog.deezerforandroid.data.storage.db.model.QueryHistoryEntity

@Database(
    entities = [QueryHistoryEntity::class, MediaCacheEntity::class],
    version = 1,
    exportSchema = false
)
abstract class DeezerLocalDb : RoomDatabase() {
    abstract fun getQueryHistoryDao(): QueryHistoryDao
    abstract fun getMediaCacheDao(): MediaCacheDao
}