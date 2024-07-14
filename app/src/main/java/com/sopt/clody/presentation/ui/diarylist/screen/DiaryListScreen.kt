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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sopt.clody.presentation.ui.component.popup.ClodyPopupBottomSheet
import com.sopt.clody.presentation.ui.diarylist.component.DiaryListTopAppBar
import com.sopt.clody.presentation.ui.diarylist.component.MonthlyDiaryList
import com.sopt.clody.presentation.ui.diarylist.component.YearMonthPicker
import com.sopt.clody.presentation.ui.diarylist.navigator.DiaryListNavigator
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun DiaryListRoute(
    navigator: DiaryListNavigator
) {
    DiaryListScreen(
        onClickBack = { navigator.navigateBack() },
        onClickCalendar = { navigator.navigateCalendar() },
        onClickReplyDiary = { navigator.navigateReplyDiary() },
    )
}

@Composable
fun DiaryListScreen(
    onClickBack: () -> Unit,
    onClickCalendar: () -> Unit,
    onClickReplyDiary: () -> Unit,
) {
    var showYearMonthPickerState by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Column {
                DiaryListTopAppBar(
                    onClickCalendar = onClickCalendar,
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
        MonthlyDiaryList(paddingValues = innerPadding, onClickReplyDiary = onClickReplyDiary)
    }

    if (showYearMonthPickerState) {
        ClodyPopupBottomSheet(onDismissRequest = { showYearMonthPickerState = false }) {
            YearMonthPicker(
                onDismissRequest = { showYearMonthPickerState = false }
            )
        }
    }
}

@Preview
@Composable
fun Show() {
    DiaryListScreen(onClickBack = { /*TODO*/ }, onClickCalendar = { /*TODO*/ }) {
        
    }
}
