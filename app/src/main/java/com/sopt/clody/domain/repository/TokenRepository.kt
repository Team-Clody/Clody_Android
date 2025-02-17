package com.sopt.clody.domain.repository

interface TokenRepository {
    fun getAccessToken(): String
    fun getRefreshToken(): String
    fun setTokens(accessToken: String, refreshToken: String)
    fun clearInfo()
}

