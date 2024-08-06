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
import com.sopt.clody.R
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun SettingAppVersion() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
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
            text = stringResource(R.string.setting_app_version), /* TODO : 서버로부터 앱 버전 받아오기 */
            color = ClodyTheme.colors.gray05,
            style = ClodyTheme.typography.body4Medium
        )
    }
}
