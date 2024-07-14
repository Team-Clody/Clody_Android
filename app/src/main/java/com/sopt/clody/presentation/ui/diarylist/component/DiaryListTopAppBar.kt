package com.sopt.clody.presentation.ui.diarylist.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.sopt.clody.R
import com.sopt.clody.ui.theme.ClodyTheme
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryListTopAppBar(
    onClickCalendar: () -> Unit,
    onShowYearMonthPickerStateChange: (Boolean) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val currentDate = LocalDate.now()
    var selectedYear by remember { mutableStateOf(currentDate.year) }
    var selectedMonth by remember { mutableStateOf(currentDate.monthValue) }

    CenterAlignedTopAppBar(
        title = {
            Row(
                modifier = Modifier
                    .clickable(onClick = { onShowYearMonthPickerStateChange(true) }),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${selectedYear}년 ${selectedMonth}월",
                    color = ClodyTheme.colors.gray01,
                    style = ClodyTheme.typography.head4
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "choose year and month"
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onClickCalendar) {
                Image(
                    painter = painterResource(id = R.drawable.ic_listview_calendar),
                    contentDescription = "go to calenderView"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(ClodyTheme.colors.white),
        scrollBehavior = scrollBehavior,
    )
}
