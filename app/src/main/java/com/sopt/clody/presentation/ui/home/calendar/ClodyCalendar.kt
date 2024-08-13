package com.sopt.clody.presentation.ui.home.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sopt.clody.data.remote.dto.response.MonthlyCalendarResponseDto
import com.sopt.clody.domain.model.generateCalendarDates
import com.sopt.clody.presentation.ui.component.FailureScreen
import com.sopt.clody.presentation.ui.component.LoadingScreen
import com.sopt.clody.presentation.ui.home.calendar.component.DailyDiaryListItem
import com.sopt.clody.presentation.ui.home.calendar.component.MonthlyItem
import com.sopt.clody.presentation.ui.home.screen.DailyDiariesState
import com.sopt.clody.presentation.ui.home.screen.HomeViewModel
import com.sopt.clody.ui.theme.ClodyTheme
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
    val dateList by remember(currentMonth.year, currentMonth.monthValue) {
        mutableStateOf(generateCalendarDates(currentMonth.year, currentMonth.monthValue))
    }
    val initialDayOfWeek = selectedDate.dayOfWeek

    LaunchedEffect(selectedDate) {
        homeViewModel.loadDailyDiariesData(selectedDate.year, selectedDate.monthValue, selectedDate.dayOfMonth)
    }

    val dailyDiariesUiState by homeViewModel.dailyDiariesUiState.collectAsStateWithLifecycle()

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
                val diary = diaries.getOrNull(date.dayOfMonth - 1)
                val diaryCount = diary?.diaryCount ?: 0
                val replyStatus = diary?.replyStatus ?: "UNREADY"
                onDiaryDataUpdated(diaryCount, replyStatus)
            },
            getDiaryDataForDate = { date ->
                diaries.getOrNull(date.dayOfMonth - 1)
            },
        )
        Spacer(modifier = Modifier.height(10.dp))
        HorizontalDivider()
        when (val state = dailyDiariesUiState) {
            is DailyDiariesState.Loading -> {
                LoadingScreen()
            }
            is DailyDiariesState.Error -> {
                FailureScreen()
            }
            is DailyDiariesState.Success -> {
                DailyDiaryListItem(
                    date = selectedDate,
                    dayOfWeek = initialDayOfWeek,
                    dailyDiaries = state.data.diaries,
                    onShowDiaryDeleteStateChange = onShowDiaryDeleteStateChange
                )
            }
            else -> {
            }
        }
    }
}

@Composable
fun HorizontalDivider(
    color: Color = ClodyTheme.colors.gray08,
    thickness: Dp = 6.dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(thickness)
            .background(color)
    )
}

@Preview(showBackground = true)
@Composable
fun ClodyCalendarPreview() {
}
