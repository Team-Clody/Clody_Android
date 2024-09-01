package com.sopt.clody.presentation.ui.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sopt.clody.R
import com.sopt.clody.ui.theme.ClodyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(
    onClickDiaryList: () -> Unit,
    onClickSetting: () -> Unit,
    onShowYearMonthPickerStateChange: (Boolean) -> Unit,
    selectedYear: Int,
    selectedMonth: Int,
) {

    CenterAlignedTopAppBar(
        title = {
            Box(
                modifier = Modifier
                    .padding(start = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                YearAndMonthTitle(
                    onShowYearMonthPickerStateChange, selectedYear, selectedMonth
                )
            }
        },
        navigationIcon = {
                IconButton(
                    onClick = { onClickDiaryList() },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_home_list),
                        contentDescription = "go to list"
                    )
                }
        },
        actions = {
                IconButton(
                    onClick = { onClickSetting() },
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_home_setting),
                        contentDescription = "go to setting"
                    )
                }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = ClodyTheme.colors.white,
            titleContentColor = ClodyTheme.colors.gray01,
            navigationIconContentColor = ClodyTheme.colors.gray01,
            actionIconContentColor = ClodyTheme.colors.gray01
        )
    )
}
