package com.sopt.clody.data.datastore

import androidx.datastore.preferences.core.stringPreferencesKey

object OAuthDataStoreKeys {
    val GOOGLE_ID_TOKEN = stringPreferencesKey("google_id_token")
    val OAUTH_PLATFORM = stringPreferencesKey("oauth_platform")
}
