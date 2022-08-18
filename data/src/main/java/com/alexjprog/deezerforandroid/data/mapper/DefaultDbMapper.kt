package com.alexjprog.deezerforandroid.data.mapper

import com.alexjprog.deezerforandroid.data.storage.db.model.QueryHistoryEntity
import com.alexjprog.deezerforandroid.domain.model.SearchSuggestionModel
import javax.inject.Inject

class DefaultDbMapper @Inject constructor() : IDbMapper {
    override fun mapQueryHistoryEntityToSuggestion(queryHistoryEntity: QueryHistoryEntity): SearchSuggestionModel =
        SearchSuggestionModel(title = queryHistoryEntity.query, isInHistory = true)

    override fun mapStringQueryToEntity(query: String): QueryHistoryEntity =
        QueryHistoryEntity(query = query)
}