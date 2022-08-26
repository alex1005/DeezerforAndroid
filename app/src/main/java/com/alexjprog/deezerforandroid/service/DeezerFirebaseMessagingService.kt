package com.alexjprog.deezerforandroid.service

import android.util.Log
import com.alexjprog.deezerforandroid.domain.model.params.MediaTypeParam
import com.alexjprog.deezerforandroid.util.FIREBASE_MEDIA_ID_KEY
import com.alexjprog.deezerforandroid.util.FIREBASE_MEDIA_TYPE_KEY
import com.alexjprog.deezerforandroid.util.FirebasePushNotificationHelper
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class DeezerFirebaseMessagingService : FirebaseMessagingService() {
    private val firebasePushNotificationHelper by lazy {
        FirebasePushNotificationHelper(this)
    }

    override fun onNewToken(token: String) {
        Log.i(TAG, "New token: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val title = message.notification?.title ?: return
        val textContent = message.notification?.body ?: return
        val mediaId = extractMediaIdArg(message.data[FIREBASE_MEDIA_ID_KEY])
        val mediaType = extractMediaTypeArg(message.data[FIREBASE_MEDIA_TYPE_KEY])

        firebasePushNotificationHelper.sendFirebaseNotification(
            title,
            textContent,
            mediaId,
            mediaType
        )
    }

    companion object {
        private const val TAG = "FirebaseMessagingTag"

        fun extractMediaIdArg(rawMediaId: String?): Int? = rawMediaId?.toIntOrNull()
        fun extractMediaTypeArg(rawMediaType: String?): MediaTypeParam =
            try {
                enumValueOf(rawMediaType ?: MediaTypeParam.NONE.toString())
            } catch (e: IllegalArgumentException) {
                MediaTypeParam.NONE
            }
    }
}