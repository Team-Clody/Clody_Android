package com.sopt.clody.data.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.sopt.clody.presentation.ui.login.OAuthProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class OAuthDataStore @Inject constructor(@ApplicationContext context: Context) {
    private val Context.dataStore by preferencesDataStore(name = "oauth_pref")
    private val dataStore = context.dataStore

    suspend fun saveIdToken(token: String) {
        dataStore.edit { it[OAuthDataStoreKeys.GOOGLE_ID_TOKEN] = token }
    }

    suspend fun getIdToken(): String? {
        return dataStore.data.first()[OAuthDataStoreKeys.GOOGLE_ID_TOKEN]
    }

    suspend fun savePlatform(provider: OAuthProvider) {
        dataStore.edit { it[OAuthDataStoreKeys.OAUTH_PLATFORM] = provider.name }
    }

    suspend fun getPlatform(): OAuthProvider? {
        return dataStore.data.first()[OAuthDataStoreKeys.OAUTH_PLATFORM]?.let {
            runCatching { OAuthProvider.valueOf(it) }.getOrNull()
        }
    }

    suspend fun clear() {
        dataStore.edit {
            it.remove(OAuthDataStoreKeys.GOOGLE_ID_TOKEN)
            it.remove(OAuthDataStoreKeys.OAUTH_PLATFORM)
        }
    }
}
