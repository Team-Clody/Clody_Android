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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.sopt.clody.ui.theme.ClodyTheme
import kotlinx.datetime.DayOfWeek
import java.time.format.TextStyle

@Composable
fun WeekHeader(
    modifier: Modifier = Modifier,
    itemWidth: Dp = (LocalConfiguration.current.screenWidthDp.dp - 40.dp) / 7,
) {
    val dayOfWeekArray = listOf(
        DayOfWeek.SUNDAY,
        DayOfWeek.MONDAY,
        DayOfWeek.TUESDAY,
        DayOfWeek.WEDNESDAY,
        DayOfWeek.THURSDAY,
        DayOfWeek.FRIDAY,
        DayOfWeek.SATURDAY,
    )

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier.fillMaxWidth(),
    ) {
        dayOfWeekArray.forEach { week ->
            Box(
                modifier = Modifier.width(itemWidth),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = week.getDisplayName(TextStyle.NARROW, LocalConfiguration.current.locales[0]),
                    color = ClodyTheme.colors.gray05,
                    style = ClodyTheme.typography.detail1Medium,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
