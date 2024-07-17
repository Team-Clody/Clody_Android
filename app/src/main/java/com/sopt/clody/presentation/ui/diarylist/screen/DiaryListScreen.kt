package com.sopt.clody.presentation.ui.diarylist.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sopt.clody.presentation.ui.component.popup.ClodyPopupBottomSheet
import com.sopt.clody.presentation.ui.diarylist.component.DiaryListTopAppBar
import com.sopt.clody.presentation.ui.diarylist.component.MonthlyDiaryList
import com.sopt.clody.presentation.ui.component.YearMonthPicker
import com.sopt.clody.presentation.ui.diarylist.navigation.DiaryListNavigator
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
    onClickCalendar: () -> Unit,
    onClickReplyDiary: () -> Unit,
) {
    var showYearMonthPickerState by remember { mutableStateOf(false) }
    val currentDate = LocalDate.now()
    var selectedYear by remember { mutableStateOf(currentDate.year) }
    var selectedMonth by remember { mutableStateOf(currentDate.monthValue) }

    val onYearMonthSelected: (Int, Int) -> Unit = { year, month ->
        selectedYear = year
        selectedMonth = month
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
                Box( // 구분선
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(ClodyTheme.colors.gray07)
                )
            }
        },
        containerColor = ClodyTheme.colors.gray08,
    ) { innerPadding ->
        MonthlyDiaryList(
            paddingValues = innerPadding,
            onClickReplyDiary = onClickReplyDiary,
            selectedYear = selectedYear,
            selectedMonth = selectedMonth
        )
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
