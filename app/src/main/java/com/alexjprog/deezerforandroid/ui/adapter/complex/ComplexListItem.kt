package com.alexjprog.deezerforandroid.ui.adapter.complex

import com.alexjprog.deezerforandroid.domain.model.TrackModel

sealed interface ComplexListItem {
    class TitleItem(val title: String): ComplexListItem
    class HorizontalTrackListItem(val data: List<TrackModel>): ComplexListItem
}