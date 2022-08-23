package com.alexjprog.deezerforandroid.data.storage.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alexjprog.deezerforandroid.domain.model.params.ContentCategoryParam
import com.alexjprog.deezerforandroid.domain.model.params.MediaTypeParam

@Entity(tableName = "media_cache")
data class MediaCacheEntity(
    @PrimaryKey
    @ColumnInfo(name = "_id")
    val id: Int,
    @ColumnInfo(name = "category")
    val category: ContentCategoryParam,
    @ColumnInfo(name = "media_type")
    val mediaType: MediaTypeParam
)