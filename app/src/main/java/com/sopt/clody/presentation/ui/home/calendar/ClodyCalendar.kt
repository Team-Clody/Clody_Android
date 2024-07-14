package com.sopt.clody.presentation.ui.home.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sopt.clody.domain.model.generateCalendarDates
import com.sopt.clody.presentation.ui.home.calendar.Component.DailyDiaryListItem
import com.sopt.clody.presentation.ui.home.calendar.Component.MonthlyItem
import com.sopt.clody.presentation.ui.home.calendar.Component.generateFakeDiaryData
import com.sopt.clody.ui.theme.ClodyTheme
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun ClodyCalendar() {
    val currentMonth = YearMonth.now()
    val today = LocalDate.now()
    val initialDayOfWeek = today.dayOfWeek
    val fakeDiaryData = generateFakeDiaryData(currentMonth.year, currentMonth.monthValue)
    val dateList = generateCalendarDates(currentMonth.year, currentMonth.monthValue)
    var selectedDate by remember { mutableStateOf(today) }
    var selectedDayOfWeek by remember { mutableStateOf(initialDayOfWeek) }
    var diaryTexts by remember { mutableStateOf(fetchDiaryTextsForDate(today)) }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        MonthlyItem(
            dateList = dateList,
            selectedDate = selectedDate,
            onDayClick = { date ->
                selectedDate = date
                selectedDayOfWeek = date.dayOfWeek
                diaryTexts = fetchDiaryTextsForDate(date)
            },
            getDiaryDataForDate = { date ->
                val dayIndex = date.dayOfMonth - 1
                if (dayIndex < fakeDiaryData.size) {
                    fakeDiaryData[dayIndex]
                } else {
                    null
                }
            },
        )
        Spacer(modifier = Modifier.height(10.dp))
        HorizontalDivider()
        DailyDiaryListItem(
            date = selectedDate,
            dayOfWeek = selectedDayOfWeek,
            diaryTexts = diaryTexts
        )
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

fun fetchDiaryTextsForDate(date: LocalDate): List<String> {
    return listOf(
        "오늘의 일기 오늘은 의진이가 가정방문을 햇다 의진이한테 많은 걸 물어볼 수 있었다. 그래서 좋았다",
        "오늘의 일기 오늘은 의진이가 가정방문을 햇다 의진이한테 많은 걸 물어볼 수 있었다. 그래서 좋았다",
        "오늘의 일기 오늘은 의진이가 가정방문을 햇다 의진이한테 많은 걸 물어볼 수 있었다. 그래서 좋았다",
        "오늘의 일기 오늘은 의진이가 가정방문을 햇다 의진이한테 많은 걸 물어볼 수 있었다. 그래서 좋았다",
    )
}

@Preview(showBackground = true)
@Composable
fun ClodyCalendarPreview() {
    ClodyCalendar()
}

