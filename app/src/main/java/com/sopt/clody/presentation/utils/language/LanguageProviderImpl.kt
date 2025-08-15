package com.sopt.clody.presentation.utils.language

import com.sopt.clody.data.datastore.OAuthProvider
import com.sopt.clody.presentation.ui.setting.screen.SettingOptionUrls
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

class LanguageProviderImpl @Inject constructor() : LanguageProvider {
    private val locale = Locale.getDefault()

    private fun isKorean(): Boolean = locale.language == LANGUAGE_KO

    override fun getCurrentLanguageTag(): String =
        locale.toLanguageTag() // e.g., "ko-KR" or "en-US"

    override fun getInspectionTimeText(start: String, end: String): String? {
        return runCatching {
            val startLdt = LocalDateTime.parse(start, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            val endLdt = LocalDateTime.parse(end, DateTimeFormatter.ISO_LOCAL_DATE_TIME)

            val serverZone = ZoneId.of("Asia/Seoul")
            val userZone = ZoneId.systemDefault()

            val startUser = startLdt.atZone(serverZone).withZoneSameInstant(userZone)
            val endUser = endLdt.atZone(serverZone).withZoneSameInstant(userZone)

            if (isKorean()) {
                val koPattern = DateTimeFormatter.ofPattern("M/d(E) HH시mm분", Locale.KOREAN)
                "${startUser.format(koPattern)} ~ ${endUser.format(koPattern)}"
            } else {
                val enDatePattern = DateTimeFormatter.ofPattern("MMM d (EEE)", Locale.ENGLISH)
                val enTimePattern = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH)
                val left = "${startUser.format(enDatePattern)}, ${startUser.format(enTimePattern)}"
                val right = "${endUser.format(enDatePattern)} ${endUser.format(enTimePattern)}"
                "$left ~ $right"
            }
        }.getOrNull()
    }

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
