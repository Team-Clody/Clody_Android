package com.sopt.clody.presentation.ui.home.calendar.component

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

enum class WeekLang {
    KOREAN, ENGLISH
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

fun DayOfWeek.toEnglishShortLabel(): String {
    return when (this) {
        DayOfWeek.SUNDAY -> "Sun"
        DayOfWeek.MONDAY -> "Mon"
        DayOfWeek.TUESDAY -> "Tue"
        DayOfWeek.WEDNESDAY -> "Wed"
        DayOfWeek.THURSDAY -> "Thu"
        DayOfWeek.FRIDAY -> "Fri"
        DayOfWeek.SATURDAY -> "Sat"
    }
}

fun DayOfWeek.getLabel(lang: WeekLang): String {
    return when (lang) {
        WeekLang.KOREAN -> this.toKoreanShortLabel()
        WeekLang.ENGLISH -> this.toEnglishShortLabel()
    }
}

@Composable
fun WeekHeader(
    modifier: Modifier = Modifier,
    itemWidth: Dp = (LocalConfiguration.current.screenWidthDp.dp - 40.dp) / 7,
    lang: WeekLang = WeekLang.KOREAN,
) {
    val weekLabelArray = listOf(
        DayOfWeek.SUNDAY,
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY,
        DayOfWeek.SATURDAY,
    )

    val labels = weekLabelArray.map { it.getLabel(lang) }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth(),
    ) {
        labels.forEach { week ->
            Box(
                modifier = Modifier.width(itemWidth),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = week,
                    color = ClodyTheme.colors.gray05,
                    style = ClodyTheme.typography.detail1Medium,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WeekHeaderKoreanPreview() {
    WeekHeader(lang = WeekLang.KOREAN)
}

@Preview(showBackground = true)
@Composable
fun WeekHeaderEnglishPreview() {
    WeekHeader(lang = WeekLang.ENGLISH)
}
