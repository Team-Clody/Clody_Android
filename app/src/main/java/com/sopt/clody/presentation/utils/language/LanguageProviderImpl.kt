package com.sopt.clody.presentation.utils.language

import android.content.Context
import com.sopt.clody.data.datastore.OAuthProvider
import com.sopt.clody.presentation.ui.setting.screen.SettingOptionUrls
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject

class LanguageProviderImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : LanguageProvider {
    private val locale: Locale
        get() = context.resources.configuration.locales[0]

    private fun isKorean(): Boolean = locale.language == LANGUAGE_KO

    override fun getLoginType(): OAuthProvider {
        return when (locale.language) {
            LANGUAGE_KO -> OAuthProvider.KAKAO
            else -> OAuthProvider.GOOGLE
        }
    }

    override fun getNicknameMaxLength(): Int {
        return if (isKorean()) NICKNAME_MAX_LENGTH_KO else NICKNAME_MAX_LENGTH_EN
    }

    override fun getDiaryMaxLength(): Int {
        return if (isKorean()) DIARY_MAX_LENGTH_KO else DIARY_MAX_LENGTH_EN
    }

    override fun getWebViewUrlFor(option: SettingOptionUrls): String {
        return if (isKorean()) option.koUrl else option.enUrl
    }

    companion object {
        const val LANGUAGE_KO = "ko"
        const val NICKNAME_MAX_LENGTH_EN = 15
        const val NICKNAME_MAX_LENGTH_KO = 10
        const val DIARY_MAX_LENGTH_EN = 100
        const val DIARY_MAX_LENGTH_KO = 50
    }
}
