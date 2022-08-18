package com.alexjprog.deezerforandroid.data.storage.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "query_history")
data class QueryHistoryEntity(
    @PrimaryKey
    @ColumnInfo(name = "query")
    val query: String
)