package com.alexjprog.deezerforandroid.data.storage.sharedprefs

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.alexjprog.deezerforandroid.domain.model.UserInfoModel
import java.util.*
import javax.inject.Inject

class UserStoreImpl @Inject constructor(private val context: Context) : UserStore {
    private val encryptedSharedPreferences by lazy {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        EncryptedSharedPreferences.create(
            context,
            LOGIN_FILE_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    override var userToken: String?
        get() = encryptedSharedPreferences.getString(TOKEN_KEY, null)
        set(token) {
            encryptedSharedPreferences.edit().apply {
                putString(TOKEN_KEY, token)
            }.commit()
        }

    override fun saveUserInfo(userInfo: UserInfoModel) {
        encryptedSharedPreferences.edit().apply {
            with(userInfo) {
                putLong(USER_ID_KEY, id)
                putString(USER_NAME_KEY, name)
                putString(USER_FIRSTNAME_KEY, firstname)
                putString(USER_LASTNAME_KEY, lastname)
                putString(USER_EMAIL_KEY, email)
                putLong(USER_BIRTHDAY_KEY, birthday.time)
                putString(USER_COUNTRY_KEY, country)
                putLong(USER_INSCRIPTION_KEY, inscriptionDate.time)
                putString(USER_SMALL_PICTURE_KEY, smallPictureLink)
                putString(USER_BIG_PICTURE_KEY, bigPictureLink)
                putString(USER_DEEZER_LINK_KEY, linkToDeezer)
            }
        }.apply()
    }

    override fun getUserInfo(): UserInfoModel? {
        encryptedSharedPreferences.run {
            val id = getLong(USER_ID_KEY, 0)
            val name = getString(USER_NAME_KEY, null) ?: return null
            val firstname = getString(USER_FIRSTNAME_KEY, null) ?: return null
            val lastname = getString(USER_LASTNAME_KEY, null) ?: return null
            val email = getString(USER_EMAIL_KEY, null) ?: return null
            val birthday = Date(getLong(USER_BIRTHDAY_KEY, 0))
            val country = getString(USER_COUNTRY_KEY, null) ?: return null
            val inscriptionDate = Date(getLong(USER_INSCRIPTION_KEY, 0))
            val smallPictureLink = getString(USER_SMALL_PICTURE_KEY, null) ?: return null
            val bigPictureLink = getString(USER_BIG_PICTURE_KEY, null) ?: return null
            val linkToDeezer = getString(USER_DEEZER_LINK_KEY, null) ?: return null
            return UserInfoModel(
                id = id,
                name = name,
                firstname = firstname,
                lastname = lastname,
                email = email,
                birthday = birthday,
                country = country,
                inscriptionDate = inscriptionDate,
                smallPictureLink = smallPictureLink,
                bigPictureLink = bigPictureLink,
                linkToDeezer = linkToDeezer
            )
        }
    }

    companion object {
        const val LOGIN_FILE_NAME = "secret_shared_prefs"
        const val TOKEN_KEY = "token"

        const val USER_ID_KEY = "user_id"
        const val USER_NAME_KEY = "user_name"
        const val USER_FIRSTNAME_KEY = "user_firstname"
        const val USER_LASTNAME_KEY = "user_lastname"
        const val USER_EMAIL_KEY = "user_email"
        const val USER_BIRTHDAY_KEY = "user_birthday"
        const val USER_COUNTRY_KEY = "user_country"
        const val USER_INSCRIPTION_KEY = "user_inscription"
        const val USER_SMALL_PICTURE_KEY = "user_small_picture"
        const val USER_BIG_PICTURE_KEY = "user_big_picture"
        const val USER_DEEZER_LINK_KEY = "user_deezer_link"
    }
}