package com.alexjprog.deezerforandroid.data.storage.api.model

import com.google.gson.annotations.SerializedName

data class TrackApiData(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("link")
    val deezerLink: String,
    @SerializedName("preview")
    val trackLink: String,
    @SerializedName("rank")
    val rank: Int,
    @SerializedName("duration")
    val duration: Int,
    @SerializedName("artist")
    val artist: ArtistApiData,
    @SerializedName("album")
    val album: AlbumApiData
)
