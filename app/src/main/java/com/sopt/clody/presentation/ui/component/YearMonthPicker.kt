package com.sopt.clody.presentation.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sopt.clody.R
import com.sopt.clody.presentation.ui.component.button.ClodyButton
import com.sopt.clody.presentation.ui.component.timepicker.ClodyPicker
import com.sopt.clody.presentation.ui.component.timepicker.rememberPickerState
import com.sopt.clody.ui.theme.ClodyTheme
import java.time.LocalDate

@Composable
fun YearMonthPicker(
    onDismissRequest: () -> Unit,
) {
    val currentDate = LocalDate.now()
    var selectedYear by remember { mutableStateOf(currentDate.year) }
    var selectedMonth by remember { mutableStateOf(currentDate.monthValue) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        color = ClodyTheme.colors.white
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .wrapContentSize()
                .background(color = ClodyTheme.colors.white)
                .padding(horizontal = 24.dp)

        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 30.dp)
            ) {
                Text(
                    text = "다른 날짜 보기",
                    style = ClodyTheme.typography.body2SemiBold,
                    color = ClodyTheme.colors.gray01,
                    modifier = Modifier.align(Alignment.Center)
                )

                IconButton(
                    onClick = onDismissRequest,
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterEnd)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_picker_dismiss),
                        contentDescription = null,
                    )
                }
            }

            val yearItems = remember { (2000..2030).map { "${it}년" } }
            val monthItems = remember { (1..12).map { "${it}월" } }

            val yearPickerState = rememberPickerState()
            val monthPickerState = rememberPickerState()

            val startYearIndex = (yearItems.indexOf("${currentDate.year}년") - 2).coerceAtLeast(0)
            val startMonthIndex = (monthItems.indexOf("${currentDate.monthValue}월") - 2).coerceAtLeast(0)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .height(35.dp)
                        .background(ClodyTheme.colors.gray08, shape = RoundedCornerShape(8.dp))
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ClodyPicker(
                        state = yearPickerState,
                        items = yearItems,
                        startIndex = startYearIndex,
                        visibleItemsCount = 5,
                        infiniteScroll = false,
                        modifier = Modifier
                            .weight(1f),
                        textModifier = Modifier.padding(8.dp),
                    )
                    ClodyPicker(
                        state = monthPickerState,
                        items = monthItems,
                        startIndex = startMonthIndex,
                        visibleItemsCount = 5,
                        infiniteScroll = false,
                        modifier = Modifier
                            .weight(1f),
                        textModifier = Modifier.padding(8.dp),
                    )
                }
            }
            ClodyButton(
                onClick = {
                    onDismissRequest()
                },
                text = "완료",
                enabled = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp, bottom = 28.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun YearMonthPickerPreview() {

}
