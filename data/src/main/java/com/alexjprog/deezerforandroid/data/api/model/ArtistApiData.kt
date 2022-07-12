package com.alexjprog.deezerforandroid.data.api.model

import com.google.gson.annotations.SerializedName

data class ArtistApiData(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("picture")
    val picture: String
)
