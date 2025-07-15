package com.sopt.clody.presentation.ui.auth.signup.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.sopt.clody.R
import com.sopt.clody.presentation.ui.auth.component.textfield.NickNameTextField
import com.sopt.clody.presentation.ui.component.LoadingScreen
import com.sopt.clody.presentation.ui.component.button.ClodyButton
import com.sopt.clody.presentation.utils.base.BasePreview
import com.sopt.clody.presentation.utils.base.ClodyPreview
import com.sopt.clody.presentation.utils.extension.heightForScreenPercentage
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun NickNamePage(
    nickname: String,
    isValidNickname: Boolean,
    nicknameMessage: String,
    isLoading: Boolean,
    isFocused: Boolean,
    onNicknameChange: (String) -> Unit,
    onFocusChanged: (Boolean) -> Unit,
    onCompleteClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    val focusManager = LocalFocusManager.current

    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(color = ClodyTheme.colors.gray04)) {
            append("${nickname.length}")
        }
        withStyle(style = SpanStyle(color = ClodyTheme.colors.gray06)) {
            append(" / ")
        }
        withStyle(style = SpanStyle(color = ClodyTheme.colors.gray06)) {
            append("10")
        }
    }

    Scaffold(
        topBar = {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(start = 8.dp),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_nickname_back),
                    contentDescription = null,
                )
            }
        },
        bottomBar = {
            ClodyButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .imePadding()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 28.dp),
                onClick = {
                    focusManager.clearFocus()
                    onCompleteClick()
                },
                text = stringResource(R.string.nickname_btn_next),
                enabled = nickname.isNotEmpty() && isValidNickname,
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ClodyTheme.colors.white)
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp),
            ) {
                Spacer(modifier = Modifier.heightForScreenPercentage(0.056f))
                Text(stringResource(R.string.nickname_title), style = ClodyTheme.typography.head1)
                Spacer(modifier = Modifier.heightForScreenPercentage(0.06f))
                NickNameTextField(
                    value = nickname,
                    onValueChange = onNicknameChange,
                    hint = stringResource(R.string.nickname_input_hint),
                    isFocused = isFocused,
                    isValid = isValidNickname,
                    onFocusChanged = onFocusChanged,
                    onRemove = { onNicknameChange("") },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = nicknameMessage,
                        color = when {
                            nickname.isEmpty() -> ClodyTheme.colors.gray04
                            isValidNickname -> ClodyTheme.colors.gray04
                            else -> ClodyTheme.colors.red
                        },
                        style = ClodyTheme.typography.detail1Regular,
                    )
                    Text(
                        text = annotatedString,
                        style = ClodyTheme.typography.detail1Medium,
                    )
                }
            }
        },
    )

    if (isLoading) {
        LoadingScreen()
    }
}

@ClodyPreview
@Composable
private fun NicknamePagePreview() {
    BasePreview {
        NickNamePage(
            nickname = "클로디",
            isValidNickname = true,
            nicknameMessage = "사용 가능한 닉네임입니다.",
            isLoading = false,
            isFocused = false,
            onNicknameChange = {},
            onFocusChanged = {},
            onCompleteClick = {},
            onBackClick = {},
        )
    }
}
