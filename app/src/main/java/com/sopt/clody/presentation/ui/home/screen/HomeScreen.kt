package com.sopt.clody.presentation.ui.home.screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sopt.clody.data.remote.dto.response.MonthlyCalendarResponseDto
import com.sopt.clody.presentation.ui.component.YearMonthPicker
import com.sopt.clody.presentation.ui.component.bottomsheet.DiaryDeleteSheet
import com.sopt.clody.presentation.ui.component.dialog.ClodyDialog
import com.sopt.clody.presentation.ui.component.popup.ClodyPopupBottomSheet
import com.sopt.clody.presentation.ui.home.HomeViewModel
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
        onClickReplyDiary = { navigator.navigateReplyDiary() }
    )
}

@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    onClickDiaryList: () -> Unit,
    onClickSetting: () -> Unit,
    onClickWriteDiary: (Int, Int, Int) -> Unit,
    onClickReplyDiary: () -> Unit,
) {
    var showYearMonthPickerState by remember { mutableStateOf(false) }
    var showDiaryDeleteState by remember { mutableStateOf(false) }
    var showDiaryDeleteDialog by remember { mutableStateOf(false) }
    val currentDate = LocalDate.now()
    var selectedYear by remember { mutableStateOf(currentDate.year) }
    var selectedMonth by remember { mutableStateOf(currentDate.monthValue) }

    val onYearMonthSelected: (Int, Int) -> Unit = { year, month ->
        selectedYear = year
        selectedMonth = month
    }

    LaunchedEffect(selectedYear, selectedMonth) {
        homeViewModel.loadCalendarData(selectedYear, selectedMonth)
    }

    val calendarData by homeViewModel.monthlyCalendarData.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HomeTopAppBar(
            onClickDiaryList = onClickDiaryList,
            onClickSetting = onClickSetting,
            onShowYearMonthPickerStateChange = { newState -> showYearMonthPickerState = newState },
            selectedYear = selectedYear,
            selectedMonth = selectedMonth,
        )
        calendarData?.let { result ->
            result.fold(
                onSuccess = { data ->
                    ScrollableCalendarView(
                        selectedYear = selectedYear,
                        selectedMonth = selectedMonth,
                        cloverCount = data.totalMonthlyCount,
                        diaries = data.diaries,
                        homeViewModel = homeViewModel,
                        onClickWriteDiary = onClickWriteDiary,
                        onClickReplyDiary = onClickReplyDiary,
                        onShowDiaryDeleteStateChange = { newState -> showDiaryDeleteState = newState }
                    )
                }, onFailure = { throwable ->
                    Log.e("HomeScreen", "Failed to load calendar data: ${throwable.message}")
                }
            )
        }
    }

    if (showYearMonthPickerState) {
        ClodyPopupBottomSheet(onDismissRequest = { showYearMonthPickerState = false }) {
            YearMonthPicker(
                onDismissRequest = { showYearMonthPickerState = false },
                selectedYear = selectedYear,
                selectedMonth = selectedMonth,
                onYearMonthSelected = onYearMonthSelected
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
            confirmAction = { /* TODO : 일기 삭제 로직 */ },
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
    onClickWriteDiary: (Int, Int, Int) -> Unit,
    onClickReplyDiary: () -> Unit,
    onShowDiaryDeleteStateChange: (Boolean) -> Unit
) {
    val scrollState = rememberScrollState()
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(ClodyTheme.colors.white)
    ) {
        CloverCount(cloverCount = cloverCount)
        Spacer(modifier = Modifier.height(20.dp))
        ClodyCalendar(
            selectedYear = selectedYear,
            selectedMonth = selectedMonth,
            selectedDate = selectedDate,
            onDateSelected = { date -> selectedDate = date },
            diaries = diaries,
            homeViewModel = homeViewModel,
            onShowDiaryDeleteStateChange = onShowDiaryDeleteStateChange
        )
        Spacer(modifier = Modifier.height(14.dp))
        DiaryStateButton(
            diaryCount = 5,
            replyStatus = "READY_NOT_READ",
            onClickWriteDiary = { onClickWriteDiary(selectedDate.year, selectedDate.monthValue, selectedDate.dayOfMonth) },
            onClickReplyDiary = onClickReplyDiary
        )
        Spacer(modifier = Modifier.height(14.dp))
    }
}
