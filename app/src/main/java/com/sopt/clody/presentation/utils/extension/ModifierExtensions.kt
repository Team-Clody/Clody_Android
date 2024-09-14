package com.sopt.clody.presentation.utils.extension

import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.heightForScreenPercentage(percentage: Float): Modifier {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    return this.height(screenHeight * percentage)
}
