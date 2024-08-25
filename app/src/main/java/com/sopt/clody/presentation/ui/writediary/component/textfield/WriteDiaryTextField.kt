package com.sopt.clody.presentation.ui.writediary.component.textfield

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sopt.clody.R
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun WriteDiaryTextField(
    entryNumber: Int,
    text: String,
    onTextChange: (String) -> Unit,
    onRemove: () -> Unit,
    isRemovable: Boolean,
    maxLength: Int,
    showWarning: Boolean,
    modifier: Modifier = Modifier
) {
    var isTextValid by remember { mutableStateOf(text.replace("\\s".toRegex(), "").matches(Regex("^[a-zA-Z가-힣0-9ㄱ-ㅎㅏ-ㅣ가-힣\\W]{2,50}$"))) }
    var isFocused by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .background(
                    if (isFocused || (showWarning && text.isEmpty())) ClodyTheme.colors.white
                    else ClodyTheme.colors.gray09,
                    shape = RoundedCornerShape(10.dp)
                )
                .border(
                    if (isFocused || showWarning) {
                        1.dp
                    } else {
                        0.dp
                    },
                    when {
                        showWarning && !isTextValid -> ClodyTheme.colors.red
                        isFocused -> ClodyTheme.colors.mainYellow
                        showWarning && text.isEmpty() -> ClodyTheme.colors.red
                        else -> Color.Transparent
                    },
                    RoundedCornerShape(10.dp)
                )
                .heightIn(min = 50.dp)  // 최소 높이를 50dp로 설정
                .fillMaxWidth()  // 너비를 부모의 너비로 채움
                .padding(vertical = 12.dp, horizontal = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()  // Row의 너비를 Box의 너비로 채움
            ) {
                Text(
                    text = "$entryNumber.",
                    modifier = Modifier
                        .padding(start = 16.dp, top = 5.dp)
                        .align(Alignment.Top),
                    style = ClodyTheme.typography.body2SemiBold,
                    color = if (text.isNotEmpty()) ClodyTheme.colors.gray01 else ClodyTheme.colors.gray06
                )
                Spacer(modifier = Modifier.width(8.dp))
                BasicTextField(
                    value = text,
                    onValueChange = {
                        if (it.length <= maxLength) {
                            onTextChange(it)
                            val textWithoutSpaces = it.replace("\\s".toRegex(), "")
                            isTextValid = textWithoutSpaces.matches(Regex("^[a-zA-Z가-힣0-9ㄱ-ㅎㅏ-ㅣ가-힣]{2,50}$"))
                        }
                    },
                    textStyle = TextStyle(
                        color = if (text.isEmpty()) ClodyTheme.colors.gray06 else ClodyTheme.colors.gray01
                    ),
                    cursorBrush = SolidColor(ClodyTheme.colors.gray01),
                    decorationBox = { innerTextField ->
                        if (text.isEmpty()) {
                            Text(
                                text = "일상 속 작은 감사함을 적어보세요",
                                style = ClodyTheme.typography.body3Medium,
                                color = ClodyTheme.colors.gray06
                            )
                        }
                        innerTextField()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .onFocusChanged { isFocused = it.isFocused }
                )

                IconButton(
                    onClick = onRemove,
                    modifier = Modifier.size(28.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_writediary_kebab),
                        contentDescription = "Remove"
                    )
                }

            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 6.dp)
        ) {
            if (showWarning && !isTextValid && text.isNotEmpty()) {
                Text(
                    text = "2~50자 까지 입력할 수 있어요. ",
                    color = ClodyTheme.colors.red,
                    style = ClodyTheme.typography.detail1Medium,
                    modifier = Modifier.weight(1f)
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }

            val annotatedString = buildAnnotatedString {
                withStyle(style = SpanStyle(color = ClodyTheme.colors.gray04)) {
                    append("${text.length}")
                }
                withStyle(style = SpanStyle(color = ClodyTheme.colors.gray06)) {
                    append(" / ")
                }
                withStyle(style = SpanStyle(color = ClodyTheme.colors.gray06)) {
                    append("$maxLength")
                }
            }

            Text(
                text = annotatedString,
                style = ClodyTheme.typography.detail1Medium,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWriteDiaryTextField() {
    WriteDiaryTextField(
        entryNumber = 1,
        text = "",
        onTextChange = {},
        onRemove = {},
        isRemovable = true,
        maxLength = 50,
        showWarning = true
    )
}
