package com.sopt.clody.presentation.utils.language

import com.sopt.clody.presentation.ui.login.LoginType
import com.sopt.clody.presentation.ui.setting.screen.SettingOptionUrls

interface LanguageProvider {
    fun getLoginType(): LoginType
    fun getNicknameMaxLength(): Int
    fun getDiaryMaxLength(): Int
    fun getWebViewUrlFor(option: SettingOptionUrls): String
}
