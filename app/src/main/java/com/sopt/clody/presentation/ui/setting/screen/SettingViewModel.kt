package com.sopt.clody.presentation.ui.setting.screen

import android.content.Context
import android.content.pm.PackageInfo
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.clody.R
import com.sopt.clody.presentation.utils.language.LanguageProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val languageProvider: LanguageProvider,
) : ViewModel() {
    val noticeUrl = languageProvider.getWebViewUrlFor(SettingOptionUrls.NOTICES_URL)
    val supportFeedbackUrl = languageProvider.getWebViewUrlFor(SettingOptionUrls.SUPPORT_FEEDBACK_URL)
    val termsOfServiceUrl = languageProvider.getWebViewUrlFor(SettingOptionUrls.TERMS_OF_SERVICE_URL)
    val privacyPolicyUrl = languageProvider.getWebViewUrlFor(SettingOptionUrls.PRIVACY_POLICY_URL)

    var versionInfo by mutableStateOf<String?>(null)
        private set

    fun getVersionInfo() {
        viewModelScope.launch {
            runCatching {
                val info: PackageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
                info.versionName
            }.onSuccess { versionName ->
                versionInfo = versionName
            }.onFailure {
                versionInfo = context.getString(R.string.setting_option_app_version_info_failure)
            }
        }
    }
}
