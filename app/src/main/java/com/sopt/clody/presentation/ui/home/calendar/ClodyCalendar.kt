package com.sopt.clody.presentation.ui.home.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sopt.clody.data.remote.dto.response.MonthlyCalendarResponseDto
import com.sopt.clody.domain.model.generateCalendarDates
import com.sopt.clody.presentation.ui.component.FailureScreen
import com.sopt.clody.presentation.ui.component.LoadingScreen
import com.sopt.clody.presentation.ui.home.calendar.component.DailyDiaryListItem
import com.sopt.clody.presentation.ui.home.calendar.component.HorizontalDivider
import com.sopt.clody.presentation.ui.home.calendar.component.MonthlyItem
import com.sopt.clody.presentation.ui.home.screen.DailyDiariesState
import com.sopt.clody.presentation.ui.home.screen.HomeViewModel
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun ClodyCalendar(
    selectedYear: Int,
    selectedMonth: Int,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    diaries: List<MonthlyCalendarResponseDto.Diary>,
    homeViewModel: HomeViewModel,
    onDiaryDataUpdated: (Int, String) -> Unit,
    onShowDiaryDeleteStateChange: (Boolean) -> Unit
) {
    val currentMonth = YearMonth.of(selectedYear, selectedMonth)
    val dateList = remember(currentMonth.year, currentMonth.monthValue) {
        generateCalendarDates(currentMonth.year, currentMonth.monthValue)
    }
    val initialDayOfWeek = selectedDate.dayOfWeek

    val dailyDiariesUiState by homeViewModel.dailyDiariesState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        MonthlyItem(
            dateList = dateList,
            selectedDate = selectedDate,
            onDayClick = { date ->
                onDateSelected(date)
                updateDiaryDataForSelectedDate(date, diaries, onDiaryDataUpdated)
            },
            getDiaryDataForDate = { date ->
                diaries.getOrNull(date.dayOfMonth - 1)
            },
        )
        Spacer(modifier = Modifier.height(10.dp))
        HorizontalDivider()

        when (val state = dailyDiariesUiState) {
            is DailyDiariesState.Loading -> LoadingScreen()
            is DailyDiariesState.Error -> FailureScreen()
            is DailyDiariesState.Success -> DailyDiaryListItem(
                date = selectedDate,
                dayOfWeek = initialDayOfWeek,
                dailyDiaries = state.data.diaries,
                onShowDiaryDeleteStateChange = onShowDiaryDeleteStateChange
            )
            else -> Unit
        }
    }
}

private fun updateDiaryDataForSelectedDate(
    date: LocalDate,
    diaries: List<MonthlyCalendarResponseDto.Diary>,
    onDiaryDataUpdated: (Int, String) -> Unit
) {
    val diary = diaries.getOrNull(date.dayOfMonth - 1)
    val diaryCount = diary?.diaryCount ?: 0
    val replyStatus = diary?.replyStatus ?: "UNREADY"
    onDiaryDataUpdated(diaryCount, replyStatus)
}
