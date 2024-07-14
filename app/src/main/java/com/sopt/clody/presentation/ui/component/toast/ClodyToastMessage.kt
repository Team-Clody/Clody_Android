package com.sopt.clody.presentation.ui.component.toast

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sopt.clody.ui.theme.ClodyTheme
import kotlinx.coroutines.delay


@Composable
fun ClodyToastMessage(
    message: String,
    iconResId: Int,
    backgroundColor: Color,
    contentColor: Color,
    durationMillis: Long,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        delay(durationMillis)
        onDismiss()
    }

    Box(
        modifier = modifier
            .height(42.dp)
            .background(color = backgroundColor, shape = RoundedCornerShape(26.5.dp))
            .padding(horizontal = 14.5.dp, vertical = 11.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = iconResId),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = message,
                color = contentColor,
                style = ClodyTheme.typography.body3SemiBold
            )
        }
    }
}

@Preview
@Composable
fun PreviewCustomToastMessage() {
    ClodyToastMessage(
            message = "토스트 메시지",
            iconResId = 0,
            backgroundColor = Color(0xFF000000),
            contentColor = Color(0xFFFFFFFF),
            durationMillis = 3000,
            onDismiss = {},
            modifier = Modifier
        )
}
