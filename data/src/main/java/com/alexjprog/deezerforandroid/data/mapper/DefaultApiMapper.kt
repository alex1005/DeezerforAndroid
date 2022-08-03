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
    override fun mapTrack(track: TrackApiData): TrackModel =
        TrackModel(
            id = track.id,
            title = track.title,
            rank = track.rank,
            duration = track.duration,
            artist = mapArtist(track.artist),
            album = mapAlbum(track.album),
            deezerLink = track.deezerLink,
            trackLink = track.trackLink
        )

    override fun mapArtist(artist: ArtistApiData): ArtistModel =
        ArtistModel(id = artist.id, name = artist.name, picture = artist.picture)

    override fun mapAlbum(album: AlbumApiData): AlbumModel =
        AlbumModel(
            id = album.id,
            title = album.title,
            artist = album.artist?.let { mapArtist(it) },
            cover = album.cover,
            coverBig = album.coverBig,
            trackList = album.trackList?.map { mapTrack(it) }
        )

    override fun mapSearchHistoryResult(result: SearchHistoryResultApiData): SearchSuggestionModel =
        SearchSuggestionModel(title = result.title, isInHistory = true)

    override fun mapSearchResult(result: TrackApiData): SearchSuggestionModel =
        SearchSuggestionModel(title = result.title, isInHistory = false)
}