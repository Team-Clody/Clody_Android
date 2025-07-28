package com.sopt.clody.presentation.utils.language

import com.sopt.clody.data.datastore.OAuthProvider
import com.sopt.clody.presentation.ui.setting.screen.SettingOptionUrls

interface LanguageProvider {
    fun getLoginType(): OAuthProvider
    fun getNicknameMaxLength(): Int
    fun getDiaryMaxLength(): Int
    fun getWebViewUrlFor(option: SettingOptionUrls): String
}
