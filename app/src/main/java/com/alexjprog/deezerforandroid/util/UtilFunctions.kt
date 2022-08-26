package com.alexjprog.deezerforandroid.util

import android.os.Bundle
import androidx.navigation.NavDirections
import com.alexjprog.deezerforandroid.domain.model.AlbumModel
import com.alexjprog.deezerforandroid.domain.model.MediaItemModel
import com.alexjprog.deezerforandroid.domain.model.TrackModel
import com.alexjprog.deezerforandroid.domain.model.params.MediaTypeParam
import com.alexjprog.deezerforandroid.model.ContentCategory
import com.alexjprog.deezerforandroid.ui.adapter.complex.ComplexListItem

fun MutableList<ComplexListItem>.addNewFeedCategoryWithMoreAction(
    category: ContentCategory,
    content: List<MediaItemModel>
) {
    if (content.isEmpty()) return
    val titleItem = ComplexListItem.TitleItemWithOpenMoreAction(category)
    val contentItem = ComplexListItem.HorizontalTrackListItem(content)
    addAll(listOf(titleItem, contentItem))
}

fun MutableList<ComplexListItem>.addNewFeedCategory(
    category: ContentCategory,
    content: List<MediaItemModel>
) {
    if (content.isEmpty()) return
    val titleItem = ComplexListItem.TitleItem(category)
    val contentItem = ComplexListItem.HorizontalTrackListItem(content)
    addAll(listOf(titleItem, contentItem))
}

fun MediaItemModel.getSafeArgPlayerNavDirection(
    playerNavDirectionBuilder: (Int, MediaTypeParam) -> NavDirections
): NavDirections =
    playerNavDirectionBuilder(
        id,
        when (this) {
            is AlbumModel -> MediaTypeParam.ALBUM
            is TrackModel -> MediaTypeParam.TRACK
        }
    )

fun Bundle.putPlayerArgs(mediaId: Int, mediaType: MediaTypeParam) = apply {
    putInt(MEDIA_ID_PLAYER_ARG_KEY, mediaId)
    putSerializable(MEDIA_TYPE_PLAYER_ARG_KEY, mediaType)
}