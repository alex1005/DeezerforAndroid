package com.alexjprog.deezerforandroid.data.repository

import com.alexjprog.deezerforandroid.data.storage.IDeezerDataSource
import com.alexjprog.deezerforandroid.data.mapper.IApiMapper
import com.alexjprog.deezerforandroid.domain.model.TrackModel
import com.alexjprog.deezerforandroid.domain.repository.MediaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MediaRepositoryImpl @Inject constructor(
    private val mediaSource: IDeezerDataSource,
    private val mediaMapper: IApiMapper
): MediaRepository {
    override fun getCharts(): Flow<List<TrackModel>> =
        mediaSource.getCharts()
            .map { list -> list.map { mediaMapper.mapTrack(it) } }
}