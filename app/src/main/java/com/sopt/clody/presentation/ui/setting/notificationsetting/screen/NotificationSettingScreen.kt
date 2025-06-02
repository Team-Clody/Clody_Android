package com.sopt.clody.presentation.ui.setting.notificationsetting.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sopt.clody.R
import com.sopt.clody.data.remote.dto.response.NotificationInfoResponseDto
import com.sopt.clody.presentation.ui.component.FailureScreen
import com.sopt.clody.presentation.ui.component.LoadingScreen
import com.sopt.clody.presentation.ui.component.dialog.FailureDialog
import com.sopt.clody.presentation.ui.component.popup.ClodyPopupBottomSheet
import com.sopt.clody.presentation.ui.component.toast.ClodyToastMessage
import com.sopt.clody.presentation.ui.setting.component.SettingTopAppBar
import com.sopt.clody.presentation.ui.setting.notificationsetting.component.NotificationSettingTimePicker
import com.sopt.clody.presentation.ui.setting.notificationsetting.component.NotificationSwitch
import com.sopt.clody.presentation.ui.setting.notificationsetting.component.NotificationTimeSelector
import com.sopt.clody.presentation.utils.extension.convertTo12HourFormat
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun NotificationSettingRoute(
    notificationSettingViewModel: NotificationSettingViewModel = hiltViewModel(),
    navigateToPrevious: () -> Unit,
) {
    val context = LocalContext.current
    val notificationInfoState by notificationSettingViewModel.notificationInfoState.collectAsState()
    val diaryAlarm by notificationSettingViewModel.diaryAlarm.collectAsState()
    val draftAlarm by notificationSettingViewModel.draftAlarm.collectAsState()
    val replyAlarm by notificationSettingViewModel.replyAlarm.collectAsState()
    val notificationTime by notificationSettingViewModel.notificationTime.collectAsState()
    val notificationTimeChangeState by notificationSettingViewModel.notificationTimeChangeState.collectAsState()
    val showFailureDialog by notificationSettingViewModel.showFailureDialog.collectAsState()
    val failureDialogMessage by notificationSettingViewModel.failureDialogMessage.collectAsState()
    var showNotificationTimePicker by remember { mutableStateOf(false) }
    var notificationInfo by remember { mutableStateOf<NotificationInfoResponseDto?>(null) }

    NotificationSettingScreen(
        notificationInfoState = notificationInfoState,
        diaryAlarm = diaryAlarm,
        draftAlarm = draftAlarm,
        replyAlarm = replyAlarm,
        notificationTime = notificationTime,
        onClickBack = navigateToPrevious,
        onClickDiarySwitch = { notificationSettingViewModel.changeDiaryAlarm(context) },
        onClickDraftSwitch = { notificationSettingViewModel.changeDraftAlarm(context) },
        onClickNotificationTime = { showNotificationTimePicker = true },
        onClickReplySwitch = { notificationSettingViewModel.changeReplyAlarm(context) },
        onClickRetry = { notificationSettingViewModel.getNotificationInfo() },
    )

    if (showNotificationTimePicker) {
        ClodyPopupBottomSheet(onDismissRequest = { showNotificationTimePicker = false }) {
            NotificationSettingTimePicker(
                onDismissRequest = { showNotificationTimePicker = false },
                onConfirm = { newNotificationTime ->
                    notificationSettingViewModel.changeNotificationTime(context, newNotificationTime)
                    showNotificationTimePicker = false
                },
            )
        }
    }

    if (notificationTimeChangeState is NotificationTimeChangeState.Success) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            contentAlignment = Alignment.BottomCenter,
        ) {
            ClodyToastMessage(
                message = stringResource(R.string.notification_setting_change_success_toast),
                iconResId = R.drawable.ic_toast_check_on_18,
                backgroundColor = ClodyTheme.colors.gray04,
                contentColor = ClodyTheme.colors.white,
                durationMillis = 3000,
                onDismiss = { notificationSettingViewModel.resetNotificationTimeChangeState() },
            )
        }
    }

    if (showFailureDialog) {
        FailureDialog(
            message = failureDialogMessage,
            onDismiss = { notificationSettingViewModel.dismissFailureDialog() },
        )
    }
}

@Composable
fun NotificationSettingScreen(
    notificationInfoState: NotificationInfoState,
    diaryAlarm: Boolean,
    draftAlarm: Boolean,
    replyAlarm: Boolean,
    notificationTime: String,
    onClickBack: () -> Unit,
    onClickDiarySwitch: () -> Unit,
    onClickDraftSwitch: () -> Unit,
    onClickNotificationTime: () -> Unit,
    onClickReplySwitch: () -> Unit,
    onClickRetry: () -> Unit,
) {
    Scaffold(
        topBar = {
            SettingTopAppBar(
                title = stringResource(R.string.notification_setting_title),
                onClickBack = onClickBack,
            )
        },
        containerColor = ClodyTheme.colors.white,
        content = { innerPadding ->
            when (notificationInfoState) {
                is NotificationInfoState.Idle -> {}

                is NotificationInfoState.Loading -> {
                    LoadingScreen()
                }

                is NotificationInfoState.Success -> {
                    Column(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(horizontal = 24.dp),
                    ) {
                        Spacer(modifier = Modifier.height(24.dp))
                        NotificationSwitch(
                            title = R.string.notification_setting_write_diary,
                            checkedState = diaryAlarm,
                            onClick = onClickDiarySwitch,
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        NotificationSwitch(
                            title = R.string.notification_setting_draft_diary,
                            checkedState = draftAlarm,
                            onClick = onClickDraftSwitch,
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        NotificationTimeSelector(
                            time = notificationTime.convertTo12HourFormat(),
                            onClick = onClickNotificationTime,
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        NotificationSwitch(
                            title = R.string.notification_setting_reply_diary,
                            checkedState = replyAlarm,
                            onClick = onClickReplySwitch,
                        )
                    }
                }

                is NotificationInfoState.Failure -> {
                    FailureScreen(
                        message = notificationInfoState.errorMessage,
                        confirmAction = onClickRetry,
                    )
                }
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewNotificationSettingScreen() {
    ClodyTheme {
        NotificationSettingScreen(
            notificationInfoState = NotificationInfoState.Success(
                NotificationInfoResponseDto(
                    isDiaryAlarm = true,
                    isDraftAlarm = false,
                    isReplyAlarm = true,
                    time = "21:30",
                ),
            ),
            diaryAlarm = true,
            draftAlarm = false,
            replyAlarm = true,
            notificationTime = "21:30",
            onClickBack = {},
            onClickDiarySwitch = {},
            onClickDraftSwitch = {},
            onClickNotificationTime = {},
            onClickReplySwitch = {},
            onClickRetry = {},
        )
    }
}
