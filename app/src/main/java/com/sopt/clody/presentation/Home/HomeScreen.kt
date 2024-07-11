package com.sopt.clody.presentation.Home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sopt.clody.R
import com.sopt.clody.domain.model.DiaryData
import com.sopt.clody.domain.model.generateCalendarDates
import com.sopt.clody.presentation.Home.Calendar.ClodyCalendar
import com.sopt.clody.presentation.Home.Calendar.Component.DailyDiaryListItem
import com.sopt.clody.presentation.Home.Calendar.Component.DayItem
import com.sopt.clody.presentation.Home.Calendar.Component.MonthlyItem
import com.sopt.clody.presentation.Home.Calendar.Component.WeekHeader
import com.sopt.clody.presentation.Home.Calendar.Component.generateFakeDiaryData
import com.sopt.clody.ui.theme.ClodyTheme
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HomeHeader()
        CalendarScreen()
    }
}

@Composable
fun HomeHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_home_list),
            contentDescription = "go to list",
            modifier = Modifier
        )
        Spacer(modifier = Modifier.weight(1f))
        YearAndMonthTitle()
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(id = R.drawable.ic_home_setting),
            contentDescription = "go to setting",
            modifier = Modifier
        )
    }
}

@Composable
fun YearAndMonthTitle() {
    val text = "2024년 7월"
    Row(
        modifier = Modifier
    ) {
        Text(
            text = text,
            style = ClodyTheme.typography.head4,
            color = ClodyTheme.colors.gray01
        )
        Image(
            painter = painterResource(id = R.drawable.ic_home_under_arrow),
            contentDescription = "choose month",
            modifier = Modifier
                .padding(horizontal = 6.dp, vertical = 6.dp)

        )
    }
}

@Composable
fun CalendarScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        CloverCount()
        Spacer(modifier = Modifier.height(20.dp))
        ClodyCalendarScreen()
    }
}

@Composable
fun CloverCount() {
    val text = "클러버 23개"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp, end = 20.dp),
        contentAlignment = Alignment.TopEnd
    ) {
        Text(
            text = text,
            style = ClodyTheme.typography.detail1SemiBold,
            color = ClodyTheme.colors.darkGreen,
            modifier = Modifier
                .border(9.dp, ClodyTheme.colors.lightGreen, shape = RoundedCornerShape(9.dp))
                .background(ClodyTheme.colors.lightGreen, shape = RoundedCornerShape(9.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ClodyCalendarScreen(
) {
    ClodyCalendar()
}

@Preview(showBackground = true)
@Composable
fun CalendarScreenPreview() {
    HomeScreen()
}
