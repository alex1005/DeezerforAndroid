package com.alexjprog.deezerforandroid.data.mapper

import com.alexjprog.deezerforandroid.data.storage.api.model.AlbumApiData
import com.alexjprog.deezerforandroid.data.storage.api.model.ArtistApiData
import com.alexjprog.deezerforandroid.data.storage.api.model.SearchHistoryResultApiData
import com.alexjprog.deezerforandroid.data.storage.api.model.TrackApiData
import com.alexjprog.deezerforandroid.domain.model.AlbumModel
import com.alexjprog.deezerforandroid.domain.model.ArtistModel
import com.alexjprog.deezerforandroid.domain.model.SearchSuggestionModel
import com.alexjprog.deezerforandroid.domain.model.TrackModel

interface IApiMapper {
    fun fromTrackApiData(track: TrackApiData): TrackModel
    fun fromArtistApiData(artist: ArtistApiData): ArtistModel
    fun fromAlbumApiData(album: AlbumApiData): AlbumModel

    fun fromSearchHistoryResultApiData(result: SearchHistoryResultApiData): SearchSuggestionModel
    fun fromSearchResultApiData(result: TrackApiData): SearchSuggestionModel
}
