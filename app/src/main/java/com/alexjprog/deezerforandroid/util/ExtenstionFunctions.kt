package com.alexjprog.deezerforandroid.util

import com.alexjprog.deezerforandroid.domain.model.MediaItemModel
import com.alexjprog.deezerforandroid.model.ContentCategory
import com.alexjprog.deezerforandroid.ui.adapter.complex.ComplexListItem

fun MutableList<ComplexListItem>.addNewFeedCategory(
    category: ContentCategory,
    content: List<MediaItemModel>
) {
    if (content.isEmpty()) return
    val titleItem = ComplexListItem.TitleItem(category)
    val contentItem = ComplexListItem.HorizontalTrackListItem(content)
    addAll(listOf(titleItem, contentItem))
}