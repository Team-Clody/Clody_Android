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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.sopt.clody.R
import com.sopt.clody.presentation.ui.setting.component.SettingOption
import com.sopt.clody.presentation.ui.setting.component.SettingSeparateLine
import com.sopt.clody.presentation.ui.setting.component.SettingTopAppBar
import com.sopt.clody.presentation.ui.setting.component.SettingVersionInfo
import com.sopt.clody.presentation.utils.amplitude.AmplitudeConstraints
import com.sopt.clody.presentation.utils.amplitude.AmplitudeUtils
import com.sopt.clody.presentation.utils.openExternalBrowser
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun SettingRoute(
    navigateToAccountManagement: () -> Unit,
    navigateToNotification: () -> Unit,
    navigateToPrevious: () -> Unit,
    settingViewModel: SettingViewModel = hiltViewModel(),
) {
    val notice by settingViewModel::noticeUrl
    val supportFeedback by settingViewModel::supportFeedbackUrl
    val termsOfService by settingViewModel::termsOfServiceUrl
    val privacyPolicy by settingViewModel::privacyPolicyUrl
    val versionInfo by settingViewModel::versionInfo
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        settingViewModel.getVersionInfo()
        AmplitudeUtils.trackEvent(eventName = AmplitudeConstraints.SETTING)
    }

    SettingScreen(
        versionInfo = versionInfo ?: stringResource(R.string.setting_option_app_version_info_failure),
        onClickBack = navigateToPrevious,
        onClickAccountManagement = navigateToAccountManagement,
        onClickNotificationSetting = navigateToNotification,
        onClickNotice = { openExternalBrowser(context, notice) },
        onClickSupportFeedback = { openExternalBrowser(context, supportFeedback) },
        onClickTerms = { openExternalBrowser(context, termsOfService) },
        onClickPrivacy = { openExternalBrowser(context, privacyPolicy) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    versionInfo: String,
    onClickBack: () -> Unit,
    onClickAccountManagement: () -> Unit,
    onClickNotificationSetting: () -> Unit,
    onClickNotice: () -> Unit,
    onClickSupportFeedback: () -> Unit,
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

            SettingOption(option = stringResource(R.string.setting_option_notification_setting), onClickNotificationSetting)
            SettingOption(option = stringResource(R.string.setting_option_announcement), onClickNotice)
            SettingOption(option = stringResource(R.string.setting_option_inquiries_suggestions), onClickSupportFeedback)

            SettingSeparateLine()

            SettingOption(option = stringResource(R.string.setting_option_terms_of_service), onClickTerms)
            SettingOption(option = stringResource(R.string.setting_option_privacy_policy), onClickPrivacy)

            SettingVersionInfo(versionInfo = versionInfo)
        }
    }
}
