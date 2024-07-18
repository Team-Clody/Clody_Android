package com.sopt.clody.presentation.ui.home.screen

import android.app.Activity
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.sopt.clody.data.remote.dto.response.MonthlyCalendarResponseDto
import com.sopt.clody.presentation.ui.component.timepicker.YearMonthPicker
import com.sopt.clody.presentation.ui.component.bottomsheet.DiaryDeleteSheet
import com.sopt.clody.presentation.ui.component.dialog.ClodyDialog
import com.sopt.clody.presentation.ui.component.popup.ClodyPopupBottomSheet
import com.sopt.clody.presentation.ui.home.calendar.ClodyCalendar
import com.sopt.clody.presentation.ui.home.component.CloverCount
import com.sopt.clody.presentation.ui.home.component.DiaryStateButton
import com.sopt.clody.presentation.ui.home.component.HomeTopAppBar
import com.sopt.clody.presentation.ui.home.navigation.HomeNavigator
import com.sopt.clody.ui.theme.ClodyTheme
import java.time.LocalDate

@Composable
fun HomeRoute(
    navigator: HomeNavigator,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    HomeScreen(
        homeViewModel = homeViewModel,
        onClickDiaryList = { navigator.navigateDiaryList() },
        onClickSetting = { navigator.navigateSetting() },
        onClickWriteDiary = { year, month, day -> navigator.navigateWriteDiary(year, month, day) },
        onClickReplyDiary = { year, month, day -> navigator.navigateReplyLoading(year, month, day) }
    )
}
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    onClickDiaryList: () -> Unit,
    onClickSetting: () -> Unit,
    onClickWriteDiary: (Int, Int, Int) -> Unit,
    onClickReplyDiary: (Int, Int, Int) -> Unit,
) {
    var showYearMonthPickerState by remember { mutableStateOf(false) }
    var showDiaryDeleteState by remember { mutableStateOf(false) }
    var showDiaryDeleteDialog by remember { mutableStateOf(false) }
    val currentDate = LocalDate.now()
    var selectedYear by remember { mutableStateOf(currentDate.year) }
    var selectedMonth by remember { mutableStateOf(currentDate.monthValue) }

    val selectedDate by homeViewModel.selectedDate.collectAsStateWithLifecycle()
    val diaryCount by homeViewModel.diaryCount.collectAsStateWithLifecycle()
    val replyStatus by homeViewModel.replyStatus.collectAsStateWithLifecycle()
    val isToday by homeViewModel.isToday.collectAsStateWithLifecycle()
    val deleteDiaryResult by homeViewModel.deleteDiaryResult.collectAsStateWithLifecycle()
    val calendarData by homeViewModel.monthlyCalendarData.collectAsStateWithLifecycle()

    LaunchedEffect(selectedYear, selectedMonth) {
        homeViewModel.loadCalendarData(selectedYear, selectedMonth)
    }

    LaunchedEffect(deleteDiaryResult) {
        if (deleteDiaryResult is DeleteDiaryState.Success) {
            homeViewModel.loadCalendarData(selectedYear, selectedMonth)
            homeViewModel.loadDailyDiariesData(selectedDate.year, selectedDate.monthValue, selectedDate.dayOfMonth)
        }
    }

    Log.d("HomeScreen", "diaryCount: $diaryCount, replyStatus: $replyStatus, isToday: $isToday")

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
                onClickDiaryList = onClickDiaryList,
                onClickSetting = onClickSetting,
                onShowYearMonthPickerStateChange = { newState -> showYearMonthPickerState = newState },
                selectedYear = selectedYear,
                selectedMonth = selectedMonth,
            )
        },
        content = { innerPadding ->
            calendarData?.let { result ->
                result.fold(
                    onSuccess = { data ->
                        ScrollableCalendarView(
                            selectedYear = selectedYear,
                            selectedMonth = selectedMonth,
                            cloverCount = data.totalMonthlyCount,
                            diaries = data.diaries,
                            homeViewModel = homeViewModel,
                            onShowDiaryDeleteStateChange = { newState -> showDiaryDeleteState = newState },
                            selectedDate = selectedDate,
                            onDiaryDataUpdated = { diaryCount, replyStatus ->
                                homeViewModel.updateDiaryState(data.diaries)
                            },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }, onFailure = { throwable ->
                        Log.e("HomeScreen", "Failed to load calendar data: ${throwable.message}")
                    }
                )
            }
        },
        bottomBar = {
            Column(modifier = Modifier.background(ClodyTheme.colors.white)) {
                Spacer(modifier = Modifier.height(14.dp))
                DiaryStateButton(
                    diaryCount = diaryCount,
                    replyStatus = replyStatus,
                    isToday = isToday,
                    year = selectedDate.year,
                    month = selectedDate.monthValue,
                    day = selectedDate.dayOfMonth,
                    onClickWriteDiary = onClickWriteDiary,
                    onClickReplyDiary = { onClickReplyDiary(selectedDate.year, selectedDate.monthValue, selectedDate.dayOfMonth) }
                )
                Spacer(modifier = Modifier.height(14.dp))
            }
        }
    )

    if (showYearMonthPickerState) {
        ClodyPopupBottomSheet(onDismissRequest = { showYearMonthPickerState = false }) {
            YearMonthPicker(
                onDismissRequest = { showYearMonthPickerState = false },
                selectedYear = selectedYear,
                selectedMonth = selectedMonth,
                onYearMonthSelected = { year, month ->
                    selectedYear = year
                    selectedMonth = month
                    homeViewModel.updateSelectedDate(LocalDate.of(year, month, 1))
                }
            )
        }
    }

    if (showDiaryDeleteState) {
        DiaryDeleteSheet(
            onDismiss = { showDiaryDeleteState = false },
            onShowDiaryDeleteDialogStateChange = { newState -> showDiaryDeleteDialog = newState }
        )
    }

    if (showDiaryDeleteDialog) {
        ClodyDialog(
            titleMassage = "정말 일기를 삭제할까요?",
            descriptionMassage = "아직 답장이 오지 않았거나 삭제하고\n다시 작성한 일기는 답장을 받을 수 없어요.",
            confirmOption = "삭제할래요",
            dismissOption = "아니요",
            confirmAction = {
                homeViewModel.deleteDailyDiary(selectedYear, selectedMonth, selectedDate.dayOfMonth)
                showDiaryDeleteDialog = false
            },
            onDismiss = { showDiaryDeleteDialog = false },
            confirmButtonColor = ClodyTheme.colors.red,
            confirmButtonTextColor = ClodyTheme.colors.white
        )
    }
}

