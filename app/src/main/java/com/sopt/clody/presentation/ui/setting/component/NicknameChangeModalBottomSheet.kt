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
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import com.sopt.clody.presentation.ui.component.bottomsheet.ClodyBottomSheet
import com.sopt.clody.presentation.ui.component.button.ClodyButton
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun NicknameChangeModalBottomSheet(
    onDisMiss: () -> Unit
) {
    ClodyBottomSheet(
        content = { NicknameChangeModalBottomSheetItem(onDisMiss) },
        onDismissRequest = { onDisMiss() },
        heightFraction = 0.46f
    )
}

@Composable
fun NicknameChangeModalBottomSheetItem(
    onDisMiss: () -> Unit
) {
    var nickname by remember { mutableStateOf(TextFieldValue("")) }
    var nicknameLength by remember { mutableIntStateOf(0) }
    var nicknameChangeState by remember { mutableStateOf(false) }
    var isFocusedState by remember { mutableStateOf(false) }
    val nicknameMaxLength = 10

    Surface(
        modifier = Modifier
            .background(ClodyTheme.colors.white)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
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
                    text = stringResource(R.string.nickname_change_title),
                    modifier = Modifier.align(Alignment.Center),
                    style = ClodyTheme.typography.head4
                )

                IconButton(
                    onClick = { onDisMiss() },
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
                    nicknameLength = it.text.length
                    nicknameChangeState = it.text.isNotEmpty()
                },
                isFocused = isFocusedState,
                onRemove = {
                    nickname = TextFieldValue("")
                    nicknameLength = nickname.text.length
                    nicknameChangeState = false
                },
                onFocusChanged = {
                    isFocusedState = it
                },
                modifier = Modifier
                    .padding(horizontal = 24.dp),
                hint = "사용자 이름" /* TODO : 사용자 이름 받아와서 힌트로 노출 */
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp)
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = stringResource(R.string.nickname_change_notice),
                    color = ClodyTheme.colors.gray04,
                    style = ClodyTheme.typography.detail1Regular
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "$nicknameLength",
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
                onClick = { /* TODO : 닉네임 변경 로직 */ },
                text = stringResource(R.string.nickname_change_confirm),
                enabled = nicknameChangeState,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp)
            )
        }
    }
}
