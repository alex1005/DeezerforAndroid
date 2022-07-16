package com.alexjprog.deezerforandroid.data.api

import com.alexjprog.deezerforandroid.data.api.model.ResultPageApiData
import com.alexjprog.deezerforandroid.data.api.model.SearchHistoryResultApiData
import com.alexjprog.deezerforandroid.data.api.model.TrackApiData
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface DeezerService {
    @GET("chart/0/tracks")
    suspend fun getCharts(): Response<ResultPageApiData<TrackApiData>>

    @GET("search/history")
    fun getSearchHistory(@Query("q") query: String):
            Single<ResultPageApiData<SearchHistoryResultApiData>>
}
