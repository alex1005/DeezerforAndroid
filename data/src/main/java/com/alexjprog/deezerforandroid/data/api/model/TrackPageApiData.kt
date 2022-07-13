package com.alexjprog.deezerforandroid.data.api.model

import com.google.gson.annotations.SerializedName

data class TrackPageApiData(
    @SerializedName("data")
    val data: List<TrackApiData>
)