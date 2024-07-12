package com.sopt.clody.presentation.ui.component.button

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun ClodyButton(
    onClick: () -> Unit,
    text: String,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (enabled) ClodyTheme.colors.mainYellow else ClodyTheme.colors.lightYellow
    val contentColor = if (enabled) ClodyTheme.colors.gray01 else ClodyTheme.colors.gray06

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = contentColor,
            disabledContainerColor = ClodyTheme.colors.lightYellow,
            disabledContentColor = ClodyTheme.colors.gray06
        ),
        shape = RoundedCornerShape(10.dp),
        enabled = enabled,
        modifier = modifier
            .height(50.dp)
    ) {
        Text(
            text = text,
            color = contentColor,
            style = ClodyTheme.typography.body2SemiBold
        )
    }
}

@Preview
@Composable
fun ClodyButtonPreview() {
    ClodyButton(
        onClick = {},
        text = "클로디 버튼",
        enabled = true
    )
}
