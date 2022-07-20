package com.alexjprog.deezerforandroid.data.storage.api

import com.alexjprog.deezerforandroid.data.storage.api.model.ResultPageApiData
import com.alexjprog.deezerforandroid.data.storage.api.model.SearchHistoryResultApiData
import com.alexjprog.deezerforandroid.data.storage.api.model.TrackApiData
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface DeezerService {
    @GET("chart/0/tracks")
    suspend fun getCharts(@Query("index") pageIndex: Int, @Query("limit") itemAmount: Int):
            Response<ResultPageApiData<TrackApiData>>

    @GET("search/history")
    fun getSearchHistory():
            Single<ResultPageApiData<SearchHistoryResultApiData>>

    @GET("search")
    fun getSearchResultsForQuery(@Query("q") query: String):
            Single<ResultPageApiData<TrackApiData>>
}
