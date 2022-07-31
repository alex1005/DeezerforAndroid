package com.alexjprog.deezerforandroid.ui.adapter.complex

import com.alexjprog.deezerforandroid.domain.model.MediaItemModel
import com.alexjprog.deezerforandroid.model.ContentCategory

sealed interface ComplexListItem {
    data class TitleItemWithOpenMoreAction(val category: ContentCategory) : ComplexListItem
    data class TitleItem(val category: ContentCategory) : ComplexListItem
    data class HorizontalTrackListItem(val data: List<MediaItemModel>) : ComplexListItem
}