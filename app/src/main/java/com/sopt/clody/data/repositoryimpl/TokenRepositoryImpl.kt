package com.sopt.clody.data.repositoryimpl

import android.util.Log
import com.sopt.clody.data.datastore.TokenDataStore
import com.sopt.clody.data.repository.TokenRepository
import javax.inject.Inject

class TokenRepositoryImpl @Inject constructor(
    private val tokenDataStore: TokenDataStore
) : TokenRepository {
    override fun getAccessToken(): String = tokenDataStore.accessToken
    override fun getRefreshToken(): String = tokenDataStore.refreshToken

    override fun setTokens(accessToken: String, refreshToken: String) {
        tokenDataStore.accessToken = accessToken
        tokenDataStore.refreshToken = refreshToken
        Log.d("TokenRepository", "Access Token Saved: $accessToken")
        Log.d("TokenRepository", "Refresh Token Saved: $refreshToken")
    }

    override fun clearInfo() {
        tokenDataStore.clearInfo()
    }
}
