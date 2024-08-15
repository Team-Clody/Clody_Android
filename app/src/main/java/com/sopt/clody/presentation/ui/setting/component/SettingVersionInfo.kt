package com.sopt.clody.presentation.ui.setting.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sopt.clody.R
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun SettingVersionInfo(
    versionInfo: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.setting_option_app_version),
            modifier = Modifier.padding(start = 4.dp),
            style = ClodyTheme.typography.body1Medium
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = versionInfo,
            color = ClodyTheme.colors.gray05,
            style = ClodyTheme.typography.body4Medium.copy(
                letterSpacing = 2.sp
            )
        )
    }
}
