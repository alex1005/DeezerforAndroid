package com.alexjprog.deezerforandroid.domain.model

data class TrackModel(
    override val id: Int,
    override val title: String? = null,
    val rank: Int? = null,
    val duration: Int? = null,
    val artist: ArtistModel? = null,
    val album: AlbumModel? = null,
    val deezerLink: String? = null,
    val trackLink: String? = null
) : MediaItemModel {
    override val subtitle: String?
        get() = artist?.name
    override val pictureLink: String?
        get() = album?.coverBig ?: album?.cover
}
