package com.alexjprog.deezerforandroid.model

data class UserInfoDisplayable(
    val firstname: String,
    val lastname: String,
    val birthday: String?,
    val inscriptionDate: String,
    val email: String,
    val country: String,
    val bigPictureLink: String,
    val linkToDeezer: String
)