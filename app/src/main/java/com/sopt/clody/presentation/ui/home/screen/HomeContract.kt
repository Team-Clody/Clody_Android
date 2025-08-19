package com.sopt.clody.presentation.ui.home.screen

import com.airbnb.mvrx.MavericksState
import com.sopt.clody.domain.model.CalendarMonthlyInfo
import com.sopt.clody.domain.model.DailyDiaryInfo
import java.time.LocalDate

class HomeContract {
    data class HomeState(
        val homeUiState: HomeUiState = HomeUiState.Idle,
        val errorMessage: String? = null,

        val year: Int = LocalDate.now().year,
        val month: Int = LocalDate.now().monthValue,
        val dayOfMonth: Int = LocalDate.now().dayOfMonth,
        val calendarMonthlyInfo: CalendarMonthlyInfo = CalendarMonthlyInfo(),
        val selectedDailyInfo: DailyDiaryInfo = DailyDiaryInfo(),
        val showYearMonthPicker: Boolean = false,

        val showDiaryDeleteBottomSheet: Boolean = false,
        val showDiaryDeleteDialog: Boolean = false,

        val showDraftExpiredDialog: Boolean = false,

        val showDraftNotificationPopup: Boolean = false,
        val showDraftNotificationToast: Boolean = false,

        val showInAppReviewPopup: Boolean = false,
    ) : MavericksState {
        val selectedDate: LocalDate = LocalDate.of(year, month, dayOfMonth)

        fun isDraftExpired(): Boolean = selectedDate != LocalDate.now()

        // 현재 선택된 날짜의 CalendarDailyInfo를 안전하게 가져오기
        fun getCurrentCalendarDailyInfo(): CalendarMonthlyInfo.CalendarDailyInfo? {
            return if (isCalendarDataLoaded()) {
                calendarMonthlyInfo.calendarDailyInfoList[dayOfMonth - 1]
            } else {
                null
            }
        }

        // 캘린더 데이터가 안전하게 로드되었는지 확인
        private fun isCalendarDataLoaded(): Boolean {
            return calendarMonthlyInfo.calendarDailyInfoList.isNotEmpty() &&
                dayOfMonth > 0 &&
                dayOfMonth <= calendarMonthlyInfo.calendarDailyInfoList.size
        }
    }

    sealed class HomeIntent {
        data class LoadCalendarMonthlyInfo(val year: Int, val month: Int) : HomeIntent()
        data class LoadDailyDiaryInfo(val year: Int, val month: Int, val dayOfMonth: Int) : HomeIntent()

        data object OnClickDiaryList : HomeIntent()
        data object OnClickYearMonth : HomeIntent()
        data class UpdateYearMonth(val newYear: Int, val newMonth: Int) : HomeIntent()
        data object DismissYearMonthPicker : HomeIntent()
        data object OnClickSetting : HomeIntent()

        data object OnClickDiaryDelete : HomeIntent()
        data object ShowDiaryDeleteDialog : HomeIntent()
        data class ConfirmDiaryDelete(val year: Int, val month: Int, val dayOfMonth: Int) : HomeIntent()
        data object DismissDiaryDelete : HomeIntent()

        data object OnClickWriteDiary : HomeIntent()
        data object ShowDraftExpiredDialog : HomeIntent()

        data object OnClickReplyDiary : HomeIntent()

        data object EnableDraftAlarm : HomeIntent()
        data class SendNotification(val granted: Boolean) : HomeIntent()
        data class UpdateInAppReview(val show: Boolean) : HomeIntent()
        data class UpdateDraftPopup(val show: Boolean) : HomeIntent()
    }

    sealed interface HomeSideEffect {
        data object NavigateToDiaryList : HomeSideEffect
        data object NavigateToSetting : HomeSideEffect
        data object NavigateToWriteDiary : HomeSideEffect
        data object NavigateToReplyLoading : HomeSideEffect
    }
}
