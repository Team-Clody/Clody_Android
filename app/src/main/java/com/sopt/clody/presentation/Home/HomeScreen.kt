package com.sopt.clody.presentation.Home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sopt.clody.R

@Composable
fun HomeScreen() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(scrollState)
    ) {
        HomeHeader()
    }
}

@Composable
fun HomeHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_home_list),
                contentDescription = "go to list",
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            //TODO 월 설정 스피너 들어갈 예정

            Image(
                painter = painterResource(id = R.drawable.ic_home_setting),
                contentDescription = "go to setting",
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 20.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun CalendarScreenPreview() {
    HomeScreen()
}
