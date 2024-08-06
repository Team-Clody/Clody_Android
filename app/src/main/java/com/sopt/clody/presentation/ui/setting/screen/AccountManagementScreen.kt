package com.sopt.clody.presentation.ui.setting.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sopt.clody.R
import com.sopt.clody.presentation.ui.component.dialog.ClodyDialog
import com.sopt.clody.presentation.ui.setting.component.LogoutDialog
import com.sopt.clody.presentation.ui.setting.component.NicknameChangeBottomSheet
import com.sopt.clody.presentation.ui.setting.component.SettingSeparateLine
import com.sopt.clody.presentation.ui.setting.component.SettingTopAppBar
import com.sopt.clody.presentation.ui.setting.navigation.SettingNavigator
import com.sopt.clody.presentation.utils.extension.showToast
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun AccountManagementRoute(
    navigator: SettingNavigator,
    accountManagementViewModel: AccountManagementViewModel = hiltViewModel()
) {
    AccountManagementScreen(
        accountManagementViewModel = accountManagementViewModel,
        onBackClick = { navigator.navigateBack() }
    )
}

@Composable
fun AccountManagementScreen(
    accountManagementViewModel: AccountManagementViewModel,
    onBackClick: () -> Unit
) {
    var showNicknameChangeBottomSheet by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showRevokeDialog by remember { mutableStateOf(false) }
    val userInfoState by accountManagementViewModel.userInfoState.collectAsState()
    var userName by remember { mutableStateOf("") }
    val userNicknameState by accountManagementViewModel.userNicknameState.collectAsState()

    LaunchedEffect(Unit) {
        accountManagementViewModel.fetchUserInfo()
    }

    LaunchedEffect(userNicknameState) {
        if (userNicknameState is UserNicknameState.Success) {
            accountManagementViewModel.fetchUserInfo()
        }
    }

    Scaffold(
        topBar = { SettingTopAppBar(stringResource(R.string.account_management_title), onBackClick) },
        containerColor = ClodyTheme.colors.white,
    ) { innerPadding ->
        when (userInfoState) {
            is UserInfoState.Idle -> {}
            is UserInfoState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(innerPadding)
                            .wrapContentSize(Alignment.Center),
                        color = ClodyTheme.colors.mainYellow
                    )
                }
            }

            is UserInfoState.Success -> {
                val userInfo = (userInfoState as UserInfoState.Success).data
                userName = userInfo.name

                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(top = 20.dp)
                )
                {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                            .padding(bottom = 17.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_account_management_clover),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = userInfo.name,
                            style = ClodyTheme.typography.body1SemiBold
                        )
                        Text(
                            text = stringResource(R.string.account_management_nickname),
                            style = ClodyTheme.typography.body1Medium
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = stringResource(R.string.account_management_nickname_change_button),
                            modifier = Modifier.clickable(onClick = { showNicknameChangeBottomSheet = true }),
                            color = ClodyTheme.colors.gray05,
                            style = ClodyTheme.typography.body4Medium
                        )
                    }

                    Row(
                        modifier = Modifier
                            .padding(top = 17.dp, bottom = 24.dp, start = 22.dp, end = 24.dp)
                    ) {
                        if (userInfo.platform == "kakao") {
                            Image(
                                painter = painterResource(id = R.drawable.img_account_management_kakao),
                                modifier = Modifier
                                    .size(24.dp),
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = userInfo.email,
                                style = ClodyTheme.typography.body1Medium
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Text(
                                text = stringResource(R.string.account_management_logout_button),
                                modifier = Modifier.clickable(onClick = { showLogoutDialog = true }),
                                color = ClodyTheme.colors.gray05,
                                style = ClodyTheme.typography.body4Medium
                            )
                        }
                    }

                    SettingSeparateLine()

                    Row(
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.account_management_revoke),
                            color = ClodyTheme.colors.gray05,
                            style = ClodyTheme.typography.body4Medium
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = stringResource(R.string.account_management_revoke_button),
                            modifier = Modifier.clickable(onClick = { showRevokeDialog = true }),
                            color = ClodyTheme.colors.gray05,
                            style = ClodyTheme.typography.body4Medium
                        )
                    }
                }
            }

            is UserInfoState.Failure -> {
                showToast(message = "${(userInfoState as UserInfoState.Failure).errorMessage}")
            }
        }
    }

    if (showNicknameChangeBottomSheet) {
        NicknameChangeBottomSheet(
            accountManagementViewModel = accountManagementViewModel,
            userName = userName,
            onDismiss = { showNicknameChangeBottomSheet = false }
        )
    }

    if (showLogoutDialog) {
        LogoutDialog(
            onDismiss = { showLogoutDialog = false },
            titleMassage = stringResource(R.string.account_management_logout_dialog_title),
            descriptionMassage = stringResource(R.string.account_management_logout_dialog_description),
            confirmOption = stringResource(R.string.account_management_logout_dialog_confirm),
            dismissOption = stringResource(R.string.account_management_logout_dialog_dismiss),
            confirmAction = { accountManagementViewModel.logOutAccount() }
        )
    }

    if (showRevokeDialog) {
        ClodyDialog(
            titleMassage = stringResource(R.string.account_management_revoke_dialog_title),
            descriptionMassage = stringResource(R.string.account_management_revoke_dialog_description),
            confirmOption = stringResource(R.string.account_management_revoke_dialog_confirm),
            dismissOption = stringResource(R.string.account_management_revoke_dialog_dismiss),
            confirmAction = { accountManagementViewModel.revokeAccount() },
            onDismiss = { showRevokeDialog = false },
            confirmButtonColor = ClodyTheme.colors.red,
            confirmButtonTextColor = ClodyTheme.colors.white
        )
    }
}
