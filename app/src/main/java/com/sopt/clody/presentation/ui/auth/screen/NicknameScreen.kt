package com.sopt.clody.presentation.ui.auth.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sopt.clody.R
import com.sopt.clody.presentation.ui.auth.component.textfield.NickNameTextField
import com.sopt.clody.presentation.ui.auth.navigation.AuthNavigator
import com.sopt.clody.presentation.ui.auth.signup.SignUpViewModel
import com.sopt.clody.presentation.ui.component.LoadingScreen
import com.sopt.clody.presentation.ui.component.button.ClodyButton
import com.sopt.clody.presentation.ui.component.dialog.FailureDialog
import com.sopt.clody.presentation.utils.base.UiState
import com.sopt.clody.presentation.utils.extension.heightForScreenPercentage
import com.sopt.clody.ui.theme.ClodyTheme

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
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

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
                showDialog = true
                dialogMessage = result.msg
            }

            else -> {}
        }
    }

    if (showDialog) {
        FailureDialog(
            message = dialogMessage,
            onDismiss = {
                showDialog = false
                viewModel.resetSignUpState()
            }
        )
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

    Scaffold(
        topBar = {
            IconButton(
                onClick = { onBackClick() },
                modifier = Modifier
                    .padding(top = 20.dp)
                    .padding(start = 8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_nickname_back),
                    contentDescription = null
                )
            }
        },
        bottomBar = {
            ClodyButton(
                onClick = {
                    focusManager.clearFocus()
                    onCompleteClick()
                },
                text = stringResource(id = R.string.nickname_next),
                enabled = nicknameTextField.text.isNotEmpty() && isValidNickname,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 28.dp)
                    .imePadding()
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = ClodyTheme.colors.white)
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Spacer(modifier = Modifier.heightForScreenPercentage(0.056f))
                Text(
                    text = stringResource(id = R.string.nickname_title),
                    style = ClodyTheme.typography.head1,
                    color = ClodyTheme.colors.gray01
                )
                Spacer(modifier = Modifier.heightForScreenPercentage(0.06f))
                NickNameTextField(
                    value = nicknameTextField,
                    onValueChange = {
                        nicknameTextField = it
                        onNicknameChange(it.text)
                    },
                    hint = stringResource(id = R.string.nickname_input_hint),
                    isFocused = isFocused,
                    isValid = isValidNickname,
                    onFocusChanged = { isFocused = it },
                    onRemove = { nicknameTextField = TextFieldValue("") },
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .clickable { focusRequester.requestFocus() }
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.heightForScreenPercentage(0.005f))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = nicknameMessage,
                        style = ClodyTheme.typography.detail1Regular,
                        color = when {
                            nicknameTextField.text.isEmpty() -> ClodyTheme.colors.gray04
                            isValidNickname -> ClodyTheme.colors.gray04
                            else -> ClodyTheme.colors.red
                        }
                    )
                    Text(
                        text = annotatedString,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = ClodyTheme.typography.detail1Medium,
                    )
                }
            }
        }
    )

    if (isLoading) {
        LoadingScreen()
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
