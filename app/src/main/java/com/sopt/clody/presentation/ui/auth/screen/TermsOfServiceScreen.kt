package com.sopt.clody.presentation.ui.auth.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.sopt.clody.R
import com.sopt.clody.presentation.ui.auth.component.button.NextButton
import com.sopt.clody.presentation.ui.auth.component.checkbox.CustomCheckbox
import com.sopt.clody.presentation.ui.auth.navigation.AuthNavigator
import com.sopt.clody.presentation.ui.component.button.ClodyButton
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

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = ClodyTheme.colors.white)
            .padding(12.dp)
    ) {
        val (backButton, title, allAgreeRow, spacer, serviceAgreeRow, nextButton, privacyAgreeRow, agreeButton) = createRefs()
        val guideline = createGuidelineFromTop(0.125f)

        IconButton(
            onClick = { onBackClick() },
            modifier = Modifier
                .padding(top = 8.dp)
                .constrainAs(backButton) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_nickname_back),
                contentDescription = null,
            )
        }

        Text(
            text = stringResource(id = R.string.terms_title),
            style = ClodyTheme.typography.head1,
            color = ClodyTheme.colors.gray01,
            modifier = Modifier.constrainAs(title) {
                top.linkTo(guideline)
                start.linkTo(parent.start, margin = 12.dp)
            }
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .constrainAs(allAgreeRow) {
                    top.linkTo(title.bottom, margin = 52.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
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

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .constrainAs(spacer) {
                    top.linkTo(allAgreeRow.bottom, margin = 16.dp)
                    start.linkTo(parent.start,)
                    end.linkTo(parent.end)
                }
                .height(1.dp)
                .background(color = ClodyTheme.colors.gray07)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .constrainAs(serviceAgreeRow) {
                    top.linkTo(spacer.bottom, margin = 16.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
            Text(
                stringResource(id = R.string.terms_service_use),
                style = ClodyTheme.typography.body1Medium,
                color = ClodyTheme.colors.gray01
            )
            NextButton(
                onClick = { /*TODO : 이용약관으로 이동 */ },
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

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
                .constrainAs(privacyAgreeRow) {
                    top.linkTo(serviceAgreeRow.bottom, margin = 22.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
            Text(stringResource(
                id = R.string.terms_service_privacy),
                style = ClodyTheme.typography.body1Medium,
                color = ClodyTheme.colors.gray01
            )
            NextButton(
                onClick = { /*TODO : 개인정보 처리방침으로 이동 */ },
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

        ClodyButton(
            onClick = onAgreeClick,
            text = stringResource(id = R.string.terms_next),
            enabled = isAgreeButtonEnabled,
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .constrainAs(agreeButton) {
                bottom.linkTo(parent.bottom, margin = 28.dp)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TermsOfServiceScreenPreview() {
    TermsOfServiceScreen(
        onAgreeClick = { },
        onBackClick = { }
    )
}

