package com.alexjprog.deezerforandroid.data.storage.sharedprefs

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import javax.inject.Inject

class LoginStoreImpl @Inject constructor(private val context: Context): LoginStore {
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

    companion object {
        const val LOGIN_FILE_NAME = "secret_shared_prefs"
        const val TOKEN_KEY = "token"
    }
}