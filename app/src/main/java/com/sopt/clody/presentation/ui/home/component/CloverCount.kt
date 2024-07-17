package com.sopt.clody.presentation.ui.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun CloverCount(cloverCount:Int) {
    val text = "클로버 ${cloverCount}개"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 5.dp, end = 20.dp),
        contentAlignment = Alignment.TopEnd
    ) {
        Text(
            text = text,
            style = ClodyTheme.typography.detail1SemiBold,
            color = ClodyTheme.colors.darkGreen,
            modifier = Modifier
                .border(9.dp, ClodyTheme.colors.lightGreenBack, shape = RoundedCornerShape(9.dp))
                .background(ClodyTheme.colors.lightGreenBack, shape = RoundedCornerShape(9.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp),
            textAlign = TextAlign.Center
        )
    }
}


