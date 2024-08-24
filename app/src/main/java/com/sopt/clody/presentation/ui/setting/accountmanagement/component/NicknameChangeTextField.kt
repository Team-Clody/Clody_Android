package com.sopt.clody.presentation.ui.setting.accountmanagement.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.sopt.clody.R
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun NickNameChangeTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    isFocused: Boolean,
    isValid: Boolean,
    onRemove: () -> Unit,
    onFocusChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "",
) {
    val nicknameMaxLength = 10

    Box(modifier = modifier) {
        BasicTextField(
            value = value,
            onValueChange = {
                if (it.text.length <= nicknameMaxLength) {
                    onValueChange(it)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .onFocusChanged { focusState ->
                    onFocusChanged(focusState.isFocused)
                },
            singleLine = true,
            textStyle = TextStyle(color = ClodyTheme.colors.gray01),
            cursorBrush = SolidColor(ClodyTheme.colors.gray01),
            decorationBox = { innerTextField ->
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 0.dp, vertical = 0.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            if (value.text.isEmpty()) {
                                Text(
                                    text = hint,
                                    style = ClodyTheme.typography.body1Medium,
                                    color = ClodyTheme.colors.gray05
                                )
                            }
                            innerTextField()
                        }
                        Box(
                            modifier = Modifier
                                .clickable { onRemove() },
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_nickname_change_clean),
                                contentDescription = null
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .height(2.dp)
                            .fillMaxWidth()
                            .background(
                                when {
                                    isValid.not() -> ClodyTheme.colors.red
                                    isFocused -> ClodyTheme.colors.mainYellow
                                    else -> ClodyTheme.colors.gray08
                                }
                            )
                    )
                }
            }
        )
    }
}
