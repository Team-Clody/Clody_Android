package com.sopt.clody.presentation.ui.setting.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.sopt.clody.R
import com.sopt.clody.presentation.ui.setting.component.SettingOption
import com.sopt.clody.presentation.ui.setting.component.SettingSeparateLine
import com.sopt.clody.presentation.ui.setting.component.SettingTopAppBar
import com.sopt.clody.presentation.ui.setting.component.SettingVersionInfo
import com.sopt.clody.presentation.utils.amplitude.AmplitudeConstraints
import com.sopt.clody.presentation.utils.amplitude.AmplitudeUtils
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun SettingRoute(
    navigateToAccountManagement: () -> Unit,
    navigateToNotification: () -> Unit,
    navigateToPrevious: () -> Unit,
    navigateToWebView: (String) -> Unit,
    settingViewModel: SettingViewModel = hiltViewModel(),
) {
    val versionInfo by settingViewModel::versionInfo

    LaunchedEffect(Unit) {
        settingViewModel.getVersionInfo()
        AmplitudeUtils.trackEvent(eventName = AmplitudeConstraints.SETTING)
    }

    SettingScreen(
        versionInfo = versionInfo ?: stringResource(R.string.setting_version_info_failure),
        onClickBack = navigateToPrevious,
        onClickAccountManagement = navigateToAccountManagement,
        onClickNotificationSetting = navigateToNotification,
        onClickAnnouncement = { navigateToWebView(SettingOptionUrls.ANNOUNCEMENT_URL) },
        onClickInquiriesSuggestions = { navigateToWebView(SettingOptionUrls.INQUIRIES_SUGGESTIONS_URL) },
        onClickTerms = { navigateToWebView(SettingOptionUrls.TERMS_OF_SERVICE_URL) },
        onClickPrivacy = { navigateToWebView(SettingOptionUrls.PRIVACY_POLICY_URL) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    versionInfo: String,
    onClickBack: () -> Unit,
    onClickAccountManagement: () -> Unit,
    onClickNotificationSetting: () -> Unit,
    onClickAnnouncement: () -> Unit,
    onClickInquiriesSuggestions: () -> Unit,
    onClickTerms: () -> Unit,
    onClickPrivacy: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { SettingTopAppBar(stringResource(R.string.setting_title), onClickBack) },
        containerColor = ClodyTheme.colors.white,
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            SettingOption(option = stringResource(R.string.setting_option_account_management), onClickAccountManagement)

            SettingSeparateLine()

            SettingOption(
                option = stringResource(R.string.setting_option_notification_setting),
                onClickNotificationSetting,
            )
            SettingOption(option = stringResource(R.string.setting_option_announcement), onClickAnnouncement)
            SettingOption(option = stringResource(R.string.setting_option_inquiries_suggestions), onClickInquiriesSuggestions)

            SettingSeparateLine()

            SettingOption(option = stringResource(R.string.setting_option_terms_of_service), onClickTerms)
            SettingOption(option = stringResource(R.string.setting_option_privacy_policy), onClickPrivacy)

            SettingVersionInfo(versionInfo = versionInfo)
        }
    }
}
