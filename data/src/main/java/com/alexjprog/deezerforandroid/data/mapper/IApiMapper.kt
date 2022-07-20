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
    fun mapTrack(track: TrackApiData): TrackModel
    fun mapArtist(artist: ArtistApiData): ArtistModel
    fun mapAlbum(album: AlbumApiData): AlbumModel

    fun mapSearchHistoryResult(result: SearchHistoryResultApiData): SearchSuggestionModel
    fun mapSearchResult(result: TrackApiData): SearchSuggestionModel
}
