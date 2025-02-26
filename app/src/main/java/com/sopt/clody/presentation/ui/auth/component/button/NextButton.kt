package com.sopt.clody.presentation.ui.auth.component.button

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun NextButton(
    onClick: () -> Unit,
    imageResource: Int,
    contentDescription: String? = null
) {
    Box(
        modifier = Modifier.run {
            size(23.dp)
                .clip(CircleShape)
                .clickable(
                    onClick = onClick
                )
        },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = imageResource),
            contentDescription = contentDescription
        )
    }
}
