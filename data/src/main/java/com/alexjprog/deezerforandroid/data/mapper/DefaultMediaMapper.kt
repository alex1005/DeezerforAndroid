package com.alexjprog.deezerforandroid.data.mapper

import com.alexjprog.deezerforandroid.data.api.model.AlbumApiData
import com.alexjprog.deezerforandroid.data.api.model.ArtistApiData
import com.alexjprog.deezerforandroid.data.api.model.TrackApiData
import com.alexjprog.deezerforandroid.domain.model.AlbumModel
import com.alexjprog.deezerforandroid.domain.model.ArtistModel
import com.alexjprog.deezerforandroid.domain.model.TrackModel
import javax.inject.Inject

class DefaultMediaMapper @Inject constructor() : IMediaMapper {
    override fun mapTrack(track: TrackApiData): TrackModel =
        TrackModel(
            id = track.id,
            title = track.title,
            deezerLink = track.deezerLink,
            trackLink = track.trackLink,
            rank = track.rank,
            duration = track.duration,
            artist = mapArtist(track.artist),
            album = mapAlbum(track.album)
        )

    override fun mapArtist(artist: ArtistApiData): ArtistModel =
        ArtistModel(id = artist.id, name = artist.name, picture = artist.picture)

    override fun mapAlbum(album: AlbumApiData): AlbumModel =
        AlbumModel(id = album.id, title = album.title, cover = album.cover)
}