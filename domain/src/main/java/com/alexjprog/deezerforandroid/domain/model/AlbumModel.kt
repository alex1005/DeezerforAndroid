package com.alexjprog.deezerforandroid.domain.model

data class AlbumModel(
    override val id: Int,
    override val title: String,
    val artist: ArtistModel?,
    val cover: String?,
    val coverBig: String?,
    val trackList: List<TrackModel>?
) : MediaItemModel {
    override val pictureLink: String?
        get() = coverBig ?: cover
    override val subtitle: String?
        get() = artist?.name
}
