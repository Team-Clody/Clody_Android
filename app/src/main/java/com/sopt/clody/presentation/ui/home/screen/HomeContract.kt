package com.sopt.clody.presentation.ui.home.screen

import com.airbnb.mvrx.MavericksState
import com.sopt.clody.domain.model.CalendarMonthlyInfo
import com.sopt.clody.domain.model.DailyDiaryInfo
import com.sopt.clody.domain.type.ReplyStatus
import com.sopt.clody.presentation.utils.base.UiLoadState
import java.time.LocalDate

class HomeContract {
    data class HomeState(
        val year: Int = LocalDate.now().year,
        val month: Int = LocalDate.now().monthValue,
        val dayOfMonth: Int = LocalDate.now().dayOfMonth,
        val calendarLoadState: UiLoadState = UiLoadState.Idle,
        val calendarMonthlyInfo: CalendarMonthlyInfo = CalendarMonthlyInfo(),
        val dailyDiaryLoadState: UiLoadState = UiLoadState.Idle,
        val dailyDiaryInfo: DailyDiaryInfo = DailyDiaryInfo(),
        val showYearMonthPicker: Boolean = false,
        val showDiaryDeleteBottomSheet: Boolean = false,
        val showDiaryDeleteDialog: Boolean = false,
        val diaryDeleteState: UiLoadState = UiLoadState.Idle,
        val showDraftExpiredDialog: Boolean = false,
        val showInAppReviewPopup: Boolean = false,
        val showDraftNotificationPopup: Boolean = false,
        val showDraftNotificationToast: Boolean = false,
        val errorDialogMessage: String? = null,
        val errorScreenMessage: String? = null,
    ) : MavericksState {
        val selectedDate: LocalDate = LocalDate.of(year, month, dayOfMonth)

        fun getCalendarDailyInfo(): CalendarMonthlyInfo.CalendarDailyInfo? {
            return if (isCalendarDataLoaded()) {
                calendarMonthlyInfo.calendarDailyInfoList[dayOfMonth - 1]
            } else {
                null
            }
        }

        private fun isCalendarDataLoaded(): Boolean {
            return calendarMonthlyInfo.calendarDailyInfoList.isNotEmpty() &&
                dayOfMonth > 0 &&
                dayOfMonth <= calendarMonthlyInfo.calendarDailyInfoList.size
        }
    }

    sealed class HomeIntent {
        data class InitializeInfo(val year: Int, val month: Int, val dayOfMonth: Int) : HomeIntent()
        data object OnClickDiaryList : HomeIntent()
        data object OnClickYearMonth : HomeIntent()
        data class ConfirmYearMonthPicker(val newYear: Int, val newMonth: Int) : HomeIntent()
        data object DismissYearMonthPicker : HomeIntent()
        data object OnClickSetting : HomeIntent()
        data class OnClickDay(val year: Int, val month: Int, val dayOfMonth: Int) : HomeIntent()
        data object OnClickDiaryDelete : HomeIntent()
        data object ShowDiaryDeleteDialog : HomeIntent()
        data class ConfirmDiaryDelete(val year: Int, val month: Int, val dayOfMonth: Int) : HomeIntent()
        data object DismissDiaryDelete : HomeIntent()
        data class OnClickWriteDiary(val year: Int, val month: Int, val dayOfMonth: Int) : HomeIntent()
        data class OnClickReplyDiary(val replyStatus: ReplyStatus) : HomeIntent()
        data object ShowDraftExpiredDialog : HomeIntent()
        data class ConfirmDraftExpiredDialog(val year: Int, val month: Int, val dayOfMonth: Int) : HomeIntent()
        data object DismissDraftExpiredDialog : HomeIntent()
        data class RequestNotificationPermission(val granted: Boolean) : HomeIntent()
        data object EnableDraftAlarm : HomeIntent()
        data class UpdateDraftPopupFlag(val show: Boolean) : HomeIntent()
        data object DismissDraftNotificationToast : HomeIntent()
        data class UpdateInAppReviewFlag(val newValue: Boolean) : HomeIntent()
        data object ResetErrorDialogMessage : HomeIntent()
    }

    sealed interface HomeSideEffect {
        data object NavigateToDiaryList : HomeSideEffect
        data object NavigateToSetting : HomeSideEffect
        data class NavigateToWriteDiary(val year: Int, val month: Int, val dayOfMonth: Int) : HomeSideEffect
        data class NavigateToReplyLoading(val replyStatus: ReplyStatus) : HomeSideEffect
    }
}
