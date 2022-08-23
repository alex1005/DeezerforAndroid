package com.alexjprog.deezerforandroid.data.mapper

import com.alexjprog.deezerforandroid.data.storage.db.model.MediaCacheEntity
import com.alexjprog.deezerforandroid.data.storage.db.model.QueryHistoryEntity
import com.alexjprog.deezerforandroid.domain.model.AlbumModel
import com.alexjprog.deezerforandroid.domain.model.MediaItemModel
import com.alexjprog.deezerforandroid.domain.model.SearchSuggestionModel
import com.alexjprog.deezerforandroid.domain.model.TrackModel
import com.alexjprog.deezerforandroid.domain.model.params.ContentCategoryParam
import com.alexjprog.deezerforandroid.domain.model.params.MediaTypeParam
import javax.inject.Inject

class DefaultDbMapper @Inject constructor() : IDbMapper {
    override fun fromQueryHistoryEntity(queryHistoryEntity: QueryHistoryEntity): SearchSuggestionModel =
        SearchSuggestionModel(title = queryHistoryEntity.query, isInHistory = true)

    override fun toQueryHistoryEntity(query: String): QueryHistoryEntity =
        QueryHistoryEntity(query = query)

    override fun fromMediaCacheEntity(mediaCacheEntity: MediaCacheEntity): MediaItemModel =
        when (mediaCacheEntity.mediaType) {
            MediaTypeParam.ALBUM -> AlbumModel(id = mediaCacheEntity.id)
            MediaTypeParam.TRACK -> TrackModel(id = mediaCacheEntity.id)
            MediaTypeParam.NONE -> throw IllegalArgumentException()
        }

    override fun toMediaCacheEntity(
        mediaItemModel: MediaItemModel,
        category: ContentCategoryParam
    ): MediaCacheEntity =
        MediaCacheEntity(
            id = mediaItemModel.id,
            category = category,
            mediaType = when (mediaItemModel) {
                is AlbumModel -> MediaTypeParam.ALBUM
                is TrackModel -> MediaTypeParam.TRACK
            }
        )
}