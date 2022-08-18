package com.alexjprog.deezerforandroid.data.mapper

import com.alexjprog.deezerforandroid.data.storage.db.model.QueryHistoryEntity
import com.alexjprog.deezerforandroid.domain.model.SearchSuggestionModel

interface IDbMapper {
    fun mapQueryHistoryEntityToSuggestion(queryHistoryEntity: QueryHistoryEntity): SearchSuggestionModel
    fun mapStringQueryToEntity(query: String): QueryHistoryEntity
}
