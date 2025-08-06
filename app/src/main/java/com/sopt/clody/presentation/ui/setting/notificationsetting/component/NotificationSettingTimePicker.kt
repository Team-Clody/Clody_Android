package com.sopt.clody.presentation.ui.setting.notificationsetting.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sopt.clody.R
import com.sopt.clody.presentation.ui.component.button.ClodyButton
import com.sopt.clody.presentation.ui.component.timepicker.ClodyPicker
import com.sopt.clody.presentation.ui.component.timepicker.rememberPickerState
import com.sopt.clody.presentation.utils.extension.TimePeriod
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun NotificationSettingTimePicker(
    onDismissRequest: () -> Unit,
    onConfirm: (TimePeriod, String, String) -> Unit,
) {
    val amPmItemsLabel = TimePeriod.entries.map { it.getLabel() }
    val amPmItems = remember { amPmItemsLabel }
    val hourItems = remember { (1..12).map { it.toString() } }
    val minuteItems = remember { listOf("00", "10", "20", "30", "40", "50") }

    val amPmPickerState = rememberPickerState()
    val hourPickerState = rememberPickerState()
    val minutePickerState = rememberPickerState()

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        color = ClodyTheme.colors.white,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .wrapContentSize()
                .background(color = ClodyTheme.colors.white)
                .padding(horizontal = 24.dp),

        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 30.dp),
            ) {
                Text(
                    text = stringResource(R.string.bottom_sheet_notification_time_change_title),
                    style = ClodyTheme.typography.head4,
                    color = ClodyTheme.colors.gray01,
                    modifier = Modifier.align(Alignment.Center),
                )

                IconButton(
                    onClick = onDismissRequest,
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.CenterEnd),
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_picker_dismiss),
                        contentDescription = null,
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .height(35.dp)
                        .background(ClodyTheme.colors.gray08, shape = RoundedCornerShape(8.dp)),
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Spacer(Modifier.weight(1f))
                    ClodyPicker(
                        state = amPmPickerState,
                        items = amPmItems,
                        startIndex = 1,
                        visibleItemsCount = 3,
                        infiniteScroll = false,
                        modifier = Modifier
                            .weight(1f),
                        textModifier = Modifier.padding(8.dp),
                    )
                    ClodyPicker(
                        state = hourPickerState,
                        items = hourItems,
                        startIndex = 8,
                        visibleItemsCount = 5,
                        infiniteScroll = true,
                        modifier = Modifier
                            .weight(1f),
                        textModifier = Modifier.padding(8.dp),
                    )
                    ClodyPicker(
                        state = minutePickerState,
                        items = minuteItems,
                        startIndex = 3,
                        visibleItemsCount = 5,
                        infiniteScroll = true,
                        modifier = Modifier
                            .weight(1f),
                        textModifier = Modifier.padding(8.dp),
                    )
                    Spacer(Modifier.weight(1f))
                }
            }
            ClodyButton(
                onClick = {
                    val selectedPeriod = if (amPmPickerState.selectedItem == "오전") TimePeriod.AM else TimePeriod.PM
                    onConfirm(selectedPeriod, hourPickerState.selectedItem, minutePickerState.selectedItem)
                },
                text = stringResource(R.string.bottom_sheet_notification_time_change_confirm),
                enabled = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp, bottom = 28.dp),
            )
        }
    }
}
