package com.sopt.clody.presentation.utils.language

import com.sopt.clody.data.datastore.OAuthProvider
import com.sopt.clody.presentation.ui.setting.screen.SettingOptionUrls
import java.util.Locale
import javax.inject.Inject

class LanguageProviderImpl @Inject constructor() : LanguageProvider {
    private val locale = Locale.getDefault()

    private fun isKorean(): Boolean = locale.language == LANGUAGE_KO

    override fun getCurrentLanguageTag(): String =
        locale.toLanguageTag() // e.g., "ko-KR" or "en-US"

    override fun getLoginType(): OAuthProvider =
        if (isKorean()) OAuthProvider.KAKAO else OAuthProvider.GOOGLE

    override fun getNicknameMaxLength(): Int =
        if (isKorean()) NICKNAME_MAX_LENGTH_KO else NICKNAME_MAX_LENGTH_EN

    override fun getDiaryMaxLength(): Int =
        if (isKorean()) DIARY_MAX_LENGTH_KO else DIARY_MAX_LENGTH_EN

    override fun getWebViewUrlFor(option: SettingOptionUrls): String =
        if (isKorean()) option.koUrl else option.enUrl

    companion object {
        const val LANGUAGE_KO = "ko"
        const val NICKNAME_MAX_LENGTH_EN = 15
        const val NICKNAME_MAX_LENGTH_KO = 10
        const val DIARY_MAX_LENGTH_EN = 100
        const val DIARY_MAX_LENGTH_KO = 50
    }
}
