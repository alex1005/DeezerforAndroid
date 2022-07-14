package com.alexjprog.deezerforandroid.ui.adapter.complex

import com.alexjprog.deezerforandroid.domain.model.TrackModel
import com.alexjprog.deezerforandroid.ui.MoreContentFragment

sealed interface ComplexListItem {
    data class TitleItem(val category: MoreContentFragment.ContentCategory) : ComplexListItem
    data class HorizontalTrackListItem(val data: List<TrackModel>) : ComplexListItem
}