package com.sopt.clody.presentation.ui.home.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sopt.clody.data.remote.dto.response.MonthlyCalendarResponseDto
import com.sopt.clody.presentation.ui.home.calendar.ClodyCalendar
import com.sopt.clody.presentation.ui.home.component.CloverCount
import com.sopt.clody.ui.theme.ClodyTheme
import java.time.LocalDate

@Composable
fun ScrollableCalendarUI(
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
            selectedDate = selectedDate,
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
