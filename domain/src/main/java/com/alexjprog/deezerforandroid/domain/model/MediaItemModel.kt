package com.alexjprog.deezerforandroid.domain.model

sealed interface MediaItemModel {
    val id: Int
    val title: String
    val subtitle: String?
    val pictureLink: String?
}