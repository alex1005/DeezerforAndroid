package com.alexjprog.deezerforandroid.data.storage.api.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class UserInfoApiData(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("firstname")
    val firstname: String,
    @SerializedName("lastname")
    val lastname: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("birthday")
    val birthday: Date,
    @SerializedName("country")
    val country: String,
    @SerializedName("inscription_date")
    val inscriptionDate: Date,
    @SerializedName("picture_small")
    val smallPictureLink: String,
    @SerializedName("picture_big")
    val bigPictureLink: String,
    @SerializedName("link")
    val linkToDeezer: String,
    @SerializedName("error")
    val error: Map<String, String>?
)