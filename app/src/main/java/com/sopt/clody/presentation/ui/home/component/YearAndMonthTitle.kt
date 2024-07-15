package com.sopt.clody.presentation.ui.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sopt.clody.R
import com.sopt.clody.ui.theme.ClodyTheme
import java.time.LocalDate

@Composable
fun YearAndMonthTitle(
    onShowYearMonthPickerStateChange: (Boolean) -> Unit
) {
    val currentDate = LocalDate.now()
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedYear by remember { mutableStateOf(currentDate.year) }
    var selectedMonth by remember { mutableStateOf(currentDate.monthValue) }

    val text = "${selectedYear}년 ${selectedMonth}월"

    Column {
        Row(
            modifier = Modifier.clickable( onClick = { onShowYearMonthPickerStateChange(true)})
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
                    .clickable(onClick = { showDatePicker = true })
            )
        }
    }
}

