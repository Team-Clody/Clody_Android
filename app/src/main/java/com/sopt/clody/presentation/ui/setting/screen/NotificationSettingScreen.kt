package com.sopt.clody.presentation.ui.setting.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sopt.clody.R
import com.sopt.clody.presentation.ui.component.popup.ClodyPopupBottomSheet
import com.sopt.clody.presentation.ui.setting.component.NotificationSettingSwitch
import com.sopt.clody.presentation.ui.setting.component.NotificationSettingTime
import com.sopt.clody.presentation.ui.setting.component.NotificationSettingTimePicker
import com.sopt.clody.presentation.ui.setting.component.SettingTopAppBar
import com.sopt.clody.presentation.ui.setting.navigation.SettingNavigator
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun NotificationSettingRoute(
    navigator: SettingNavigator
) {
    var selectedTime by remember { mutableStateOf("오후 9시 30분") }
    var showNotificationTimePicker by remember { mutableStateOf(false) }

    NotificationSettingScreen(
        selectedTime = selectedTime,
        updateSelectedTime = { newSelectedTime -> selectedTime = newSelectedTime },
        showNotificationTimePicker = showNotificationTimePicker,
        updateNotificationTimePicker = { state -> showNotificationTimePicker = state },
        onClickBack = { navigator.navigateBack() }
    )
}

@Composable
fun NotificationSettingScreen(
    selectedTime: String,
    updateSelectedTime: (String) -> Unit,
    showNotificationTimePicker: Boolean,
    updateNotificationTimePicker: (Boolean) -> Unit,
    onClickBack: () -> Unit
) {
    Scaffold(
        topBar = {
            SettingTopAppBar(
                title = stringResource(R.string.notification_setting_title),
                onClickBack = onClickBack
            )
        },
        containerColor = ClodyTheme.colors.white,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            NotificationSettingSwitch(
                title = stringResource(R.string.notification_setting_write_diary),
                checkedState = remember { mutableStateOf(true) }
            )
            Spacer(modifier = Modifier.height(32.dp))
            NotificationSettingTime(
                selectedTime = selectedTime,
                updateNotificationTimePicker = updateNotificationTimePicker
            )
            Spacer(modifier = Modifier.height(32.dp))
            NotificationSettingSwitch(
                title = stringResource(R.string.notification_setting_reply_diary),
                checkedState = remember { mutableStateOf(true) }
            )
        }
    }
    if (showNotificationTimePicker) {
        ClodyPopupBottomSheet(onDismissRequest = { updateNotificationTimePicker(false) }) {
            NotificationSettingTimePicker(
                onTimeSelected = { newSelectedTime ->
                    updateSelectedTime(newSelectedTime)
                    updateNotificationTimePicker(false)
                },
                onDismissRequest = { updateNotificationTimePicker(false) }
            )
        }
    }
}
