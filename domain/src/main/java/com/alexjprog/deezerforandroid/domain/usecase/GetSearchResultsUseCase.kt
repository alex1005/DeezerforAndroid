package com.alexjprog.deezerforandroid.domain.usecase

import androidx.paging.PagingData
import com.alexjprog.deezerforandroid.domain.model.MediaItemModel
import com.alexjprog.deezerforandroid.domain.repository.MediaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSearchResultsUseCase @Inject constructor(private val mediaRepository: MediaRepository) {
    operator fun invoke(
        pageSize: Int,
        query: String
    ): Flow<PagingData<MediaItemModel>> = mediaRepository.getSearchResultsForQuery(pageSize, query)
}
