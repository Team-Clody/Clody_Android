package com.sopt.clody.presentation.ui.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
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

    TopAppBar(
        title = {
            Box(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        IconButton(onClick = { onClickDiaryList() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_home_list),
                                contentDescription = "go to list"
                            )
                        }
                    }
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        YearAndMonthTitle(
                            onShowYearMonthPickerStateChange,
                            selectedYear,
                            selectedMonth)
                    }
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        IconButton(
                            onClick = { onClickSetting() },
                            modifier = Modifier.padding(end = 10.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_home_setting),
                                contentDescription = "go to setting"
                            )
                        }
                    }
                }
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
