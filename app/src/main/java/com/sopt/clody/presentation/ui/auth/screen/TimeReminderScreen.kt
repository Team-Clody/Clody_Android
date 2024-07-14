package com.sopt.clody.presentation.ui.auth.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.sopt.clody.presentation.ui.auth.component.container.PickerBox
import com.sopt.clody.presentation.ui.auth.component.timepicker.BottomSheetTimePicker
import com.sopt.clody.presentation.ui.auth.navigation.AuthNavigator
import com.sopt.clody.presentation.ui.component.button.ClodyButton
import com.sopt.clody.presentation.ui.component.popup.ClodyPopupBottomSheet
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun TimeReminderRoute(
    navigator: AuthNavigator
) {
    TimeReminderScreen(
        onStartClick = { navigator.navigateGuide() }
    )
}

@Composable
fun TimeReminderScreen(
    onStartClick: () -> Unit
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedTime by remember { mutableStateOf("오후 9시 30분") } // 상태 추가

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
            val (title, pickerBox, spacer, completeButton, nextSettingButton) = createRefs()
            val guideline = createGuidelineFromTop(0.123f)

            Text(
                text = "몇시에 감사일기\n작성 알림을 드릴까요?",
                style = ClodyTheme.typography.head1,
                color = ClodyTheme.colors.gray01,
                modifier = Modifier.constrainAs(title) {
                    top.linkTo(guideline)
                    start.linkTo(parent.start)
                }
            )

            PickerBox(
                time = selectedTime, // 수정: 선택된 시간을 전달
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
                onClick = { onStartClick() },
                text = "완료",
                enabled = true,
                modifier = Modifier.constrainAs(completeButton) {
                    bottom.linkTo(parent.bottom, margin = 46.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
            )

            TextButton(
                onClick = { onStartClick() },
                modifier = Modifier
                    .constrainAs(nextSettingButton) {
                        top.linkTo(completeButton.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    text = "다음에 설정할게요",
                    modifier = Modifier.wrapContentHeight(),
                    style = ClodyTheme.typography.detail1Medium,
                    color = ClodyTheme.colors.gray05,
                    textDecoration = TextDecoration.Underline
                )
            }
        }

        if (showBottomSheet) {
            ClodyPopupBottomSheet(onDismissRequest = { showBottomSheet = false }) {
                BottomSheetTimePicker(onDismissRequest = { showBottomSheet = false })
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewTimeReminderScreen() {
    TimeReminderScreen(
        onStartClick = { }
    )
}
