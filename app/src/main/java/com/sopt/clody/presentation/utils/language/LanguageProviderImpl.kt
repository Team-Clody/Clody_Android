package com.sopt.clody.presentation.utils.language

import com.sopt.clody.data.datastore.OAuthProvider
import com.sopt.clody.presentation.ui.setting.screen.SettingOptionUrls
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
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
            val serverZone = ZoneId.of(SERVER_TIMEZONE)
            val userZone = ZoneId.systemDefault()

            val startUser = LocalDateTime.parse(start).atZone(serverZone).withZoneSameInstant(userZone)
            val endUser = LocalDateTime.parse(end).atZone(serverZone).withZoneSameInstant(userZone)

            formatInspectionTime(startUser, endUser)
        }.getOrNull()
    }

    private fun formatInspectionTime(startUser: ZonedDateTime, endUser: ZonedDateTime): String {
        return if (isKorean()) {
            val koPattern = DateTimeFormatter.ofPattern(INSPECTION_TIME_FORMAT_KO, Locale.KOREAN)
            "${startUser.format(koPattern)} ~ ${endUser.format(koPattern)}"
        } else {
            val enDateFormatter = DateTimeFormatter.ofPattern(INSPECTION_DATE_FORMAT_EN, Locale.ENGLISH)
            val enTimeFormatter = DateTimeFormatter.ofPattern(INSPECTION_TIME_FORMAT_EN, Locale.ENGLISH)
            val left = "${startUser.format(enDateFormatter)}, ${startUser.format(enTimeFormatter)}"
            val right = "${endUser.format(enDateFormatter)} ${endUser.format(enTimeFormatter)}"
            "$left ~ $right"
        }
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
        private const val LANGUAGE_KO = "ko"
        private const val SERVER_TIMEZONE = "Asia/Seoul"
        private const val INSPECTION_TIME_FORMAT_KO = "M/d(E) HH시mm분"
        private const val INSPECTION_DATE_FORMAT_EN = "MMM d (EEE)"
        private const val INSPECTION_TIME_FORMAT_EN = "HH:mm"
        private const val NICKNAME_MAX_LENGTH_EN = 15
        private const val NICKNAME_MAX_LENGTH_KO = 10
        private const val DIARY_MAX_LENGTH_EN = 100
        private const val DIARY_MAX_LENGTH_KO = 50
    }
}
