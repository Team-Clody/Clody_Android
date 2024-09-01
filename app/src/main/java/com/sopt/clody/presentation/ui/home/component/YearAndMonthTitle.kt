package com.sopt.clody.presentation.ui.home.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sopt.clody.R
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun YearAndMonthTitle(
    onShowYearMonthPickerStateChange: (Boolean) -> Unit,
    selectedYear: Int,
    selectedMonth: Int
) {
    val text = "${selectedYear}년 ${selectedMonth}월"

    Column {
        Row(
            modifier = Modifier.clickable(
                onClick = { onShowYearMonthPickerStateChange(true)},
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
            verticalAlignment = Alignment.CenterVertically
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
}

