package com.sopt.clody.presentation.ui.diarylist.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sopt.clody.presentation.ui.component.YearMonthPicker
import com.sopt.clody.presentation.ui.component.popup.ClodyPopupBottomSheet
import com.sopt.clody.presentation.ui.diarylist.component.DiaryListTopAppBar
import com.sopt.clody.presentation.ui.diarylist.component.MonthlyDiaryList
import com.sopt.clody.presentation.ui.diarylist.navigation.DiaryListNavigator
import com.sopt.clody.presentation.utils.extension.showToast
import com.sopt.clody.ui.theme.ClodyTheme
import java.time.LocalDate

@Composable
fun DiaryListRoute(
    navigator: DiaryListNavigator
) {
    DiaryListScreen(
        onClickCalendar = { navigator.navigateCalendar() },
        onClickReplyDiary = { navigator.navigateReplyDiary() },
    )
}

@Composable
fun DiaryListScreen(
    diaryListViewModel: DiaryListViewModel = hiltViewModel(),
    onClickCalendar: () -> Unit,
    onClickReplyDiary: () -> Unit,
) {
    var showYearMonthPickerState by remember { mutableStateOf(false) }
    val currentDate = LocalDate.now()
    var selectedYear by remember { mutableIntStateOf(currentDate.year) }
    var selectedMonth by remember { mutableIntStateOf(currentDate.monthValue) }
    val diaryListState by diaryListViewModel.diaryListState.collectAsState()

    val onYearMonthSelected: (Int, Int) -> Unit = { year, month ->
        selectedYear = year
        selectedMonth = month
    }

    LaunchedEffect(selectedYear, selectedMonth) {
        diaryListViewModel.fetchMonthlyDiary(selectedYear, selectedMonth)
    }

    Scaffold(
        topBar = {
            Column {
                DiaryListTopAppBar(
                    onClickCalendar = onClickCalendar,
                    selectedYear = selectedYear,
                    selectedMonth = selectedMonth,
                    onShowYearMonthPickerStateChange = { newState -> showYearMonthPickerState = newState }
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(ClodyTheme.colors.gray07)
                )
            }
        },
        containerColor = ClodyTheme.colors.gray08,
    ) { innerPadding ->
        when (diaryListState) {
            is DiaryListState.Idle -> { }
            is DiaryListState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    CircularProgressIndicator(
                        modifier = Modifier.padding(innerPadding).wrapContentSize(Alignment.Center),
                        color = ClodyTheme.colors.mainYellow
                    )
                }
            }
            is DiaryListState.Success -> {
                MonthlyDiaryList(
                    paddingValues = innerPadding,
                    onClickReplyDiary = onClickReplyDiary,
                    diaries = (diaryListState as DiaryListState.Success).data.diaries
                )
            }
            is DiaryListState.Failure -> {
                showToast(message = "${(diaryListState as DiaryListState.Failure).errorMessage}")
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
    }
}
