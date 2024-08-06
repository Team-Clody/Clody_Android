package com.sopt.clody.presentation.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun FailureScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = { /* TODO : 재시도 액션 */},
            modifier = Modifier
                .wrapContentSize(Alignment.Center),
            colors = ButtonDefaults.buttonColors(ClodyTheme.colors.gray05)
        ) {
            Text(
                text = "재시도",
                color = ClodyTheme.colors.gray01,
                style = ClodyTheme.typography.body1Medium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun showFailureScreen() {
    FailureScreen()
}

