package com.sopt.clody.presentation.ui.auth.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.sopt.clody.R
import com.sopt.clody.presentation.ui.auth.component.textfield.NickNameTextField
import com.sopt.clody.presentation.ui.auth.navigation.AuthNavigator
import com.sopt.clody.presentation.ui.component.button.ClodyButton
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun NicknameRoute(
    navigator: AuthNavigator
) {
    NicknameScreen(
        onCompleteClick = { navigator.navigateTimeReminder() },
        onBackClick = { navigator.navigateBack() }
    )
}

@Composable
fun NicknameScreen(
    onCompleteClick: () -> Unit,
    onBackClick: () -> Unit
) {
    var nickname by remember { mutableStateOf(TextFieldValue("")) }
    val focusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = ClodyTheme.colors.white)
            .padding(12.dp)
            .imePadding()
    ) {
        val (backButton, title, nicknameField, nicknameRegex, nicknameLength, completeButton) = createRefs()
        val guideline = createGuidelineFromTop(0.124f)

        IconButton(
            onClick = { onBackClick() },
            modifier = Modifier
                .padding(top = 8.dp)
                .constrainAs(backButton) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_nickname_back),
                contentDescription = null,
            )
        }

        Text(
            text = "만나서 반가워요!\n" + "어떻게 불러 드릴까요?",
            style = ClodyTheme.typography.head1,
            color = ClodyTheme.colors.gray01,
            modifier = Modifier
                .padding(start = 12.dp)
                .constrainAs(title) {
                    top.linkTo(guideline)
                    start.linkTo(parent.start)
                },
        )

        NickNameTextField(
            value = nickname,
            onValueChange = { nickname = it },
            hint = "닉네임을 입력해주세요",
            isFocused = isFocused,
            onFocusChanged = { isFocused = it },
            onRemove = { nickname = TextFieldValue("") },
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .constrainAs(nicknameField) {
                    top.linkTo(title.bottom, margin = 52.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
                .focusRequester(focusRequester)
                .clickable { focusRequester.requestFocus() }
        )

        Text(
            text = "특수문자, 띄어쓰기 없이 작성해주세요",
            style = ClodyTheme.typography.detail1Regular,
            color = ClodyTheme.colors.gray04,
            modifier = Modifier
                .padding(start = 12.dp)
                .constrainAs(nicknameRegex) {
                    top.linkTo(nicknameField.bottom, 4.dp)
                    start.linkTo(parent.start)
                    width = Dimension.fillToConstraints
                }
        )

        val annotatedString = buildAnnotatedString {
            withStyle(style = SpanStyle(color = ClodyTheme.colors.gray04)) {
                append("${nickname.text.length}")
            }
            withStyle(style = SpanStyle(color = ClodyTheme.colors.gray06)) {
                append(" / ")
            }
            withStyle(style = SpanStyle(color = ClodyTheme.colors.gray06)) {
                append("10")
            }
        }

        Text(
            text = annotatedString,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = ClodyTheme.typography.detail1Medium,
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .constrainAs(nicknameLength) {
                    top.linkTo(nicknameField.bottom, 4.dp)
                    end.linkTo(parent.end)
                    width = Dimension.wrapContent
                }
        )

        ClodyButton(
            onClick = {
                focusManager.clearFocus()
                onCompleteClick()
            },
            text = "다음",
            enabled = nickname.text.isNotEmpty(),
            modifier = Modifier
                .padding(bottom = 28.dp)
                .padding(horizontal = 12.dp)
                .constrainAs(completeButton) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
                .imePadding()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NicknameScreenPreview() {
    NicknameScreen(
        onCompleteClick = { },
        onBackClick = { }
    )
}
