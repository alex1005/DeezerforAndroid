package com.alexjprog.deezerforandroid.data.api

import com.alexjprog.deezerforandroid.data.api.model.TrackApiData
import retrofit2.Response
import retrofit2.http.GET

interface MediaService {
    @GET("chart/0/tracks")
    fun getCharts(): Response<List<TrackApiData>>
}
