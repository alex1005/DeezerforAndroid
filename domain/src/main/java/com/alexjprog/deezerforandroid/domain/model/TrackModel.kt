package com.alexjprog.deezerforandroid.domain.model

data class TrackModel(
    override val id: Int,
    override val title: String,
    val rank: Int,
    val duration: Int,
    val artist: ArtistModel,
    val album: AlbumModel
) : MediaItemModel {
    override val subtitle: String
        get() = artist.name
    override val pictureLink: String?
        get() = album.coverBig ?: album.cover
}
