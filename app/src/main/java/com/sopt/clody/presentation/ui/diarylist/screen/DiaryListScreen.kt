package com.sopt.clody.presentation.ui.diarylist.screen

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.sopt.clody.presentation.ui.component.FailureScreen
import com.sopt.clody.presentation.ui.component.LoadingScreen
import com.sopt.clody.presentation.ui.component.popup.ClodyPopupBottomSheet
import com.sopt.clody.presentation.ui.component.timepicker.YearMonthPicker
import com.sopt.clody.presentation.ui.diarylist.component.DiaryListTopAppBar
import com.sopt.clody.presentation.ui.diarylist.component.MonthlyDiaryList
import com.sopt.clody.presentation.ui.diarylist.navigation.DiaryListNavigator
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun DiaryListRoute(
    navigator: DiaryListNavigator,
    diaryListViewModel: DiaryListViewModel = hiltViewModel(),
    selectedYearFromHome: Int,
    selectedMonthFromHome: Int
) {
    var selectedYearInDiaryList by remember { mutableIntStateOf(selectedYearFromHome) }
    var selectedMonthInDiaryList by remember { mutableIntStateOf(selectedMonthFromHome) }
    val diaryListState by diaryListViewModel.diaryListState.collectAsState()
    val deleteDiaryResult by diaryListViewModel.deleteDiaryResult.collectAsState()
    var showYearMonthPicker by remember { mutableStateOf(false) }

    LaunchedEffect(selectedYearInDiaryList, selectedMonthInDiaryList) {
        diaryListViewModel.fetchMonthlyDiary(selectedYearInDiaryList, selectedMonthInDiaryList)
    }

    DiaryListScreen(
        diaryListViewModel = diaryListViewModel,
        selectedYearInDiaryList = selectedYearInDiaryList,
        selectedMonthInDiaryList = selectedMonthInDiaryList,
        updateYearAndMonth = { newYear, newMonth ->
            selectedYearInDiaryList = newYear
            selectedMonthInDiaryList = newMonth
        },
        diaryListState = diaryListState,
        deleteDiaryResult = deleteDiaryResult,
        showYearMonthPicker = showYearMonthPicker,
        updateYearMonthPicker = { state -> showYearMonthPicker = state },
        onClickCalendar = { selectedYearFromDiaryList, selectedMonthFromDiaryList -> navigator.navigateHome(selectedYearFromDiaryList, selectedMonthFromDiaryList) },
        onClickReplyDiary = { year, month, day -> navigator.navigateReplyLoading(year, month, day) }
    )
}

@Composable
fun DiaryListScreen(
    diaryListViewModel: DiaryListViewModel,
    selectedYearInDiaryList: Int,
    selectedMonthInDiaryList: Int,
    updateYearAndMonth: (Int, Int) -> Unit,
    diaryListState: DiaryListState,
    deleteDiaryResult: DeleteDiaryListState,
    showYearMonthPicker: Boolean,
    updateYearMonthPicker: (Boolean) -> Unit,
    onClickCalendar: (Int, Int) -> Unit,
    onClickReplyDiary: (Int, Int, Int) -> Unit,
) {
    Scaffold(
        topBar = {
            DiaryListTopAppBar(
                onClickCalendar = { onClickCalendar(selectedYearInDiaryList, selectedMonthInDiaryList) },
                selectedYear = selectedYearInDiaryList,
                selectedMonth = selectedMonthInDiaryList,
                updateYearMonthPicker = updateYearMonthPicker
            )
        },
        containerColor = ClodyTheme.colors.gray08,
        content = { innerPadding ->
            when (diaryListState) {
                is DiaryListState.Idle -> {

                }

                is DiaryListState.Loading -> {
                    LoadingScreen()
                }

                is DiaryListState.Success -> {
                    MonthlyDiaryList(
                        paddingValues = innerPadding,
                        onClickReplyDiary = onClickReplyDiary,
                        diaries = diaryListState.data.diaries,
                        diaryListViewModel = diaryListViewModel
                    )
                }

                is DiaryListState.Failure -> {
                    FailureScreen()
                }
            }

            when (deleteDiaryResult) {
                is DeleteDiaryListState.Idle -> {

                }

                is DeleteDiaryListState.Loading -> {
                    LoadingScreen()
                }

                is DeleteDiaryListState.Success -> {
                    LaunchedEffect(Unit) {
                        diaryListViewModel.fetchMonthlyDiary(selectedYearInDiaryList, selectedMonthInDiaryList)
                    }
                }

                is DeleteDiaryListState.Failure -> {
                    FailureScreen()
                }
            }
        }
    )

    if (showYearMonthPicker) {
        ClodyPopupBottomSheet(onDismissRequest = { updateYearMonthPicker(false) }) {
            YearMonthPicker(
                onDismissRequest = { updateYearMonthPicker(false) },
                selectedYear = selectedYearInDiaryList,
                selectedMonth = selectedMonthInDiaryList,
                onYearMonthSelected = updateYearAndMonth
            )
        }
    }
}
