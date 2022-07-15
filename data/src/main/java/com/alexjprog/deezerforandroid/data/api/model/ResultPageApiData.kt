package com.alexjprog.deezerforandroid.data.api.model

import com.google.gson.annotations.SerializedName

data class ResultPageApiData<out T>(
    @SerializedName("data")
    val data: List<T>
)