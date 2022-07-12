package com.alexjprog.deezerforandroid.data.repository

import com.alexjprog.deezerforandroid.data.api.IMediaDataSource
import com.alexjprog.deezerforandroid.data.mapper.IMediaMapper
import com.alexjprog.deezerforandroid.domain.model.TrackModel
import com.alexjprog.deezerforandroid.domain.repository.MediaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MediaRepositoryImpl(
    private val mediaSource: IMediaDataSource,
    private val mediaMapper: IMediaMapper
): MediaRepository {
    override fun getCharts(): Flow<List<TrackModel>> =
        mediaSource.getCharts()
            .map { list -> list.map { mediaMapper.mapTrack(it) } }
}