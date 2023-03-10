package com.alexjprog.deezerforandroid.data.storage.api

import com.alexjprog.deezerforandroid.data.storage.api.model.*
import io.reactivex.rxjava3.core.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface DeezerService {
    @GET("chart/0/tracks")
    suspend fun getCharts(@Query("index") pageIndex: Int, @Query("limit") itemAmount: Int):
            Response<ResultPageApiData<TrackApiData>>

    @GET("user/me/flow")
    suspend fun getFlow(@Query("index") pageIndex: Int, @Query("limit") itemAmount: Int):
            Response<ResultPageApiData<TrackApiData>>

    @GET("user/me/recommendations/tracks")
    suspend fun getRecommendations(@Query("index") pageIndex: Int, @Query("limit") itemAmount: Int):
            Response<ResultPageApiData<TrackApiData>>

    @GET("editorial/0/selection")
    suspend fun getEditorialSelection():
            Response<ResultPageApiData<AlbumApiData>>

    @GET("editorial/0/releases")
    suspend fun getEditorialReleases():
            Response<ResultPageApiData<AlbumApiData>>

    @GET("search/history")
    fun getSearchHistory():
            Single<ResultPageApiData<SearchHistoryResultApiData>>

    @GET("search")
    fun getSearchSuggestionsForQuery(@Query("q") query: String):
            Single<ResultPageApiData<TrackApiData>>

    @GET("search")
    suspend fun getSearchResultsForQuery(
        @Query("q") query: String,
        @Query("index") pageIndex: Int,
        @Query("limit") itemAmount: Int
    ): Response<ResultPageApiData<TrackApiData>>

    @GET("track/{id}")
    fun getTrackInfo(@Path("id") id: Int): Single<TrackApiData>

    @GET("album/{id}")
    fun getAlbumInfo(@Path("id") id: Int): Single<AlbumApiData>

    @GET("user/me")
    fun getUserInfo(): Single<UserInfoApiData>

    @GET("user/me")
    fun getUserInfoWithToken(@Query("access_token") token: String): Single<UserInfoApiData>
}
