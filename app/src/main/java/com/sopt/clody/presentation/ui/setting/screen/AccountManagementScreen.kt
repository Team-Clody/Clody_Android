package com.sopt.clody.presentation.ui.setting.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
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
import com.sopt.clody.presentation.ui.component.FailureScreen
import com.sopt.clody.presentation.ui.component.LoadingScreen
import com.sopt.clody.presentation.ui.component.dialog.ClodyDialog
import com.sopt.clody.presentation.ui.setting.component.AccountManagementLogout
import com.sopt.clody.presentation.ui.setting.component.AccountManagementNickname
import com.sopt.clody.presentation.ui.setting.component.AccountManagementRevoke
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
                LoadingScreen()
            }

            is UserInfoState.Success -> {
                val userInfo = (userInfoState as UserInfoState.Success).data
                userName = userInfo.name

                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                ) {
                    AccountManagementNickname(
                        userName = userInfo.name,
                        updateNicknameChangeBottomSheet = updateNicknameChangeBottomSheet
                    )

                    if (userInfo.platform == "kakao") {
                        AccountManagementLogout(
                            userEmail = userInfo.email,
                            updateLogoutDialog = updateLogoutDialog
                        )
                    }

                    SettingSeparateLine()

                    AccountManagementRevoke(
                        updateRevokeDialog = updateRevokeDialog
                    )
                }
            }

            is UserInfoState.Failure -> {
                FailureScreen()
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
