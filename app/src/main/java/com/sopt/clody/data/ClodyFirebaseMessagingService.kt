package com.sopt.clody.data

import android.content.Context
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class ClodyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        saveTokenToPreferences(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        // TODO: 메시지 처리 로직
    }

    private fun saveTokenToPreferences(token: String) {
        val sharedPreferences = getSharedPreferences("fcm_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("fcm_token", token)
        editor.apply()
    }

    companion object {
        fun getTokenFromPreferences(context: Context): String? {
            val sharedPreferences = context.getSharedPreferences("fcm_prefs", Context.MODE_PRIVATE)
            return sharedPreferences.getString("fcm_token", null)
        }
    }
}
