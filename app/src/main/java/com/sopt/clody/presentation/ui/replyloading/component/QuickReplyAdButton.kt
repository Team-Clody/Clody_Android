package com.sopt.clody.presentation.ui.replyloading.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sopt.clody.R
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun QuickReplyAdButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .background(ClodyTheme.colors.lightBlue)
            .clickable { onClick() }
            .padding(vertical = 6.dp, horizontal = 12.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_replyloading_player),
                contentDescription = null,
                tint = Color.Unspecified,
            )
            Text(
                text = stringResource(R.string.reply_loading_btn_ad_request),
                style = ClodyTheme.typography.body4Medium,
                color = ClodyTheme.colors.blue,
                modifier = Modifier.padding(start = 5.dp),
            )
        }
    }
}

@Composable
@Preview
fun QuickReplyAdButtonPreview() {
    QuickReplyAdButton()
}
