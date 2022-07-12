package com.alexjprog.deezerforandroid.domain.model

data class Track(
    val id: Int,
    val title: String,
    val deezerLink: String,
    val trackLink: String,
    val rank: Int,
    val duration: Int,
    val artist: Artist,
    val album: Album
)
