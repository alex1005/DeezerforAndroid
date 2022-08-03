package com.alexjprog.deezerforandroid.domain.model

data class AlbumModel(
    override val id: Int,
    override val title: String? = null,
    val artist: ArtistModel? = null,
    val cover: String? = null,
    val coverBig: String? = null,
    val trackList: List<TrackModel>? = null
) : MediaItemModel {
    override val pictureLink: String?
        get() = coverBig ?: cover
    override val subtitle: String?
        get() = artist?.name
}
