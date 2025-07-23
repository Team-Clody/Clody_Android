package com.sopt.clody.presentation.ui.login

import android.content.Context
import android.util.Log
import androidx.activity.result.IntentSenderRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.sopt.clody.BuildConfig

class GoogleSignInHelper(context: Context) {

    private val signInClient: SignInClient = Identity.getSignInClient(context.applicationContext)

    fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(BuildConfig.GOOGLE_AUTH_WEB_CLIENT_ID)
                    .setFilterByAuthorizedAccounts(false)
                    .build(),
            )
            .setAutoSelectEnabled(false)
            .build()
    }

    fun requestSignIn(
        onSuccess: (IntentSenderRequest) -> Unit,
        onFailure: (Exception) -> Unit,
    ) {
        val request = buildSignInRequest()
        signInClient.beginSignIn(request)
            .addOnSuccessListener { result ->
                val intentSenderRequest =
                    IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                onSuccess(intentSenderRequest)
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    fun extractIdToken(data: android.content.Intent?): String? {
        return runCatching {
            val credential = signInClient.getSignInCredentialFromIntent(data)
            credential.googleIdToken
        }.getOrNull()
    }
}
