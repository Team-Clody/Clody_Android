package com.sopt.clody.presentation.ui.home.calendar.Component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sopt.clody.ui.theme.ClodyTheme
import kotlinx.datetime.DayOfWeek

@Composable
fun WeekHeader(modifier: Modifier = Modifier, itemWidth: Dp) {
    val itemWidth = (LocalConfiguration.current.screenWidthDp.dp - 40.dp) / 7
    Row(
        horizontalArrangement = Arrangement.SpaceBetween, // 고르게 분포되도록 설정
        modifier = modifier.fillMaxWidth()
    ) {
        val weekLabelArray = listOf(
            DayOfWeek.SUNDAY,
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY,
            DayOfWeek.SATURDAY
        )

        val koreanWeekLabels = weekLabelArray.map { it.toKoreanShortLabel() }

        koreanWeekLabels.forEach { week ->
            Box(
                modifier = Modifier.width(itemWidth),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = week,
                    style = ClodyTheme.typography.body3SemiBold,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

fun DayOfWeek.toKoreanShortLabel(): String {
    return when (this) {
        DayOfWeek.SUNDAY -> "일"
        DayOfWeek.MONDAY -> "월"
        DayOfWeek.TUESDAY -> "화"
        DayOfWeek.WEDNESDAY -> "수"
        DayOfWeek.THURSDAY -> "목"
        DayOfWeek.FRIDAY -> "금"
        DayOfWeek.SATURDAY -> "토"
    }
}

@Composable
@Preview(showBackground = true)
fun WeekHeaderPreview() {
}
