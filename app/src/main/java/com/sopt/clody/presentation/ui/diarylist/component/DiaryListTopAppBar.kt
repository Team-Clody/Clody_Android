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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.sopt.clody.R
import com.sopt.clody.ui.theme.ClodyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiaryListTopAppBar(name: String) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    CenterAlignedTopAppBar(
        // 표준 규격 : 64.dp 적용할 것
        title = {
            Row(
                modifier = Modifier
                    .clickable { /* 연,월 선택 */ },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = name,
                    style = ClodyTheme.typography.head4
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "choose year and month"
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = { /* 캘린더뷰로 이동 */ }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_listview_calendar),
                    contentDescription = "go to calenderView"
                )
            }
        },
        scrollBehavior = scrollBehavior,
    )
}
