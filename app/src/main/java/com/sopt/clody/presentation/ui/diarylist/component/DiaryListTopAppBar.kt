package com.sopt.clody.presentation.ui.diarylist.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sopt.clody.R
import com.sopt.clody.ui.theme.ClodyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryListTopAppBar(
    onClickCalendar: () -> Unit,
    selectedYear: Int,
    selectedMonth: Int,
    onShowYearMonthPickerStateChange: (Boolean) -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    CenterAlignedTopAppBar(
        title = {
            Row(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .clickable(onClick = { onShowYearMonthPickerStateChange(true) }),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${selectedYear}년 ${selectedMonth}월",
                    color = ClodyTheme.colors.gray01,
                    style = ClodyTheme.typography.head4
                )
                Image(
                    painter = painterResource(id = R.drawable.ic_listview_arrow_down),
                    contentDescription = null
                )
            }
        },
        navigationIcon = {
            IconButton(
                onClick = onClickCalendar,
                modifier = Modifier.padding(start = 8.dp)
            ) {
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
