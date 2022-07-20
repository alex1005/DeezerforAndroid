package com.alexjprog.deezerforandroid.domain.usecase

import androidx.paging.PagingData
import com.alexjprog.deezerforandroid.domain.model.TrackModel
import com.alexjprog.deezerforandroid.domain.model.params.ContentCategoryParam
import com.alexjprog.deezerforandroid.domain.repository.MediaRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMoreContentUseCase @Inject constructor(private val mediaRepository: MediaRepository) {
    operator fun invoke(
        pageSize: Int,
        category: ContentCategoryParam
    ): Flow<PagingData<TrackModel>> = mediaRepository.getCategoryContent(pageSize, category)
}