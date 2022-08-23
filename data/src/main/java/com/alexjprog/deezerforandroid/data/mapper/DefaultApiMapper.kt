package com.alexjprog.deezerforandroid.data.mapper

import com.alexjprog.deezerforandroid.data.storage.api.model.AlbumApiData
import com.alexjprog.deezerforandroid.data.storage.api.model.ArtistApiData
import com.alexjprog.deezerforandroid.data.storage.api.model.SearchHistoryResultApiData
import com.alexjprog.deezerforandroid.data.storage.api.model.TrackApiData
import com.alexjprog.deezerforandroid.domain.model.AlbumModel
import com.alexjprog.deezerforandroid.domain.model.ArtistModel
import com.alexjprog.deezerforandroid.domain.model.SearchSuggestionModel
import com.alexjprog.deezerforandroid.domain.model.TrackModel
import javax.inject.Inject

class DefaultApiMapper @Inject constructor() : IApiMapper {
    override fun fromTrackApiData(track: TrackApiData): TrackModel =
        TrackModel(
            id = track.id,
            title = track.title,
            rank = track.rank,
            duration = track.duration,
            artist = fromArtistApiData(track.artist),
            album = fromAlbumApiData(track.album),
            deezerLink = track.deezerLink,
            trackLink = track.trackLink
        )

    override fun fromArtistApiData(artist: ArtistApiData): ArtistModel =
        ArtistModel(id = artist.id, name = artist.name, picture = artist.picture)

    override fun fromAlbumApiData(album: AlbumApiData): AlbumModel =
        AlbumModel(
            id = album.id,
            title = album.title,
            artist = album.artist?.let { fromArtistApiData(it) },
            cover = album.cover,
            coverBig = album.coverBig,
            trackList = album.trackList?.data?.map { fromTrackApiData(it) }
        )

    override fun fromSearchHistoryResultApiData(result: SearchHistoryResultApiData): SearchSuggestionModel =
        SearchSuggestionModel(title = result.title, isInHistory = true)

    override fun fromSearchResultApiData(result: TrackApiData): SearchSuggestionModel =
        SearchSuggestionModel(title = result.title, isInHistory = false)
}