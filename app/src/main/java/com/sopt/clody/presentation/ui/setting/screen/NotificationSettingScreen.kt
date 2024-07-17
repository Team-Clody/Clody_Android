package com.sopt.clody.presentation.ui.setting.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import com.sopt.clody.presentation.ui.setting.component.SettingBottomSheetTimePicker
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
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedTime by remember { mutableStateOf("오후 9시 30분") }

    Scaffold(
        topBar = { SettingTopAppBar(stringResource(R.string.setting_notification_setting), onBackClick) },
        containerColor = ClodyTheme.colors.white,
        contentColor = ClodyTheme.colors.white
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(top = 20.dp)
        ) {
            NotificationSettingSwitch(
                title = "일기 작성 알림 받기",
                checkedState = remember { mutableStateOf(true) }
            )
            Spacer(modifier = Modifier.height(32.dp))
            AlarmTimeSetting(
                showBottomSheetStateChange = { newState -> showBottomSheet = newState}
            )
            Spacer(modifier = Modifier.height(32.dp))
            NotificationSettingSwitch(
                title = "답장 도착 알림 받기",
                checkedState = remember { mutableStateOf(true) }
            )
        }
    }
    if (showBottomSheet) {
        ClodyPopupBottomSheet(onDismissRequest = { showBottomSheet = false }) {
            SettingBottomSheetTimePicker(
                onDismissRequest = { showBottomSheet = false },
                onTimeSelected = { newTime ->
                    selectedTime = newTime
                    showBottomSheet = false
                }
            )
        }
    }
}

@Composable
fun NotificationSettingSwitch(title: String, checkedState: MutableState<Boolean>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = ClodyTheme.typography.body1Medium,
            color = ClodyTheme.colors.gray03
        )
        Spacer(modifier = Modifier.weight(1f))
        Switch(
            checked = checkedState.value,
            onCheckedChange = { checkedState.value = it },
            colors = SwitchDefaults.colors(
                checkedThumbColor = ClodyTheme.colors.white,
                checkedTrackColor = ClodyTheme.colors.darkYellow.copy(alpha = 0.5f),
                uncheckedThumbColor = ClodyTheme.colors.white,
                uncheckedTrackColor = ClodyTheme.colors.gray06,
                uncheckedBorderColor = ClodyTheme.colors.gray06
            )
        )
    }
}

@Composable
fun AlarmTimeSetting(showBottomSheetStateChange: (Boolean) -> Unit) {
    var selectedTime by remember { mutableStateOf("오후 9시 30분") }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "알림 시간",
            style = ClodyTheme.typography.body1Medium,
            color = ClodyTheme.colors.gray03
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = selectedTime,
            style = ClodyTheme.typography.body3Medium,
            color = ClodyTheme.colors.gray05,
        )
        Image(
            painterResource(id = R.drawable.ic_setting_next),
            contentDescription = "pick the alarm time",
            modifier = Modifier.clickable { showBottomSheetStateChange(true) }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationSettingScreenPreview() {
    NotificationSettingScreen(onBackClick = {})
}
