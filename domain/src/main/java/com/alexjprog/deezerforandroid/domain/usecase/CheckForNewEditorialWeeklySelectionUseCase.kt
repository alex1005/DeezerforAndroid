package com.alexjprog.deezerforandroid.domain.usecase

import com.alexjprog.deezerforandroid.domain.model.AlbumModel
import com.alexjprog.deezerforandroid.domain.repository.MediaRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CheckForNewEditorialWeeklySelectionUseCase @Inject constructor(
    private val mediaRepository: MediaRepository
) {
    operator fun invoke() = flow {
        val cache = mediaRepository.getEditorialSelectionCache().firstOrNull()
        val newData = mediaRepository.getEditorialSelectionPage().firstOrNull()

        emit(
            if (cache == null) {
                newData ?: emptyList()
            } else {
                val newAlbums = mutableListOf<AlbumModel>()
                newData?.forEach { album ->
                    if (album.id !in cache.map { it.id }) newAlbums += album
                }
                newAlbums
            }
        )
    }
}