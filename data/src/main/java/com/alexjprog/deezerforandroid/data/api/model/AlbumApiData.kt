package com.alexjprog.deezerforandroid.data.api.model

import com.google.gson.annotations.SerializedName

data class AlbumApiData(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("cover")
    val cover: String
)
