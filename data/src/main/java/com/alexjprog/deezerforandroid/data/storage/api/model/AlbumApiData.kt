package com.alexjprog.deezerforandroid.data.storage.api.model

import com.google.gson.annotations.SerializedName

data class AlbumApiData(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("artist")
    val artist: ArtistApiData?,
    @SerializedName("cover")
    val cover: String?,
    @SerializedName("cover_big")
    val coverBig: String?,
    @SerializedName("tracks")
    val trackList: ResultPageApiData<TrackApiData>?,
    @SerializedName("error")
    val error: Map<String, String>?
)
