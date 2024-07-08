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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sopt.clody.R
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        HomeHeader()
        CalendarScreen()
    }
}

@Composable
fun HomeHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
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
    val text = "2024년 6월"
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
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        CloverCount()
        ClodyCalendar()
    }
}

@Composable
fun CloverCount() {
    val text = "클러버 23개"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 22.dp)
    ) {
        Text(
            text = text,
            style = ClodyTheme.typography.detail1SemiBold,
            color = ClodyTheme.colors.darkGreen,
            modifier = Modifier
                .border(9.dp, ClodyTheme.colors.lightGreen, shape = RoundedCornerShape(4.dp))
                .background(ClodyTheme.colors.lightGreen, shape = RoundedCornerShape(4.dp))
                .padding(12.dp),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ClodyCalendar() {

}

@Preview(showBackground = true)
@Composable
fun CalendarScreenPreview() {
    HomeScreen()
}
