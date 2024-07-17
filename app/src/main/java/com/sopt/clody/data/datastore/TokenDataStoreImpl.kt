package com.sopt.clody.data.datastore

import android.content.SharedPreferences
import javax.inject.Inject

class TokenDataStoreImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : TokenDataStore {
    override var accessToken: String
        get() = sharedPreferences.getString(ACCESS_TOKEN, "") ?: ""
        set(value) = sharedPreferences.edit().putString(ACCESS_TOKEN, value).apply()

    override var refreshToken: String
        get() = sharedPreferences.getString(REFRESH_TOKEN, "") ?: ""
        set(value) = sharedPreferences.edit().putString(REFRESH_TOKEN, value).apply()

    override fun clearInfo() {
        sharedPreferences.edit().clear().apply()
    }

    companion object {
        private const val ACCESS_TOKEN = "ACCESS_TOKEN"
        private const val REFRESH_TOKEN = "REFRESH_TOKEN"
    }
}
