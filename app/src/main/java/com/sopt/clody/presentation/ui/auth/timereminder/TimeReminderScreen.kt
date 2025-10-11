package com.sopt.clody.presentation.ui.auth.timereminder

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sopt.clody.R
import com.sopt.clody.presentation.ui.auth.component.container.PickerBox
import com.sopt.clody.presentation.ui.auth.component.timepicker.BottomSheetTimePicker
import com.sopt.clody.presentation.ui.component.LoadingScreen
import com.sopt.clody.presentation.ui.component.button.ClodyButton
import com.sopt.clody.presentation.ui.component.dialog.FailureDialog
import com.sopt.clody.presentation.ui.component.popup.ClodyPopupBottomSheet
import com.sopt.clody.presentation.utils.amplitude.AmplitudeConstraints
import com.sopt.clody.presentation.utils.amplitude.AmplitudeUtils
import com.sopt.clody.presentation.utils.extension.TimePeriod
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun TimeReminderRoute(
    navigateToGuide: () -> Unit,
    viewModel: TimeReminderViewModel = hiltViewModel(),
) {
    val timeReminderState by viewModel.timeReminderState.collectAsState()
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    // 알림 권한 요청 결과에 따른 처리
    LaunchedEffect(timeReminderState) {
        when (val result = timeReminderState) {
            is TimeReminderState.Success -> navigateToGuide()
            is TimeReminderState.Failure -> {
                showDialog = true
                dialogMessage = result.error
            }
            else -> {}
        }
    }

    if (showDialog) {
        FailureDialog(
            message = dialogMessage,
            onDismiss = {
                showDialog = false
                viewModel.resetTimeReminderState()
            },
        )
    }

    TimeReminderScreen(
        onStartClick = {
            viewModel.setSelectedTime(TimePeriod.PM, "9", "30")
            viewModel.sendNotification(context)
        },
        onTimeSelected = { period, hour, minute ->
            viewModel.setSelectedTime(period, hour, minute)
        },
        onCompleteClick = {
            AmplitudeUtils.trackEvent(eventName = AmplitudeConstraints.ONBOARDING_ALARM)
            viewModel.sendNotification(context)
        },
        isLoading = timeReminderState is TimeReminderState.Loading,
    )
}

@Composable
fun TimeReminderScreen(
    onStartClick: () -> Unit,
    onTimeSelected: (TimePeriod, String, String) -> Unit,
    onCompleteClick: () -> Unit,
    isLoading: Boolean,
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedTimePeriod by remember { mutableStateOf(TimePeriod.PM) }
    var selectedHour by remember { mutableStateOf("9") }
    var selectedMinute by remember { mutableStateOf("30") }

    val onRemindTimeSelected: (TimePeriod, String, String) -> Unit = { period, hour, minute ->
        selectedTimePeriod = period
        selectedHour = hour
        selectedMinute = minute
        onTimeSelected(period, hour, minute)
    }

    Scaffold(
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 14.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                ClodyButton(
                    onClick = onCompleteClick,
                    text = stringResource(id = R.string.time_reminder_btn_complete),
                    enabled = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(LocalConfiguration.current.screenHeightDp.dp * 0.015f))
                Text(
                    text = stringResource(id = R.string.time_reminder_btn_skip),
                    modifier = Modifier
                        .clickable(
                            onClick = onStartClick,
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                        ),
                    style = ClodyTheme.typography.detail1Medium,
                    color = ClodyTheme.colors.gray05,
                    textDecoration = TextDecoration.Underline,
                )
                Spacer(modifier = Modifier.height(LocalConfiguration.current.screenHeightDp.dp * 0.017f))
            }
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = ClodyTheme.colors.white)
                    .padding(innerPadding)
                    .padding(horizontal = 24.dp),
            ) {
                Spacer(modifier = Modifier.height(LocalConfiguration.current.screenHeightDp.dp * 0.14f))
                Text(
                    text = stringResource(id = R.string.time_reminder_title),
                    style = ClodyTheme.typography.head1,
                    color = ClodyTheme.colors.gray01,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(LocalConfiguration.current.screenHeightDp.dp * 0.05f))
                PickerBox(
                    time = stringResource(
                        id = R.string.time_reminder_time_format,
                        selectedTimePeriod.getLabel(),
                        selectedHour,
                        selectedMinute,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { showBottomSheet = true },
                )
                HorizontalDivider(
                    color = ClodyTheme.colors.gray07,
                    thickness = 1.dp,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            if (showBottomSheet) {
                ClodyPopupBottomSheet(onDismissRequest = { showBottomSheet = false }) {
                    BottomSheetTimePicker(
                        onDismissRequest = { showBottomSheet = false },
                        onRemindTimeSelected = onRemindTimeSelected,
                    )
                }
            }
        },
    )

    if (isLoading) {
        LoadingScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTimeReminderScreen() {
    TimeReminderScreen(
        onStartClick = {},
        onTimeSelected = { _, _, _ -> },
        onCompleteClick = {},
        isLoading = false,
    )
}
