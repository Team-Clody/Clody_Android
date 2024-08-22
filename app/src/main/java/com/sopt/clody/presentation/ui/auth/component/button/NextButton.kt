package com.sopt.clody.presentation.ui.auth.component.button

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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

    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = Modifier
            .size(23.dp)
            .clip(CircleShape) // 원형으로 자르기
            .clickable(
                interactionSource = interactionSource,
                indication = rememberRipple(bounded = false, radius = 15.dp),
                onClick =  onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = imageResource),
            contentDescription = contentDescription,
        )
    }
}
