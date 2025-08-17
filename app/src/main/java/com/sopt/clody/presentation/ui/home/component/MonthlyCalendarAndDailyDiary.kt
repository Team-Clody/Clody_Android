package com.sopt.clody.presentation.ui.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sopt.clody.R
import com.sopt.clody.data.remote.dto.response.DailyDiariesResponseDto
import com.sopt.clody.data.remote.dto.response.MonthlyCalendarResponseDto
import com.sopt.clody.presentation.ui.component.FailureScreen
import com.sopt.clody.presentation.ui.component.LoadingScreen
import com.sopt.clody.presentation.ui.home.screen.DailyDiariesState
import com.sopt.clody.presentation.ui.home.screen.HomeViewModel
import com.sopt.clody.presentation.ui.type.ReplyStatus
import com.sopt.clody.ui.theme.ClodyTheme
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun MonthlyCalendarAndDailyDiary(
    selectedYear: Int,
    selectedMonth: Int,
    cloverCount: Int,
    homeViewModel: HomeViewModel,
    diaries: List<MonthlyCalendarResponseDto.Diary>,
    onShowDiaryDeleteStateChange: (Boolean) -> Unit,
    selectedDate: LocalDate,
    onDiaryDataUpdated: (Int, ReplyStatus) -> Unit,
    modifier: Modifier = Modifier,
    dailyDiariesState: DailyDiariesState<DailyDiariesResponseDto>,
) {
    val scrollState = rememberScrollState()
    val currentMonth = YearMonth.of(selectedYear, selectedMonth)
    val dateList = remember(currentMonth.year, currentMonth.monthValue) {
        (1..YearMonth.of(currentMonth.year, currentMonth.monthValue).lengthOfMonth()).map { day ->
            CalendarDate(day, currentMonth.monthValue, currentMonth.year)
        }
    }
    val initialDayOfWeek = selectedDate.dayOfWeek

    LaunchedEffect(selectedDate, diaries) {
        if (selectedDate.year == selectedYear && selectedDate.monthValue == selectedMonth) {
            homeViewModel.updateDiaryState(diaries)
            onDiaryDataUpdated(
                homeViewModel.diaryCount.value,
                homeViewModel.replyStatus.value,
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(ClodyTheme.colors.white),
    ) {
        // 클로버 총 갯수
        Text(
            text = stringResource(R.string.home_total_clover, cloverCount),
            style = ClodyTheme.typography.detail1SemiBold,
            color = ClodyTheme.colors.darkGreen,
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 10.dp, bottom = 25.dp, end = 20.dp)
                .border(9.dp, ClodyTheme.colors.lightGreenBack, shape = RoundedCornerShape(9.dp))
                .background(ClodyTheme.colors.lightGreenBack, shape = RoundedCornerShape(9.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp),
            textAlign = TextAlign.Center,
        )

        // 캘린더 + 일별 일기 리스트
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            MonthlyCalendar(
                dateList = dateList,
                selectedDate = selectedDate,
                onDayClick = { date ->
                    homeViewModel.updateSelectedDate(date)
                    homeViewModel.updateDiaryState(diaries)
                },
                getDiaryDataForDate = { date ->
                    diaries.getOrNull(date.dayOfMonth - 1)
                },
            )

            HorizontalDivider(
                color = ClodyTheme.colors.gray08,
                thickness = 6.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp, bottom = 20.dp),
            )

            when (dailyDiariesState) {
                is DailyDiariesState.Idle -> {
                }

                is DailyDiariesState.Loading -> {
                    LoadingScreen()
                }

                is DailyDiariesState.Success -> {
                    DailyDiary(
                        date = selectedDate,
                        dayOfWeek = initialDayOfWeek,
                        dailyDiary = dailyDiariesState.data,
                        onShowDiaryDeleteStateChange = onShowDiaryDeleteStateChange,
                    )
                }

                is DailyDiariesState.Error -> {
                    FailureScreen()
                }
            }
        }
    }
}
