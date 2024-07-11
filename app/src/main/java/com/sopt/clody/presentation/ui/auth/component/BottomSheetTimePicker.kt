package com.sopt.clody.presentation.ui.auth.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sopt.clody.presentation.ui.component.timepicker.ClodyPicker
import com.sopt.clody.presentation.ui.component.timepicker.rememberPickerState
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun BottomSheetTimePicker() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        color = Color.White
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .wrapContentSize()
                .background(color = ClodyTheme.colors.white)

        ) {
            val amPmItems = remember { listOf("오전", "오후") }
            val hourItems = remember { (1..12).map { it.toString() } }
            val minuteItems = remember { listOf("00", "10", "20", "30", "40", "50") }

            val amPmPickerState = rememberPickerState()
            val hourPickerState = rememberPickerState()
            val minutePickerState = rememberPickerState()

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .height(35.dp)
                        .background(ClodyTheme.colors.gray08, shape = RoundedCornerShape(8.dp))
                )
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    ClodyPicker(
                        state = amPmPickerState,
                        items = amPmItems,
                        visibleItemsCount = 3,
                        infiniteScroll = false,
                        modifier = Modifier
                            .weight(1f),
                        textModifier = Modifier.padding(8.dp),

                        )
                    ClodyPicker(
                        state = hourPickerState,
                        items = hourItems,
                        visibleItemsCount = 5,
                        infiniteScroll = true,
                        modifier = Modifier
                            .weight(1f),
                        textModifier = Modifier.padding(8.dp),

                        )
                    ClodyPicker(
                        state = minutePickerState,
                        items = minuteItems,
                        visibleItemsCount = 5,
                        infiniteScroll = true,
                        modifier = Modifier
                            .weight(1f),
                        textModifier = Modifier.padding(8.dp),
                    )
                }

            }

            Text(
                text = "선택시간: ${amPmPickerState.selectedItem} ${hourPickerState.selectedItem}:${minutePickerState.selectedItem}",
                modifier = Modifier.padding(vertical = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BottomSheetPickerPreview() {
    BottomSheetTimePicker()
}
