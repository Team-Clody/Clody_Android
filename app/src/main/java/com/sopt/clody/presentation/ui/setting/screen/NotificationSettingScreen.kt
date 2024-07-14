package com.sopt.clody.presentation.ui.setting.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sopt.clody.R
import com.sopt.clody.presentation.ui.setting.navigation.SettingNavigator
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun NotificationSettingRoute(
    navigator: SettingNavigator
) {
    NotificationSettingScreen(
        onBackClick = { navigator.navigateToBack() }
    )
}

@Composable
fun NotificationSettingScreen(onBackClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {

        Spacer(modifier = Modifier.height(20.dp))
        NotificationSettingHeader()
        Spacer(modifier = Modifier.height(40.dp))
        DiaryWrittingAlarmSetting()
        Spacer(modifier = Modifier.height(12.dp))
        AlarmTime()
        Spacer(modifier = Modifier.height(12.dp))
        ReplyAlarmSetting()

    }
}

@Composable
fun NotificationSettingHeader() {
    Row(
        modifier = Modifier.padding(horizontal = 12.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            painterResource(id = R.drawable.ic_setting_back),
            contentDescription = "go to back",
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "알림 설정",
            style = ClodyTheme.typography.head4,
            color = ClodyTheme.colors.gray01

        )
        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
fun DiaryWrittingAlarmSetting() {
    val checkedState = remember { mutableStateOf(true) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "일기 작성 알림 받기",
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
fun AlarmTime(){
    Row(
        modifier = Modifier
            .fillMaxWidth()
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
            text = "오후 9시 30분",
            style = ClodyTheme.typography.body3Medium,
            color = ClodyTheme.colors.gray05
        )
        Image(
            painterResource(id = R.drawable.ic_setting_next),
            contentDescription = "pick the alarm time",

        )
    }
}

@Composable
fun ReplyAlarmSetting(){
    val checkedState = remember { mutableStateOf(true) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "답장 도착 알림 받기",
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

@Preview(showBackground = true)
@Composable
fun NotificationSettingScreenPreview() {
    NotificationSettingScreen(onBackClick = {})
}
