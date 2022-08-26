package com.alexjprog.deezerforandroid.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class DeezerFirebaseMessagingService : FirebaseMessagingService() {
    override fun onNewToken(token: String) {
        Log.i(TAG, "New token: $token")
    }

    override fun onMessageReceived(message: RemoteMessage) {

    }

    companion object {
        private const val TAG = "FirebaseMessagingTag"
    }
}