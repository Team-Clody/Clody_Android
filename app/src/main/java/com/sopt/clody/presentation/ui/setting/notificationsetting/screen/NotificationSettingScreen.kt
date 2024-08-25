package com.sopt.clody.presentation.ui.setting.notificationsetting.screen

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sopt.clody.R
import com.sopt.clody.data.remote.dto.response.ResponseNotificationInfoDto
import com.sopt.clody.presentation.ui.component.FailureScreen
import com.sopt.clody.presentation.ui.component.LoadingScreen
import com.sopt.clody.presentation.ui.component.popup.ClodyPopupBottomSheet
import com.sopt.clody.presentation.ui.component.toast.ClodyToastMessage
import com.sopt.clody.presentation.ui.setting.component.SettingTopAppBar
import com.sopt.clody.presentation.ui.setting.navigation.SettingNavigator
import com.sopt.clody.presentation.ui.setting.notificationsetting.component.DiaryAlarmSwitch
import com.sopt.clody.presentation.ui.setting.notificationsetting.component.NotificationSettingTime
import com.sopt.clody.presentation.ui.setting.notificationsetting.component.NotificationSettingTimePicker
import com.sopt.clody.presentation.ui.setting.notificationsetting.component.ReplyAlarmSwitch
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun NotificationSettingRoute(
    navigator: SettingNavigator,
    notificationSettingViewModel: NotificationSettingViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val notificationInfoState by notificationSettingViewModel.notificationInfoState.collectAsState()
    val diaryAlarmChangeState by notificationSettingViewModel.diaryAlarmChangeState.collectAsState()
    val notificationTimeChangeState by notificationSettingViewModel.notificationTimeChangeState.collectAsState()
    val replyAlarmChangeState by notificationSettingViewModel.replyAlarmChangeState.collectAsState()
    var showNotificationTimePicker by remember { mutableStateOf(false) }
    var notificationInfo by remember { mutableStateOf<ResponseNotificationInfoDto?>(null) }

    LaunchedEffect(Unit) {
        notificationSettingViewModel.getNotificationInfo()
    }

    LaunchedEffect(diaryAlarmChangeState, notificationTimeChangeState, replyAlarmChangeState) {
        val isSuccess = diaryAlarmChangeState is DiaryAlarmChangeState.Success ||
                notificationTimeChangeState is NotificationTimeChangeState.Success ||
                replyAlarmChangeState is ReplyAlarmChangeState.Success

        if (isSuccess) {
            notificationSettingViewModel.getNotificationInfo()
        }
    }

    NotificationSettingScreen(
        notificationSettingViewModel = notificationSettingViewModel,
        context = context,
        notificationInfoState = notificationInfoState,
        notificationInfo = notificationInfo,
        notificationTimeChangeState = notificationTimeChangeState,
        showNotificationTimePicker = showNotificationTimePicker,
        updateNotificationTimePicker = { state -> showNotificationTimePicker = state },
        onClickBack = { navigator.navigateBack() },
        onNotificationInfoAvailable = { notificationInfo = it }
    )
}

@Composable
fun NotificationSettingScreen(
    notificationSettingViewModel: NotificationSettingViewModel,
    context: Context,
    notificationInfoState: NotificationInfoState,
    notificationInfo: ResponseNotificationInfoDto?,
    notificationTimeChangeState: NotificationTimeChangeState,
    showNotificationTimePicker: Boolean,
    updateNotificationTimePicker: (Boolean) -> Unit,
    onClickBack: () -> Unit,
    onNotificationInfoAvailable: (ResponseNotificationInfoDto) -> Unit
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
        when (notificationInfoState) {
            is NotificationInfoState.Idle -> {}

            is NotificationInfoState.Loading -> {
                LoadingScreen()
            }

            is NotificationInfoState.Success -> {
                val notificationInfo = notificationInfoState.data
                onNotificationInfoAvailable(notificationInfo)
                val notificationTime = notificationSettingViewModel.convertTo12HourFormat(notificationInfo.time)
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    DiaryAlarmSwitch(
                        notificationSettingViewModel = notificationSettingViewModel,
                        context = context,
                        title = stringResource(R.string.notification_setting_write_diary),
                        notificationInfo = notificationInfo,
                        checkedState = remember { mutableStateOf(notificationInfo.isDiaryAlarm) }
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    NotificationSettingTime(
                        selectedTime = notificationTime,
                        updateNotificationTimePicker = updateNotificationTimePicker
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    ReplyAlarmSwitch(
                        notificationSettingViewModel = notificationSettingViewModel,
                        context = context,
                        title = stringResource(R.string.notification_setting_reply_diary),
                        notificationInfo = notificationInfo,
                        checkedState = remember { mutableStateOf(notificationInfo.isReplyAlarm) }
                    )
                }
            }

            is NotificationInfoState.Failure -> {
                FailureScreen()
            }
        }
    }

    if (showNotificationTimePicker) {
        ClodyPopupBottomSheet(onDismissRequest = { updateNotificationTimePicker(false) }) {
            NotificationSettingTimePicker(
                notificationSettingViewModel = notificationSettingViewModel,
                onTimeSelected = { newNotificationTime ->
                    notificationInfo?.let {
                        notificationSettingViewModel.changeNotificationTime(context, it, newNotificationTime)
                    }
                    updateNotificationTimePicker(false)
                },
                onDismissRequest = { updateNotificationTimePicker(false) }
            )
        }
    }

    if (notificationTimeChangeState is NotificationTimeChangeState.Success) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            ClodyToastMessage(
                message = stringResource(R.string.notification_setting_change_success_toast),
                iconResId = R.drawable.ic_toast_check_on_18,
                backgroundColor = ClodyTheme.colors.gray04,
                contentColor = ClodyTheme.colors.white,
                durationMillis = 3000,
                onDismiss = { notificationSettingViewModel.resetNotificationChangeState() }
            )
        }
    }
}

