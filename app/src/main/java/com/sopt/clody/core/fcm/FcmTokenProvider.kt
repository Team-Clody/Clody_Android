package com.sopt.clody.core.fcm

import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class FcmTokenProvider @Inject constructor() {
    suspend fun getToken(): String? {
        return try {
            Firebase.messaging.token.await()
        } catch (e: Exception) {
            Timber.e("FCM 토큰 수신 실패: ${e.message}")
            null
        }
    }
}
