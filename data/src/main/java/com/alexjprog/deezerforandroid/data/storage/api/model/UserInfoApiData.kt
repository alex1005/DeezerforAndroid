package com.alexjprog.deezerforandroid.data.storage.api.model

import com.google.gson.annotations.SerializedName

data class UserInfoApiData(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("picture_small")
    val smallPictureLink: String,
    @SerializedName("picture_big")
    val bigPictureLink: String,
    @SerializedName("link")
    val linkToDeezer: String,
    @SerializedName("error")
    val error: Map<String, String>?
)