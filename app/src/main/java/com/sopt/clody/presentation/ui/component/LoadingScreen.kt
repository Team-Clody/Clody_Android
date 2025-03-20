package com.sopt.clody.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun LoadingScreen(
    backgroundColor: Color = ClodyTheme.colors.white
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.wrapContentSize(Alignment.Center),
            color = ClodyTheme.colors.mainYellow
        )
    }
}

@Composable
@Preview
fun LoadingScreenPreview() {
    LoadingScreen()
}

