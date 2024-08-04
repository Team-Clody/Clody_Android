package com.sopt.clody.presentation.ui.auth.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.sopt.clody.R
import com.sopt.clody.presentation.ui.auth.component.textfield.NickNameTextField
import com.sopt.clody.presentation.ui.auth.navigation.AuthNavigator
import com.sopt.clody.presentation.ui.auth.signup.SignUpViewModel
import com.sopt.clody.presentation.ui.component.button.ClodyButton
import com.sopt.clody.presentation.utils.base.UiState
import com.sopt.clody.presentation.utils.extension.showLongToast
import com.sopt.clody.ui.theme.ClodyTheme
import kotlinx.coroutines.launch

@Composable
fun NicknameRoute(
    navigator: AuthNavigator,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val nickname by viewModel.nickname.collectAsState()
    val isValidNickname by viewModel.isValidNickname.collectAsState()
    val nicknameMessage by viewModel.nicknameMessage.collectAsState()
    val signUpState by viewModel.signUpState.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    NicknameScreen(
        nickname = nickname,
        onNicknameChange = viewModel::setNickname,
        onCompleteClick = { viewModel.proceedWithSignUp(context) },
        onBackClick = { navigator.navigateBack() },
        isLoading = signUpState.uiState is UiState.Loading,
        isValidNickname = isValidNickname,
        nicknameMessage = nicknameMessage
    )

    LaunchedEffect(signUpState) {
        when (val result = signUpState.uiState) {
            is UiState.Success -> {
                navigator.navigateTimeReminder()
            }
            is UiState.Failure -> {
                coroutineScope.launch {
                    showLongToast(context, result.msg)
                }
            }
            else -> {}
        }
    }
}

@Composable
fun NicknameScreen(
    nickname: String,
    onNicknameChange: (String) -> Unit,
    onCompleteClick: () -> Unit,
    onBackClick: () -> Unit,
    isLoading: Boolean,
    isValidNickname: Boolean,
    nicknameMessage: String
) {
    var nicknameTextField by remember { mutableStateOf(TextFieldValue(nickname)) }
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
        val (backButton, title, nicknameField, nicknameRegex, nicknameLength, completeButton, progressBar) = createRefs()
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
            text = "만나서 반가워요!\n어떻게 불러 드릴까요?",
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
            value = nicknameTextField,
            onValueChange = {
                nicknameTextField = it
                onNicknameChange(it.text)
            },
            hint = "닉네임을 입력해주세요",
            isFocused = isFocused,
            isValid = isValidNickname,
            onFocusChanged = { isFocused = it },
            onRemove = { nicknameTextField = TextFieldValue("") },
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
            text = nicknameMessage,
            style = ClodyTheme.typography.detail1Regular,
            color = if (nicknameTextField.text.isEmpty()) ClodyTheme.colors.gray04 else if (isValidNickname) ClodyTheme.colors.gray04 else ClodyTheme.colors.red,
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
                append("${nicknameTextField.text.length}")
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
                    top.linkTo(nicknameRegex.bottom, 4.dp)
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
            enabled = nicknameTextField.text.isNotEmpty() && isValidNickname,
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

        if (isLoading) {
            CircularProgressIndicator(
                color = ClodyTheme.colors.mainYellow,
                modifier = Modifier.constrainAs(progressBar) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NicknameScreenPreview() {
    NicknameScreen(
        nickname = "닉네임",
        onNicknameChange = {},
        onCompleteClick = {},
        onBackClick = {},
        isLoading = false,
        isValidNickname = true,
        nicknameMessage = "특수문자, 띄어쓰기 없이 작성해주세요"
    )
}
