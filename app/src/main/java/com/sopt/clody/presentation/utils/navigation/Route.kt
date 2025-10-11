package com.sopt.clody.presentation.utils.navigation

import com.sopt.clody.domain.type.ReplyStatus
import kotlinx.serialization.Serializable

/**
 * sealed interface로 정의한 앱 내 네비게이션 Route
 * @Serializable을 활용해 arguments를 type-safe하게 전달.
 */

@Serializable
sealed interface Route {

    @Serializable
    data object Splash : Route

    @Serializable
    data object Login : Route

    @Serializable
    data object SignUp : Route

    @Serializable
    data object TimeReminder : Route

    @Serializable
    data object Guide : Route

    @Serializable
    data class Home(
        val selectedYear: Int,
        val selectedMonth: Int,
        val selectedDay: Int? = null,
        val isFromReplyDiary: Boolean = false,
    ) : Route

    @Serializable
    data class DiaryList(
        val selectedYearFromHome: Int,
        val selectedMonthFromHome: Int,
    ) : Route

    @Serializable
    data class WriteDiary(
        val year: Int,
        val month: Int,
        val date: Int,
    ) : Route

    @Serializable
    data class ReplyLoading(
        val year: Int,
        val month: Int,
        val date: Int,
        val from: ReplyLoadingFrom = ReplyLoadingFrom.HOME,
        val replyStatus: ReplyStatus = ReplyStatus.UNREADY,
    ) : Route {
        @Serializable
        enum class ReplyLoadingFrom {
            HOME,
            DIARY_LIST,
        }
    }

    @Serializable
    data class ReplyDiary(
        val year: Int,
        val month: Int,
        val date: Int,
        val replyStatus: ReplyStatus = ReplyStatus.UNREADY,
    ) : Route

    @Serializable
    data object Setting : Route

    @Serializable
    data object AccountManagement : Route

    @Serializable
    data object NotificationSetting : Route
}
