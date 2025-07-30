package com.sopt.clody.presentation.ui.setting.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sopt.clody.R
import com.sopt.clody.data.datastore.OAuthProvider
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun AccountManagementLogoutOption(
    userEmail: String,
    platform: OAuthProvider?,
    updateLogoutDialog: (Boolean) -> Unit,
) {
    val platformIconRes = when (platform) {
        OAuthProvider.KAKAO -> R.drawable.img_account_management_kakao
        OAuthProvider.GOOGLE -> R.drawable.img_google_button_logo
        else -> R.drawable.img_google_button_logo // 서버에서 google을 어떻게 내려줄까요?
    }

    Row(
        modifier = Modifier.padding(top = 12.dp, bottom = 24.dp, start = 22.dp, end = 24.dp),
    ) {
        Image(
            painter = painterResource(id = platformIconRes),
            modifier = Modifier.size(24.dp),
            contentDescription = null,
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = userEmail,
            color = ClodyTheme.colors.gray03,
            style = ClodyTheme.typography.body1Medium,
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = stringResource(R.string.account_management_btn_logout),
            modifier = Modifier.clickable(
                onClick = { updateLogoutDialog(true) },
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
            ),
            color = ClodyTheme.colors.gray05,
            style = ClodyTheme.typography.body4Medium,
        )
    }
}
