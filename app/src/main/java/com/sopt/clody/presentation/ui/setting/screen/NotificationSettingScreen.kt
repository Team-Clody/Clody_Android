package com.sopt.clody.presentation.ui.setting.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
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
    NotificationSettingScreen(
        onBackClick = { navigator.navigateBack() }
    )
}

@Composable
fun NotificationSettingScreen(onBackClick: () -> Unit) {
    var showNotificationTimePicker by remember { mutableStateOf(false) }
    var selectedTime by remember { mutableStateOf("오후 9시 30분") }

    Scaffold(
        topBar = { SettingTopAppBar(stringResource(R.string.notification_setting_title), onBackClick) },
        containerColor = ClodyTheme.colors.white,
        contentColor = ClodyTheme.colors.white
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
                showBottomSheetStateChange = { newState -> showNotificationTimePicker = newState }
            )
            Spacer(modifier = Modifier.height(32.dp))
            NotificationSettingSwitch(
                title = stringResource(R.string.notification_setting_reply_diary),
                checkedState = remember { mutableStateOf(true) }
            )
        }
    }
    if (showNotificationTimePicker) {
        ClodyPopupBottomSheet(onDismissRequest = { showNotificationTimePicker = false }) {
            NotificationSettingTimePicker(
                onDismissRequest = { showNotificationTimePicker = false },
                onTimeSelected = { newTime ->
                    selectedTime = newTime
                    showNotificationTimePicker = false
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationSettingScreenPreview() {
    NotificationSettingScreen(onBackClick = {})
}
