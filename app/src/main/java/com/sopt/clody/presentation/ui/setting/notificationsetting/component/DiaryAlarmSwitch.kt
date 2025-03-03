package com.sopt.clody.presentation.ui.setting.notificationsetting.component

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sopt.clody.R
import com.sopt.clody.data.remote.dto.response.NotificationInfoResponseDto
import com.sopt.clody.presentation.ui.setting.notificationsetting.screen.NotificationSettingViewModel
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun DiaryAlarmSwitch(
    notificationSettingViewModel: NotificationSettingViewModel,
    context: Context,
    title: String,
    notificationInfo: NotificationInfoResponseDto,
    checkedState: MutableState<Boolean>
) {
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
            onCheckedChange = {
                checkedState.value = it
                notificationSettingViewModel.changeDiaryAlarm(context, notificationInfo, checkedState.value)
            },
            modifier = Modifier.scale(0.8f),
            thumbContent = {
                Image(
                    painter = painterResource(id = R.drawable.ic_notification_setting_switch_thumb),
                    contentDescription = null
                )
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = ClodyTheme.colors.white,
                checkedTrackColor = ClodyTheme.colors.mainYellow,
                uncheckedThumbColor = ClodyTheme.colors.white,
                uncheckedTrackColor = ClodyTheme.colors.gray06,
                uncheckedBorderColor = ClodyTheme.colors.gray06
            ),
        )
    }
}
