package com.sopt.clody.data.datastore

import kotlinx.coroutines.flow.Flow
import tech.thdev.useful.encrypted.data.store.preferences.ksp.annotations.UsefulPreferences
import tech.thdev.useful.encrypted.data.store.preferences.ksp.annotations.value.ClearValues
import tech.thdev.useful.encrypted.data.store.preferences.ksp.annotations.value.GetValue
import tech.thdev.useful.encrypted.data.store.preferences.ksp.annotations.value.SetValue

@UsefulPreferences
interface SecureDataStore {
    @GetValue(KEY_ACCESSTOKEN)
    fun getAccessToken(): Flow<String>

    @SetValue(KEY_ACCESSTOKEN)
    suspend fun setAccessToken(string: String)

    @GetValue(KEY_REFRESHTOKEN)
    fun getRefreshToken(): Flow<String>

    @SetValue(KEY_REFRESHTOKEN)
    suspend fun setRefreshToken(string: String)

    @GetValue(KEY_AUTOLOGIN)
    fun getAutoLogin(): Flow<Boolean>

    @SetValue(KEY_AUTOLOGIN)
    suspend fun setAutoLogin(boolean: Boolean)

    @ClearValues
    suspend fun clearAll()

    companion object {
        const val KEY_ACCESSTOKEN = "key-accesstoken"
        const val KEY_REFRESHTOKEN = "key-refreshtoken"
        const val KEY_AUTOLOGIN = "key-autologin"
    }
}
