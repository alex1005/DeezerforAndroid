package com.alexjprog.deezerforandroid.domain.model

import java.util.*

data class UserInfoModel(
    val id: Long,
    val name: String,
    val firstname: String,
    val lastname: String,
    val email: String,
    val birthday: Date,
    val country: String,
    val inscriptionDate: Date,
    val smallPictureLink: String,
    val bigPictureLink: String,
    val linkToDeezer: String
) {
    val displayableFirstname: String
        get() = firstname.ifBlank { name.takeWhile { !it.isWhitespace() } }

    val displayableLastname: String
        get() = lastname.ifBlank { name.takeLastWhile { !it.isWhitespace() } }
}
