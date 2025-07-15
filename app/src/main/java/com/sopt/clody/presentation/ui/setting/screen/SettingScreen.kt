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
import java.util.Locale

@Composable
fun SettingRoute(
    navigateToAccountManagement: () -> Unit,
    navigateToNotification: () -> Unit,
    navigateToPrevious: () -> Unit,
    navigateToWebView: (String) -> Unit,
    settingViewModel: SettingViewModel = hiltViewModel(),
) {
    val currentLang = Locale.getDefault().language
    val notice = if (currentLang == "ko") SettingOptionUrls.NOTICES_URL.koUrl else SettingOptionUrls.NOTICES_URL.enUrl
    val supportFeedback = if (currentLang == "ko") SettingOptionUrls.SUPPORT_FEEDBACK_URL.koUrl else SettingOptionUrls.SUPPORT_FEEDBACK_URL.enUrl
    val termsOfService = if (currentLang == "ko") SettingOptionUrls.TERMS_OF_SERVICE_URL.koUrl else SettingOptionUrls.TERMS_OF_SERVICE_URL.enUrl
    val privacyPolicy = if (currentLang == "ko") SettingOptionUrls.PRIVACY_POLICY_URL.koUrl else SettingOptionUrls.PRIVACY_POLICY_URL.enUrl

    val versionInfo by settingViewModel::versionInfo

    LaunchedEffect(Unit) {
        settingViewModel.getVersionInfo()
        AmplitudeUtils.trackEvent(eventName = AmplitudeConstraints.SETTING)
    }

    SettingScreen(
        versionInfo = versionInfo ?: stringResource(R.string.setting_option_app_version_info_failure),
        onClickBack = navigateToPrevious,
        onClickAccountManagement = navigateToAccountManagement,
        onClickNotificationSetting = navigateToNotification,
        onClickNotice = { navigateToWebView(notice) },
        onClickSupportFeedback = { navigateToWebView(supportFeedback) },
        onClickTerms = { navigateToWebView(termsOfService) },
        onClickPrivacy = { navigateToWebView(privacyPolicy) },
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
