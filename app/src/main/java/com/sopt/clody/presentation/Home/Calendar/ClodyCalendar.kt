package com.sopt.clody.presentation.Home.Calendar

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
import com.sopt.clody.presentation.Home.Calendar.Component.DailyDiaryListItem
import com.sopt.clody.presentation.Home.Calendar.Component.MonthlyItem
import com.sopt.clody.presentation.Home.Calendar.Component.generateFakeDiaryData
import com.sopt.clody.ui.theme.ClodyTheme
import kotlinx.datetime.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun ClodyCalendar() {
    val currentMonth = YearMonth.now()
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedDayOfWeek by remember { mutableStateOf(DayOfWeek.MONDAY) } // Default value
    val fakeDiaryData = generateFakeDiaryData(currentMonth.year, currentMonth.monthValue)
    val dateList = generateCalendarDates(currentMonth.year, currentMonth.monthValue)
    var diaryTexts by remember { mutableStateOf(emptyList<String>()) }

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
        Spacer(modifier = Modifier.height(16.dp))
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
        "감사일기 내용 1",
        "감사일기 내용 2",
        "감사일기 내용 3"
    )
}

@Preview(showBackground = true)
@Composable
fun ClodyCalendarPreview() {
    ClodyCalendar()
}

