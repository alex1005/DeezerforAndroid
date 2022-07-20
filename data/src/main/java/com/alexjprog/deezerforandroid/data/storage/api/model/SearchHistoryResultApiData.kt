package com.alexjprog.deezerforandroid.data.storage.api.model

import com.google.gson.annotations.SerializedName

data class SearchHistoryResultApiData(
    @SerializedName("query")
    val title: String
)