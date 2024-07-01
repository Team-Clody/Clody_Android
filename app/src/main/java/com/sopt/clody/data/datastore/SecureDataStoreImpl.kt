package com.sopt.clody.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SecureDataStoreImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : SecureDataStore {

    companion object {
        private val KEY_ACCESSTOKEN = stringPreferencesKey("key-accesstoken")
        private val KEY_REFRESHTOKEN = stringPreferencesKey("key-refreshtoken")
        private val KEY_AUTOLOGIN = booleanPreferencesKey("key-autologin")
    }

    override fun getAccessToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[KEY_ACCESSTOKEN] ?: ""
        }
    }

    override suspend fun setAccessToken(string: String) {
        dataStore.edit { preferences ->
            preferences[KEY_ACCESSTOKEN] = string
        }
    }

    override fun getRefreshToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[KEY_REFRESHTOKEN] ?: ""
        }
    }

    override suspend fun setRefreshToken(string: String) {
        dataStore.edit { preferences ->
            preferences[KEY_REFRESHTOKEN] = string
        }
    }

    override fun getAutoLogin(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[KEY_AUTOLOGIN] ?: false
        }
    }

    override suspend fun setAutoLogin(boolean: Boolean) {
        dataStore.edit { preferences ->
            preferences[KEY_AUTOLOGIN] = boolean
        }
    }

    override suspend fun clearAll() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
