package com.alexjprog.deezerforandroid.data.mapper

import com.alexjprog.deezerforandroid.data.storage.api.model.*
import com.alexjprog.deezerforandroid.domain.model.*

interface IApiMapper {
    fun fromTrackApiData(track: TrackApiData): TrackModel
    fun fromArtistApiData(artist: ArtistApiData): ArtistModel
    fun fromAlbumApiData(album: AlbumApiData): AlbumModel

    fun fromSearchHistoryResultApiData(result: SearchHistoryResultApiData): SearchSuggestionModel
    fun fromSearchResultApiData(result: TrackApiData): SearchSuggestionModel

    fun fromUserInfoApiData(userInfo: UserInfoApiData): UserInfoModel
}
