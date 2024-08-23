package com.sopt.clody.presentation.ui.auth.timereminder

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.sopt.clody.R
import com.sopt.clody.presentation.ui.auth.component.container.PickerBox
import com.sopt.clody.presentation.ui.auth.component.timepicker.BottomSheetTimePicker
import com.sopt.clody.presentation.ui.auth.navigation.AuthNavigator
import com.sopt.clody.presentation.ui.component.button.ClodyButton
import com.sopt.clody.presentation.ui.component.popup.ClodyPopupBottomSheet
import com.sopt.clody.presentation.utils.extension.showLongToast
import com.sopt.clody.ui.theme.ClodyTheme
import kotlinx.coroutines.launch

@Composable
fun TimeReminderRoute(
    navigator: AuthNavigator,
    viewModel: TimeReminderViewModel = hiltViewModel()
) {
    val timeReminderState by viewModel.timeReminderState.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val isNotificationPermissionGranted = remember { mutableStateOf(false) }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        isNotificationPermissionGranted.value = isGranted
    }
    // 알림 권한 요청
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val notificationPermission = Manifest.permission.POST_NOTIFICATIONS
            if (ContextCompat.checkSelfPermission(context, notificationPermission) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(notificationPermission)
            } else {
                isNotificationPermissionGranted.value = true
            }
        } else {
            isNotificationPermissionGranted.value = true
        }
    }

    // 알림 권한 요청 결과에 따른 처리
    LaunchedEffect(timeReminderState) {
        if (timeReminderState is TimeReminderState.Success) {
            navigator.navigateGuide()
        } else if (timeReminderState is TimeReminderState.Failure) {
            coroutineScope.launch {
                showLongToast(context, (timeReminderState as TimeReminderState.Failure).error)
            }
        }
    }

    TimeReminderScreen(
        onStartClick = {
            viewModel.setFixedTime("21", "30")
            viewModel.sendNotification(context, isNotificationPermissionGranted.value)
        },
        onTimeSelected = { amPm, hour, minute ->
            viewModel.setSelectedTime(amPm, hour, minute)
        },
        onCompleteClick = {
            viewModel.sendNotification(context, isNotificationPermissionGranted.value)
        },
        timeReminderState = timeReminderState
    )
}

@Composable
fun TimeReminderScreen(
    onStartClick: () -> Unit,
    onTimeSelected: (String, String, String) -> Unit,
    onCompleteClick: () -> Unit,
    timeReminderState: TimeReminderState
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedAmPm by remember { mutableStateOf("오후") }
    var selectedHour by remember { mutableStateOf("9") }
    var selectedMinute by remember { mutableStateOf("30") }

    val onRemindTimeSelected: (String, String, String) -> Unit = { amPm, hour, minute ->
        selectedAmPm = amPm
        selectedHour = hour
        selectedMinute = minute
        onTimeSelected(amPm, hour, minute)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = ClodyTheme.colors.white)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            val (title, pickerBox, spacer, completeButton, nextSettingButton, loading) = createRefs()
            val guideline = createGuidelineFromTop(0.123f)

            Text(
                text = stringResource(id = R.string.time_reminder_title),
                style = ClodyTheme.typography.head1,
                color = ClodyTheme.colors.gray01,
                modifier = Modifier.constrainAs(title) {
                    top.linkTo(guideline)
                    start.linkTo(parent.start)
                }
            )

            PickerBox(
                time = "${selectedAmPm} ${selectedHour}시 ${selectedMinute}분",
                modifier = Modifier
                    .constrainAs(pickerBox) {
                        top.linkTo(title.bottom, margin = 46.dp)
                        start.linkTo(parent.start)
                    }
                    .fillMaxWidth(),
                onClick = { showBottomSheet = true }
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(spacer) {
                        top.linkTo(pickerBox.bottom, margin = 3.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .height(1.dp)
                    .background(color = ClodyTheme.colors.gray07)
            )

            ClodyButton(
                onClick = onCompleteClick,
                text = stringResource(id = R.string.time_reminder_complete_button),
                enabled = true,
                modifier = Modifier.constrainAs(completeButton) {
                    bottom.linkTo(parent.bottom, margin = 46.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
            )

            Text(
                text = stringResource(id = R.string.time_reminder_next_setting_button),
                modifier = Modifier
                    .constrainAs(nextSettingButton) {
                        top.linkTo(completeButton.bottom, margin = 18.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .wrapContentHeight()
                    .clickable(
                        onClick = onStartClick,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                style = ClodyTheme.typography.detail1Medium,
                color = ClodyTheme.colors.gray05,
                textDecoration = TextDecoration.Underline
            )

            if (timeReminderState is TimeReminderState.Loading) {
                CircularProgressIndicator(
                    modifier = Modifier.constrainAs(loading) {
                        top.linkTo(completeButton.bottom, margin = 16.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                )
            }
        }
        if (showBottomSheet) {
            ClodyPopupBottomSheet(onDismissRequest = { showBottomSheet = false }) {
                BottomSheetTimePicker(
                    onDismissRequest = { showBottomSheet = false },
                    onRemindTimeSelected = onRemindTimeSelected
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTimeReminderScreen() {
    TimeReminderScreen(
        onStartClick = {},
        onTimeSelected = { _, _, _ -> },
        onCompleteClick = {},
        timeReminderState = TimeReminderState.Idle
    )
}
