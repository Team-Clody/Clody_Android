package com.sopt.clody.presentation.ui.diarylist.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sopt.clody.R
import com.sopt.clody.ui.theme.ClodyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryListTopAppBar(
    selectedYear: Int,
    selectedMonth: Int,
    showYearMonthPicker: () -> Unit,
    onClickCalendar: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Column {
        CenterAlignedTopAppBar(
            title = {
                Row(
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .clickable(
                            onClick = showYearMonthPicker,
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.diarylist_selected_year_month, selectedYear, selectedMonth),
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(ClodyTheme.colors.gray07)
        )
    }
}
