package com.sopt.clody.presentation.ui.auth.signup.page

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sopt.clody.R
import com.sopt.clody.presentation.ui.auth.component.checkbox.CustomCheckbox
import com.sopt.clody.presentation.ui.component.button.ClodyButton
import com.sopt.clody.presentation.ui.home.calendar.component.HorizontalDivider
import com.sopt.clody.presentation.ui.setting.screen.SettingOptionUrls
import com.sopt.clody.presentation.utils.base.BasePreview
import com.sopt.clody.presentation.utils.base.ClodyPreview
import com.sopt.clody.presentation.utils.extension.heightForScreenPercentage
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun TermsOfServicePage(
    allChecked: Boolean,
    serviceChecked: Boolean,
    privacyChecked: Boolean,
    onToggleAll: (Boolean) -> Unit,
    onToggleService: (Boolean) -> Unit,
    onTogglePrivacy: (Boolean) -> Unit,
    onAgreeClick: () -> Unit,
    navigateToPrevious: () -> Unit,
    navigateToWebView: (String) -> Unit,
) {
    val isAgreeButtonEnabled = serviceChecked && privacyChecked

    Scaffold(
        topBar = {
            IconButton(
                onClick = navigateToPrevious,
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(start = 8.dp),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_nickname_back),
                    contentDescription = null,
                )
            }
        },
        bottomBar = {
            ClodyButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 28.dp),
                onClick = onAgreeClick,
                text = stringResource(R.string.terms_next),
                enabled = isAgreeButtonEnabled,
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = ClodyTheme.colors.white)
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp),
            ) {
                Spacer(modifier = Modifier.heightForScreenPercentage(0.056f))
                Text(
                    text = stringResource(R.string.terms_title),
                    color = ClodyTheme.colors.gray01,
                    style = ClodyTheme.typography.head1,
                )
                Spacer(modifier = Modifier.heightForScreenPercentage(0.06f))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.terms_agree_all),
                        modifier = Modifier.weight(1f),
                        color = ClodyTheme.colors.gray01,
                        style = ClodyTheme.typography.head3,
                    )
                    CustomCheckbox(
                        checked = allChecked,
                        onCheckedChange = onToggleAll,
                        size = 25.dp,
                        checkedImageRes = R.drawable.ic_terms_check_on_25,
                        uncheckedImageRes = R.drawable.ic_terms_check_off_25,
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = ClodyTheme.colors.gray07, thickness = 1.dp)
                Spacer(modifier = Modifier.height(16.dp))
                TermsCheckboxRow(
                    text = stringResource(R.string.terms_service_use),
                    checked = serviceChecked,
                    onCheckedChange = onToggleService,
                    onClickMore = { navigateToWebView(SettingOptionUrls.TERMS_OF_SERVICE_URL) },
                )
                Spacer(modifier = Modifier.height(8.dp))
                TermsCheckboxRow(
                    text = stringResource(R.string.terms_service_privacy),
                    checked = privacyChecked,
                    onCheckedChange = onTogglePrivacy,
                    onClickMore = { navigateToWebView(SettingOptionUrls.PRIVACY_POLICY_URL) },
                )
            }
        },
    )
}

@Composable
fun TermsCheckboxRow(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onClickMore: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(text, style = ClodyTheme.typography.body1Medium)
        Spacer(modifier = Modifier.width(8.dp))
        Image(
            painter = painterResource(id = R.drawable.ic_terms_next),
            contentDescription = null,
            modifier = Modifier
                .clickable(onClick = onClickMore),
        )
        Spacer(modifier = Modifier.weight(1f))
        CustomCheckbox(
            checked = checked,
            onCheckedChange = onCheckedChange,
            size = 23.dp,
            checkedImageRes = R.drawable.ic_terms_check_on_23,
            uncheckedImageRes = R.drawable.ic_terms_check_off_23,
        )
    }
}

@ClodyPreview
@Composable
private fun TermsOfServicePagePreview() {
    BasePreview {
        TermsOfServicePage(
            allChecked = false,
            serviceChecked = false,
            privacyChecked = false,
            onToggleAll = {},
            onToggleService = {},
            onTogglePrivacy = {},
            onAgreeClick = {},
            navigateToPrevious = {},
            navigateToWebView = {},
        )
    }
}
