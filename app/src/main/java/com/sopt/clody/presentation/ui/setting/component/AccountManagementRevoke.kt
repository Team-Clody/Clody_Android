package com.sopt.clody.presentation.ui.setting.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sopt.clody.R
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun AccountManagementRevoke(
    updateRevokeDialog: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.padding(24.dp)
    ) {
        Text(
            text = stringResource(R.string.account_management_revoke),
            color = ClodyTheme.colors.gray05,
            style = ClodyTheme.typography.body4Medium
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = stringResource(R.string.account_management_revoke_button),
            modifier = Modifier.clickable(onClick = { updateRevokeDialog(true) }),
            color = ClodyTheme.colors.gray05,
            style = ClodyTheme.typography.body4Medium
        )
    }
}
