package com.alexjprog.deezerforandroid.data.storage.sharedprefs

import com.alexjprog.deezerforandroid.domain.model.UserInfoModel

interface UserStore {
    var userToken: String?

    fun saveUserInfo(userInfo: UserInfoModel)
    fun getUserInfo(): UserInfoModel?
}