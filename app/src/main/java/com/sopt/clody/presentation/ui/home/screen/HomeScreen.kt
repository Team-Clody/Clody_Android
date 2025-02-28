package com.sopt.clody.presentation.ui.home.screen

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sopt.clody.presentation.ui.component.FailureScreen
import com.sopt.clody.presentation.ui.component.LoadingScreen
import com.sopt.clody.presentation.ui.component.bottomsheet.DiaryDeleteSheet
import com.sopt.clody.presentation.ui.component.dialog.ClodyDialog
import com.sopt.clody.presentation.ui.component.popup.ClodyPopupBottomSheet
import com.sopt.clody.presentation.ui.component.timepicker.YearMonthPicker
import com.sopt.clody.presentation.ui.home.component.DiaryStateButton
import com.sopt.clody.presentation.ui.home.component.HomeTopAppBar
import com.sopt.clody.presentation.ui.home.model.DiaryDateData
import com.sopt.clody.presentation.ui.home.navigation.HomeNavigator
import com.sopt.clody.presentation.utils.amplitude.AmplitudeConstraints
import com.sopt.clody.presentation.utils.amplitude.AmplitudeUtils
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun HomeRoute(
    navigator: HomeNavigator,
    homeViewModel: HomeViewModel = hiltViewModel(),
    selectedYear: Int,
    selectedMonth: Int,
) {
    val calendarState by homeViewModel.calendarState.collectAsStateWithLifecycle()
    val dailyDiariesState by homeViewModel.dailyDiariesState.collectAsStateWithLifecycle()

    val isError = calendarState is CalendarState.Error || dailyDiariesState is DailyDiariesState.Error
    val errorMessage = when {
        calendarState is CalendarState.Error -> (calendarState as CalendarState.Error).message
        dailyDiariesState is DailyDiariesState.Error -> (dailyDiariesState as DailyDiariesState.Error).message
        else -> ""
    }

    LaunchedEffect(Unit) {
        AmplitudeUtils.trackEvent(eventName = AmplitudeConstraints.HOME)
    }

    LaunchedEffect(selectedYear, selectedMonth) {
        homeViewModel.refreshCalendarDataCalendarData(selectedYear, selectedMonth)
        val selectedDate = homeViewModel.selectedDate.value
        homeViewModel.loadDailyDiariesData(selectedYear, selectedMonth, selectedDate.dayOfMonth)
    }

    if (isError) {
        FailureScreen(
            message = errorMessage,
            confirmAction = {
                val selectedDate = homeViewModel.selectedDate.value
                homeViewModel.refreshCalendarDataCalendarData(selectedYear, selectedMonth)
                homeViewModel.loadDailyDiariesData(selectedYear, selectedMonth, selectedDate.dayOfMonth)
            }
        )
    } else {
        HomeScreen(
            homeViewModel = homeViewModel,
            onClickDiaryList = { selectedYearFromHome, selectedMonthFromHome -> navigator.navigateDiaryList(selectedYearFromHome, selectedMonthFromHome) },
            onClickSetting = { navigator.navigateSetting() },
            onClickWriteDiary = { year, month, day ->
                AmplitudeUtils.trackEvent(eventName = AmplitudeConstraints.HOME_WRITING_DIARY)
                navigator.navigateWriteDiary(year, month, day) },
            onClickReplyDiary = { year, month, day, replyStatus ->
                AmplitudeUtils.trackEvent(eventName = AmplitudeConstraints.HOME_REPLY)
                navigator.navigateReplyLoading(year, month, day, replyStatus) },
            selectedYear = selectedYear,
            selectedMonth = selectedMonth
        )
    }
}

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    onClickDiaryList: (Int, Int) -> Unit,
    onClickSetting: () -> Unit,
    onClickWriteDiary: (Int, Int, Int) -> Unit,
    onClickReplyDiary: (Int, Int, Int, String) -> Unit,
    selectedYear: Int,
    selectedMonth: Int
) {
    val (isError, errorMessage) = homeViewModel.errorState.collectAsStateWithLifecycle().value

    if (isError) {
        FailureScreen(
            message = errorMessage,
            confirmAction = {
                homeViewModel.refreshCalendarDataCalendarData(selectedYear, selectedMonth)
                val selectedDate = homeViewModel.selectedDate.value
                homeViewModel.loadDailyDiariesData(selectedYear, selectedMonth, selectedDate.dayOfMonth)
            }
        )
    } else {
        val selectedDiaryDate by homeViewModel.selectedDiaryDate.collectAsStateWithLifecycle()
        val selectedDate by homeViewModel.selectedDate.collectAsStateWithLifecycle()
        val diaryCount by homeViewModel.diaryCount.collectAsStateWithLifecycle()
        val replyStatus by homeViewModel.replyStatus.collectAsStateWithLifecycle()
        val isToday by homeViewModel.isToday.collectAsStateWithLifecycle()
        val isDeleted by homeViewModel.isDeleted.collectAsStateWithLifecycle()
        val calendarState by homeViewModel.calendarState.collectAsStateWithLifecycle()
        val deleteDiaryState by homeViewModel.deleteDiaryState.collectAsStateWithLifecycle()
        val showYearMonthPickerState by homeViewModel.showYearMonthPickerState.collectAsStateWithLifecycle()
        val showDiaryDeleteState by homeViewModel.showDiaryDeleteState.collectAsStateWithLifecycle()
        val showDiaryDeleteDialog by homeViewModel.showDiaryDeleteDialog.collectAsStateWithLifecycle()

        var backPressedTime by remember { mutableStateOf(0L) }
        val backPressThreshold = 2000
        val context = LocalContext.current

        BackHandler {
            val currentTime = System.currentTimeMillis()
            if (currentTime - backPressedTime <= backPressThreshold) {
                (context as? Activity)?.finish()
            } else {
                backPressedTime = currentTime
            }
        }

        Scaffold(
            topBar = {
                HomeTopAppBar(
                    onClickDiaryList = {
                        AmplitudeUtils.trackEvent(eventName = AmplitudeConstraints.HOME_LIST_DIARY)
                        onClickDiaryList(selectedDiaryDate.year, selectedDiaryDate.month) },
                    onClickSetting = onClickSetting,
                    onShowYearMonthPickerStateChange = { newState -> homeViewModel.setShowYearMonthPickerState(newState) },
                    selectedYear = selectedDiaryDate.year,
                    selectedMonth = selectedDiaryDate.month,
                )
            },
            containerColor = ClodyTheme.colors.white,
            content = { innerPadding ->
                when (val state = calendarState) {
                    is CalendarState.Idle -> {  }

                    is CalendarState.Loading -> {
                        LoadingScreen()
                    }

                    is CalendarState.Success -> {
                        ScrollableCalendar(
                            selectedYear = selectedDiaryDate.year,
                            selectedMonth = selectedDiaryDate.month,
                            cloverCount = state.data.totalCloverCount,
                            diaries = state.data.diaries,
                            homeViewModel = homeViewModel,
                            onShowDiaryDeleteStateChange = { newState -> homeViewModel.setShowDiaryDeleteState(newState) },
                            selectedDate = selectedDate,
                            onDiaryDataUpdated = { diaryCount, replyStatus ->
                                homeViewModel.updateDiaryState(state.data.diaries)
                            },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }

                    is CalendarState.Error -> {
                        homeViewModel.setErrorState(true, state.message ?: "알 수 없는 오류가 발생했습니다.")
                    }
                }

                when (deleteDiaryState) {
                    is DeleteDiaryState.Idle -> { }

                    is DeleteDiaryState.Loading -> {
                        LoadingScreen()
                    }

                    is DeleteDiaryState.Success -> {

                    }

                    is DeleteDiaryState.Failure -> {
                        homeViewModel.setErrorState(true, "일기 삭제 중 오류가 발생했습니다.")
                    }
                }
            },
            bottomBar = {
                Column(modifier = Modifier.background(ClodyTheme.colors.white)) {
                    Spacer(modifier = Modifier.height(14.dp))
                    DiaryStateButton(
                        diaryCount = diaryCount,
                        replyStatus = replyStatus,
                        isToday = isToday,
                        isDeleted = isDeleted,
                        year = selectedDate.year,
                        month = selectedDate.monthValue,
                        day = selectedDate.dayOfMonth,
                        onClickWriteDiary = onClickWriteDiary,
                        onClickReplyDiary = { onClickReplyDiary(selectedDate.year, selectedDate.monthValue, selectedDate.dayOfMonth, replyStatus) }
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                }
            }
        )

        if (showYearMonthPickerState) {
            ClodyPopupBottomSheet(onDismissRequest = { homeViewModel.setShowYearMonthPickerState(false) }) {
                YearMonthPicker(
                    onDismissRequest = { homeViewModel.setShowYearMonthPickerState(false) },
                    selectedYear = selectedDiaryDate.year,
                    selectedMonth = selectedDiaryDate.month,
                    onYearMonthSelected = { year, month ->
                        homeViewModel.updateSelectedDiaryDate(DiaryDateData(year, month))
                        homeViewModel.loadCalendarData(year, month)
                    }
                )
            }
        }

        if (showDiaryDeleteState) {
            DiaryDeleteSheet(
                onDismiss = { homeViewModel.setShowDiaryDeleteState(false) },
                showDiaryDeleteDialog = {
                    AmplitudeUtils.trackEvent(eventName = AmplitudeConstraints.HOME_DELETE_DIARY)
                    homeViewModel.setShowDiaryDeleteDialog(true) }
            )
        }

        if (showDiaryDeleteDialog) {
            ClodyDialog(
                titleMassage = "정말 일기를 삭제할까요?",
                descriptionMassage = "아직 답장이 오지 않았거나 삭제하고\n다시 작성한 일기는 답장을 받을 수 없어요.",
                confirmOption = "삭제할래요",
                dismissOption = "아니요",
                confirmAction = {
                    homeViewModel.deleteDailyDiary(selectedDiaryDate.year, selectedDiaryDate.month, selectedDate.dayOfMonth)
                    homeViewModel.setShowDiaryDeleteDialog(false)
                },
                onDismiss = {
                    AmplitudeUtils.trackEvent(eventName = AmplitudeConstraints.HOME_NO_DELETE_DIARY)
                    homeViewModel.setShowDiaryDeleteDialog(false) },
                confirmButtonColor = ClodyTheme.colors.red,
                confirmButtonTextColor = ClodyTheme.colors.white
            )
        }
    }
}
