package com.sopt.clody.presentation.ui.setting.screen

import android.content.Context
import android.content.Intent
import android.net.Uri
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
import com.sopt.clody.presentation.ui.setting.navigation.SettingNavigator
import com.sopt.clody.presentation.utils.amplitude.AmplitudeConstraints
import com.sopt.clody.presentation.utils.amplitude.AmplitudeUtils
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun SettingRoute(
    navigator: SettingNavigator,
    settingViewModel: SettingViewModel = hiltViewModel()
) {
    val versionInfo by settingViewModel::versionInfo

    LaunchedEffect(Unit) {
        settingViewModel.getVersionInfo()
        AmplitudeUtils.trackEvent(eventName = AmplitudeConstraints.SETTING)
    }

    SettingScreen(
        versionInfo = versionInfo ?: stringResource(R.string.setting_version_info_failure),
        onClickBack = { navigator.navigateBack() },
        onClickAccountManagement = { navigator.navigateAccountManagement() },
        onClickNotificationSetting = { navigator.navigateNotificationSetting() },
        onClickInquiriesSuggestions = { navigator.navigateWebView(SettingOptionUrls.INQUIRIES_SUGGESTIONS_URL) },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    versionInfo: String,
    onClickBack: () -> Unit,
    onClickAccountManagement: () -> Unit,
    onClickNotificationSetting: () -> Unit,
    onClickInquiriesSuggestions: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val context = LocalContext.current

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
            SettingOption(option = stringResource(R.string.setting_option_announcement)) { onClickSettingOption(context, SettingOptionUrls.ANNOUNCEMENT_URL) }
            SettingOption(option = stringResource(R.string.setting_option_inquiries_suggestions), onClickInquiriesSuggestions)

            SettingSeparateLine()

            SettingOption(option = stringResource(R.string.setting_option_terms_of_service)) { onClickSettingOption(context, SettingOptionUrls.TERMS_OF_SERVICE_URL) }
            SettingOption(option = stringResource(R.string.setting_option_privacy_policy)) { onClickSettingOption(context, SettingOptionUrls.PRIVACY_POLICY_URL) }
            SettingVersionInfo(versionInfo = versionInfo)
        }
    }
}

fun onClickSettingOption(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}