@Composable
fun ScrollableCalendarView(
    selectedYear: Int,
    selectedMonth: Int,
    cloverCount: Int,
    homeViewModel: HomeViewModel,
    diaries: List<MonthlyCalendarResponseDto.Diary>,
    onShowDiaryDeleteStateChange: (Boolean) -> Unit,
    selectedDate: LocalDate,
    onDiaryDataUpdated: (Int, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    LaunchedEffect(selectedDate) {
        val diary = diaries.getOrNull(selectedDate.dayOfMonth - 1)
        val diaryCount = diary?.diaryCount ?: 0
        val replyStatus = diary?.replyStatus ?: "UNREADY"
        onDiaryDataUpdated(diaryCount, replyStatus)
        homeViewModel.loadDailyDiariesData(selectedDate.year, selectedDate.monthValue, selectedDate.dayOfMonth)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(ClodyTheme.colors.white)
    ) {
        CloverCount(cloverCount = cloverCount)
        Spacer(modifier = Modifier.height(20.dp))
        ClodyCalendar(
            selectedYear = selectedYear,
            selectedMonth = selectedMonth,
            selectedDate = remember { mutableStateOf(selectedDate) },
            onDateSelected = { date ->
                homeViewModel.updateSelectedDate(date)
            },
            diaries = diaries,
            homeViewModel = homeViewModel,
            onDiaryDataUpdated = onDiaryDataUpdated,
            onShowDiaryDeleteStateChange = onShowDiaryDeleteStateChange
        )
    }
}
