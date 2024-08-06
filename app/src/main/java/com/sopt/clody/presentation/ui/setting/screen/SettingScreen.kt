package com.sopt.clody.presentation.ui.setting.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sopt.clody.R
import com.sopt.clody.presentation.ui.setting.component.SettingAppVersion
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
        topBar = { SettingTopAppBar(stringResource(R.string.setting_title), onClickBack) },
        containerColor = ClodyTheme.colors.white,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
        ) {
            SettingOption(option = stringResource(R.string.setting_option_account_management), onClickAccountManagement)

            SettingSeparateLine()

            SettingOption(option = stringResource(R.string.setting_option_notification_setting), onClickNotificationSetting)
            SettingOption(option = stringResource(R.string.setting_option_announcement), { /* TODO : 공지사항 이동 */ })
            SettingOption(option = stringResource(R.string.setting_option_inquiries_suggestions), { /* TODO : 문의/제안하기 이동 */ })

            SettingSeparateLine()

            SettingOption(option = stringResource(R.string.setting_option_terms_of_service), { /* TODO : 서비스 이용 약관 이동 */ })
            SettingOption(option = stringResource(R.string.setting_option_privacy_policy), { /* TODO : 개인정보 처리방침 이동 */ })
            SettingAppVersion()
        }
    }
}
