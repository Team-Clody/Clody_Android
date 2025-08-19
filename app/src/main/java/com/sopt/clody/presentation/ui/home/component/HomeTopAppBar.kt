package com.sopt.clody.presentation.ui.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
fun HomeTopAppBar(
    selectedYear: String,
    selectedMonth: String,
    onClickDiaryList: () -> Unit,
    onClickYearMonth: () -> Unit,
    onClickSetting: () -> Unit,
) {
    CenterAlignedTopAppBar(
        title = {
            Box(
                modifier = Modifier.padding(start = 16.dp),
                contentAlignment = Alignment.Center,
            ) {
                Row(
                    modifier = Modifier.clickable(
                        onClick = onClickYearMonth,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.home_year_month_format, selectedYear, selectedMonth),
                        style = ClodyTheme.typography.head4,
                        color = ClodyTheme.colors.gray01,
                    )
                    Image(
                        painter = painterResource(id = R.drawable.ic_home_under_arrow),
                        contentDescription = "choose month",
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp),
                    )
                }
            }
        },
        navigationIcon = {
            IconButton(
                onClick = { onClickDiaryList() },
                modifier = Modifier.padding(start = 8.dp),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_home_list),
                    contentDescription = "go to list",
                )
            }
        },
        actions = {
            IconButton(
                onClick = { onClickSetting() },
                modifier = Modifier.padding(end = 8.dp),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_home_setting),
                    contentDescription = "go to setting",
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = ClodyTheme.colors.white,
            titleContentColor = ClodyTheme.colors.gray01,
            navigationIconContentColor = ClodyTheme.colors.gray01,
            actionIconContentColor = ClodyTheme.colors.gray01,
        ),
    )
}
