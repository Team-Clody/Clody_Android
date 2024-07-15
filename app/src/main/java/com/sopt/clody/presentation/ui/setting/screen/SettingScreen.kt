package com.sopt.clody.presentation.ui.setting.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sopt.clody.R
import com.sopt.clody.presentation.ui.setting.component.SettingOption
import com.sopt.clody.presentation.ui.setting.component.SettingSeparateLine
import com.sopt.clody.presentation.ui.setting.component.SettingTopAppBar
import com.sopt.clody.presentation.ui.setting.navigation.SettingNavigator
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun SettingRoute(
    navigator: SettingNavigator
) {
    SettingScreen(
        onClickBack = { navigator.navigateBack() },
        onClickAccountManagement = { navigator.navigateAccountManagement() },
        onClickNotificationSetting = { navigator.navigateNotificationSetting() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    onClickBack: () -> Unit,
    onClickAccountManagement: () -> Unit,
    onClickNotificationSetting: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { SettingTopAppBar(stringResource(R.string.setting_topappbar_title), onClickBack) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ClodyTheme.colors.white)
                .padding(innerPadding)
        ) {
            SettingOption(option = stringResource(R.string.setting_account_management), onClickAccountManagement)

            SettingSeparateLine()

            SettingOption(option = stringResource(R.string.setting_notification_setting), onClickNotificationSetting)
            SettingOption(option = stringResource(R.string.setting_announcement), { /* TODO : 공지사항 이동 */ })
            SettingOption(option = stringResource(R.string.setting_inquiries_suggestions), { /* TODO : 문의/제안하기 이동 */ })

            SettingSeparateLine()

            SettingOption(option = stringResource(R.string.setting_terms_of_service), { /* TODO : 서비스 이용 약관 이동 */ })
            SettingOption(option = stringResource(R.string.setting_privacy_policy), { /* TODO : 개인정보 처리방침 이동 */ })
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(24.dp))
                Text(
                    text = stringResource(R.string.setting_app_version),
                    style = ClodyTheme.typography.body1Medium
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "최신버전", /* TODO : 서버로부터 앱 버전 받아오기 */
                    modifier = Modifier.padding(end = 20.dp),
                    color = ClodyTheme.colors.gray05,
                    style = ClodyTheme.typography.body4Medium
                )
            }
        }
    }
}
