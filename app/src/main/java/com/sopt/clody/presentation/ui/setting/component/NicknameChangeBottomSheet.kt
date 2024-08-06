package com.sopt.clody.presentation.ui.setting.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.sopt.clody.R
import com.sopt.clody.data.remote.dto.RequestModifyNicknameDto
import com.sopt.clody.presentation.ui.component.bottomsheet.ClodyBottomSheet
import com.sopt.clody.presentation.ui.component.button.ClodyButton
import com.sopt.clody.presentation.ui.setting.screen.AccountManagementViewModel
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun NicknameChangeBottomSheet(
    accountManagementViewModel: AccountManagementViewModel,
    userName: String,
    onDismiss: () -> Unit
) {
    ClodyBottomSheet(
        content = {
            NicknameChangeBottomSheetItem(
                accountManagementViewModel = accountManagementViewModel,
                userName = userName,
                onDismiss = onDismiss
            )
        },
        onDismissRequest = { onDismiss() },
    )
}

@Composable
fun NicknameChangeBottomSheetItem(
    accountManagementViewModel: AccountManagementViewModel,
    userName: String,
    onDismiss: () -> Unit
) {
    var nickname by remember { mutableStateOf(TextFieldValue("")) }
    var nicknameChangeState by remember { mutableStateOf(false) }
    var isFocusedState by remember { mutableStateOf(false) }
    val userNicknameState by accountManagementViewModel.userNicknameState.collectAsState()
    val nicknameMaxLength = 10

    Surface {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(ClodyTheme.colors.white),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .padding(top = 8.dp),
            ) {
                Text(
                    text = stringResource(R.string.account_management_nickname_change_title),
                    modifier = Modifier.align(Alignment.Center),
                    style = ClodyTheme.typography.head4
                )

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Image(
                        painter = painterResource(
                            id = R.drawable.ic_nickname_change_dismiss
                        ),
                        contentDescription = null
                    )
                }
            }

            Spacer(modifier = Modifier.height(60.dp))

            NickNameChangeTextField(
                value = nickname,
                onValueChange = {
                    nickname = it
                    nicknameChangeState = it.text.isNotEmpty()
                },
                isFocused = isFocusedState,
                onRemove = {
                    nickname = TextFieldValue("")
                    nicknameChangeState = false
                },
                onFocusChanged = {
                    isFocusedState = it
                },
                modifier = Modifier
                    .padding(horizontal = 24.dp),
                hint = userName
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp)
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = stringResource(R.string.account_management_nickname_change_notice),
                    color = ClodyTheme.colors.gray04,
                    style = ClodyTheme.typography.detail1Regular
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "${nickname.text.length}",
                    color = ClodyTheme.colors.gray04,
                    style = ClodyTheme.typography.detail1Medium
                )
                Text(
                    text = " / $nicknameMaxLength",
                    color = ClodyTheme.colors.gray06,
                    style = ClodyTheme.typography.detail1Medium
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            ClodyButton(
                onClick = {
                    accountManagementViewModel.changeNickname(RequestModifyNicknameDto(name = nickname.text))
                    onDismiss()
                },
                text = stringResource(R.string.account_management_nickname_change_confirm),
                enabled = nicknameChangeState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp)
            )
            Spacer(modifier = Modifier.navigationBarsPadding())
            Spacer(modifier = Modifier.height(60.dp))
        }
    }
}
