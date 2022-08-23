package com.alexjprog.deezerforandroid.data.mapper

import com.alexjprog.deezerforandroid.data.storage.db.model.MediaCacheEntity
import com.alexjprog.deezerforandroid.data.storage.db.model.QueryHistoryEntity
import com.alexjprog.deezerforandroid.domain.model.MediaItemModel
import com.alexjprog.deezerforandroid.domain.model.SearchSuggestionModel
import com.alexjprog.deezerforandroid.domain.model.params.ContentCategoryParam

interface IDbMapper {
    fun fromQueryHistoryEntity(queryHistoryEntity: QueryHistoryEntity): SearchSuggestionModel
    fun toQueryHistoryEntity(query: String): QueryHistoryEntity

    fun fromMediaCacheEntity(mediaCacheEntity: MediaCacheEntity): MediaItemModel
    fun toMediaCacheEntity(
        mediaItemModel: MediaItemModel,
        category: ContentCategoryParam
    ): MediaCacheEntity
}
