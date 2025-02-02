package com.sopt.clody.presentation.ui.auth.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.sopt.clody.R
import com.sopt.clody.presentation.ui.auth.component.button.NextButton
import com.sopt.clody.presentation.ui.auth.component.checkbox.CustomCheckbox
import com.sopt.clody.presentation.ui.auth.navigation.AuthNavigator
import com.sopt.clody.presentation.ui.component.button.ClodyButton
import com.sopt.clody.presentation.ui.home.calendar.component.HorizontalDivider
import com.sopt.clody.presentation.ui.setting.screen.SettingOptionUrls
import com.sopt.clody.presentation.ui.setting.screen.onClickSettingOption
import com.sopt.clody.presentation.utils.extension.heightForScreenPercentage
import com.sopt.clody.ui.theme.ClodyTheme
import kotlinx.coroutines.delay

@Composable
fun TermsOfServiceRoute(
    navigator: AuthNavigator
) {
    var backPressCount by remember { mutableStateOf(0) }

    LaunchedEffect(backPressCount) {
        if (backPressCount > 0) {
            delay(2000) // 2 seconds delay
            backPressCount = 0
        }
    }

    BackHandler {
        if (backPressCount == 1) {
            navigator.navigateToSignupScreen()
        } else {
            backPressCount++
        }
    }
    TermsOfServiceScreen(
        onAgreeClick = { navigator.navigateNickname() },
        onBackClick = { navigator.navigateToSignupScreen() }
    )
}

@Composable
fun TermsOfServiceScreen(
    onAgreeClick: () -> Unit,
    onBackClick: () -> Unit
) {
    var allChecked by remember { mutableStateOf(false) }
    var serviceChecked by remember { mutableStateOf(false) }
    var privacyChecked by remember { mutableStateOf(false) }

    val isAgreeButtonEnabled = serviceChecked && privacyChecked
    val context = LocalContext.current

    Scaffold(
        topBar = {
            IconButton(
                onClick = { onBackClick() },
                modifier = Modifier
                    .padding(top = 20.dp)
                    .padding(start = 8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_nickname_back),
                    contentDescription = null
                )
            }
        },
        bottomBar = {
            ClodyButton(
                onClick = onAgreeClick,
                text = stringResource(id = R.string.terms_next),
                enabled = isAgreeButtonEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 28.dp)
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = ClodyTheme.colors.white)
                    .padding(paddingValues)
                    .padding(horizontal = 24.dp)
            ) {
                Spacer(modifier = Modifier.heightForScreenPercentage(0.056f))
                Text(
                    text = stringResource(id = R.string.terms_title),
                    style = ClodyTheme.typography.head1,
                    color = ClodyTheme.colors.gray01,
                )
                Spacer(modifier = Modifier.heightForScreenPercentage(0.06f))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.terms_agree_all),
                        style = ClodyTheme.typography.head3,
                        color = ClodyTheme.colors.gray01,
                        modifier = Modifier.weight(1f)
                    )
                    CustomCheckbox(
                        checked = allChecked,
                        onCheckedChange = { checked ->
                            allChecked = checked
                            serviceChecked = checked
                            privacyChecked = checked
                        },
                        size = 25.dp,
                        checkedImageRes = R.drawable.ic_terms_check_on_25,
                        uncheckedImageRes = R.drawable.ic_terms_check_off_25
                    )
                }

                Spacer(modifier = Modifier.heightForScreenPercentage(0.02f))
                HorizontalDivider(color = ClodyTheme.colors.gray07, thickness = 1.dp)
                Spacer(modifier = Modifier.heightForScreenPercentage(0.02f))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        stringResource(id = R.string.terms_service_use),
                        style = ClodyTheme.typography.body1Medium,
                        color = ClodyTheme.colors.gray01
                    )
                    NextButton(
                        onClick = { onClickSettingOption(context, SettingOptionUrls.TERMS_OF_SERVICE_URL) },
                        imageResource = R.drawable.ic_terms_next,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    CustomCheckbox(
                        checked = serviceChecked,
                        onCheckedChange = { checked ->
                            serviceChecked = checked
                            if (!checked) allChecked = false
                            if (checked && privacyChecked) allChecked = true
                        },
                        size = 23.dp,
                        checkedImageRes = R.drawable.ic_terms_check_on_23,
                        uncheckedImageRes = R.drawable.ic_terms_check_off_23
                    )
                }

                Spacer(modifier = Modifier.heightForScreenPercentage(0.02f))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        stringResource(id = R.string.terms_service_privacy),
                        style = ClodyTheme.typography.body1Medium,
                        color = ClodyTheme.colors.gray01
                    )
                    NextButton(
                        onClick = { onClickSettingOption(context, SettingOptionUrls.PRIVACY_POLICY_URL) },
                        imageResource = R.drawable.ic_terms_next,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    CustomCheckbox(
                        checked = privacyChecked,
                        onCheckedChange = { checked ->
                            privacyChecked = checked
                            if (!checked) allChecked = false
                            if (checked && serviceChecked) allChecked = true
                        },
                        size = 23.dp,
                        checkedImageRes = R.drawable.ic_terms_check_on_23,
                        uncheckedImageRes = R.drawable.ic_terms_check_off_23
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun TermsOfServiceScreenPreview() {
    TermsOfServiceScreen(
        onAgreeClick = { },
        onBackClick = { }
    )
}
