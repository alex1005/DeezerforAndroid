package com.alexjprog.deezerforandroid.domain.util

data class Optional<T>(val value: T?)

fun <T> T?.asOptional() = Optional(this)