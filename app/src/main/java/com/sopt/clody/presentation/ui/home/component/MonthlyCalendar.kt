package com.sopt.clody.presentation.ui.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sopt.clody.R
import com.sopt.clody.domain.model.CalendarMonthlyInfo
import com.sopt.clody.domain.type.ReplyStatus
import com.sopt.clody.presentation.ui.type.DailyCloverType
import com.sopt.clody.presentation.utils.amplitude.AmplitudeConstraints
import com.sopt.clody.presentation.utils.amplitude.AmplitudeUtils
import com.sopt.clody.ui.theme.ClodyTheme
import kotlinx.datetime.DayOfWeek
import java.time.LocalDate
import java.time.format.TextStyle

@Composable
fun MonthlyCalendar(
    year: Int,
    month: Int,
    selectedDate: LocalDate,
    calendarDailyInfoList: List<CalendarMonthlyInfo.CalendarDailyInfo>,
    onClickDay: (Int) -> Unit,
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val cellWidth = remember(screenWidth) { (screenWidth - 40.dp) / 7 }
    val dayOfWeeks = remember { List(7) { i -> DayOfWeek.SUNDAY.plus(i.toLong()) } }

    val yearMonth = remember(year, month) { java.time.YearMonth.of(year, month) }
    val monthDates = remember(yearMonth) { (1..yearMonth.lengthOfMonth()).map { day -> yearMonth.atDay(day) } }
    val firstDayOfWeek = remember(yearMonth) { yearMonth.atDay(1).dayOfWeek }
    val emptyDays = remember(firstDayOfWeek) { firstDayOfWeek.value % 7 } // Sunday=0, Monday=1, ...
    val paddedDates: List<LocalDate?> = remember(monthDates, emptyDays) { List(emptyDays) { null } + monthDates }

    // 날짜 문자열(YYYY-MM-DD) → Info 매핑 (탐색 비용 절감)
    val infoByDate = remember(calendarDailyInfoList) { calendarDailyInfoList.associateBy { it.date } }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
    ) {
        // 요일 헤더
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
        ) {
            dayOfWeeks.forEach { dayOfWeek ->
                Box(
                    modifier = Modifier.width(cellWidth),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = dayOfWeek.getDisplayName(TextStyle.NARROW, LocalConfiguration.current.locales[0]),
                        color = ClodyTheme.colors.gray05,
                        style = ClodyTheme.typography.detail1Medium,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }

        // 월별 캘린더의 클로버
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth(),
            ) {
                paddedDates.chunked(7).forEach { weekDates ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        weekDates.forEach { dateOrNull ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(vertical = 2.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                if (dateOrNull != null) {
                                    val calendarDailyInfo = infoByDate[dateOrNull.toString()]
                                    if (calendarDailyInfo != null) {
                                        DailyClover(
                                            localDate = dateOrNull,
                                            calendarDailyInfo = calendarDailyInfo,
                                            onClickDay = {
                                                AmplitudeUtils.trackEvent(AmplitudeConstraints.HOME_CALENDAR_CLOVER)
                                                onClickDay(dateOrNull.dayOfMonth)
                                            },
                                            isSelected = dateOrNull == selectedDate,
                                            modifier = Modifier.fillMaxWidth(),
                                        )
                                    }
                                }
                            }
                        }
                        if (weekDates.size < 7) {
                            repeat(7 - weekDates.size) {
                                Box(
                                    modifier = Modifier
                                        .width(cellWidth)
                                        .padding(vertical = 2.dp),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DailyClover(
    localDate: LocalDate,
    calendarDailyInfo: CalendarMonthlyInfo.CalendarDailyInfo,
    onClickDay: () -> Unit,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClickDay() },
    ) {
        Box(
            modifier = Modifier
                .size(48.dp),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(id = DailyCloverType.getType(calendarDailyInfo).iconRes),
                contentDescription = "Diary clover icon",
            )
            if (calendarDailyInfo.replyStatus == ReplyStatus.READY_NOT_READ && calendarDailyInfo.diaryCount > 0) {
                Image(
                    painter = painterResource(id = R.drawable.ic_home_unread_reply),
                    contentDescription = "Unread replies icon",
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 0.dp, bottom = 8.dp)
                        .size(12.dp),
                )
            }
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(
                    if (isSelected) Color.Black else Color.Transparent,
                    shape = RoundedCornerShape(12.dp),
                )
                .padding(horizontal = 6.dp),
        ) {
            Text(
                text = localDate.dayOfMonth.toString(),
                style = ClodyTheme.typography.detail1SemiBold.copy(
                    color = when {
                        isSelected -> ClodyTheme.colors.white
                        localDate == LocalDate.now() -> ClodyTheme.colors.gray02
                        else -> ClodyTheme.colors.gray05
                    },
                ),
                textAlign = TextAlign.Center,
            )
        }
    }
}
