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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sopt.clody.R
import com.sopt.clody.domain.model.CalendarMonthlyInfo
import com.sopt.clody.domain.model.DailyDiaryInfo
import com.sopt.clody.ui.theme.ClodyTheme
import java.time.LocalDate

@Composable
fun MonthlyCalendarAndDailyDiary(
    year: Int,
    month: Int,
    selectedDate: LocalDate,
    calendarMonthlyInfo: CalendarMonthlyInfo,
    onClickDay: (Int) -> Unit,
    selectedDailyInfo: DailyDiaryInfo,
    onClickDiaryDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(ClodyTheme.colors.white),
    ) {
        // 클로버 총 갯수
        Text(
            text = stringResource(R.string.home_total_clover, calendarMonthlyInfo.totalCloverCount),
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
                year = year,
                month = month,
                selectedDate = selectedDate,
                calendarDailyInfoList = calendarMonthlyInfo.calendarDailyInfoList,
                onClickDay = onClickDay,
            )

            HorizontalDivider(
                color = ClodyTheme.colors.gray08,
                thickness = 6.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp, bottom = 20.dp),
            )

            DailyDiary(
                selectedDate = selectedDate,
                selectedDailyInfo = selectedDailyInfo,
                onClickDiaryDelete = onClickDiaryDelete,
            )
        }
    }
}
