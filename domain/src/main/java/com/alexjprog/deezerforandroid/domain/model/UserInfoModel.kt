package com.alexjprog.deezerforandroid.domain.model

data class UserInfoModel(
    val id: Long,
    val name: String,
    val email: String,
    val smallPictureLink: String,
    val bigPictureLink: String,
    val linkToDeezer: String
)
