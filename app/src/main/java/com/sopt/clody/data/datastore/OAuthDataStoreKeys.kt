package com.sopt.clody.data.datastore

import androidx.datastore.preferences.core.stringPreferencesKey

object OAuthDataStoreKeys {
    val OAUTH_PLATFORM = stringPreferencesKey("oauth_platform")
    val GOOGLE_ID_TOKEN = stringPreferencesKey("google_id_token")
    val KAKAO_ID_TOKEN = stringPreferencesKey("kakao_id_token")
}
