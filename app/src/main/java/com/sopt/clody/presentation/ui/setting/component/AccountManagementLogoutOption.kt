package com.sopt.clody.presentation.ui.setting.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sopt.clody.R
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun AccountManagementLogoutOption(
    userEmail: String,
    updateLogoutDialog: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(top = 12.dp, bottom = 24.dp, start = 22.dp, end = 24.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_account_management_kakao),
            modifier = Modifier
                .size(24.dp),
            contentDescription = null
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = userEmail,
            style = ClodyTheme.typography.body1Medium
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = stringResource(R.string.account_management_logout_button),
            modifier = Modifier.clickable(onClick = { updateLogoutDialog(true) }),
            color = ClodyTheme.colors.gray05,
            style = ClodyTheme.typography.body4Medium
        )

    }
}
