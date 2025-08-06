package com.sopt.clody.presentation.ui.auth.component.textfield

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sopt.clody.R
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun NickNameTextField(
    value: String,
    onValueChange: (String) -> Unit,
    maxLength: Int,
    isFocused: Boolean,
    isValid: Boolean,
    onRemove: () -> Unit,
    onFocusChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    hint: String = "",
) {
    BasicTextField(
        value = value,
        onValueChange = {
            if (it.length <= maxLength) {
                onValueChange(it)
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .onFocusChanged { focusState ->
                onFocusChanged(focusState.isFocused)
            },
        singleLine = true,
        textStyle = TextStyle(color = ClodyTheme.colors.gray01),
        cursorBrush = SolidColor(ClodyTheme.colors.gray01),
        decorationBox = { innerTextField ->
            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        if (value.isEmpty() && !isFocused) {
                            Text(
                                text = hint,
                                style = ClodyTheme.typography.body1Medium,
                                color = ClodyTheme.colors.gray05,
                            )
                        }
                        innerTextField()
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Image(
                        painter = painterResource(id = R.drawable.ic_nickname_delete),
                        contentDescription = null,
                        modifier = Modifier.clickable { onRemove() },
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .height(2.dp)
                        .fillMaxWidth()
                        .background(
                            when {
                                value.isEmpty() -> ClodyTheme.colors.gray08
                                !isValid -> ClodyTheme.colors.red
                                isFocused -> ClodyTheme.colors.mainYellow
                                else -> ClodyTheme.colors.gray08
                            },
                        ),
                )
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewNickNameTextField() {
    NickNameTextField(
        value = "닉네임",
        onValueChange = {},
        maxLength = 15,
        isFocused = false,
        isValid = true,
        onRemove = {},
        onFocusChanged = {},
        modifier = Modifier.fillMaxWidth(),
        hint = "닉네임을 입력해주세요",
    )
}
