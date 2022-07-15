package com.alexjprog.deezerforandroid.domain.repository

interface UserRepository {
    fun getUserAccessToken(): String?
    fun saveUserAccessToken(token: String)
}