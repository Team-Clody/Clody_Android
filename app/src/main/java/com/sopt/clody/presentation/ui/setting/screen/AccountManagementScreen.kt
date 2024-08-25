package com.sopt.clody.presentation.ui.setting.screen

import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.sopt.clody.R
import com.sopt.clody.presentation.ui.component.FailureScreen
import com.sopt.clody.presentation.ui.component.LoadingScreen
import com.sopt.clody.presentation.ui.component.dialog.ClodyDialog
import com.sopt.clody.presentation.ui.component.dialog.FailureDialog
import com.sopt.clody.presentation.ui.component.toast.ClodyToastMessage
import com.sopt.clody.presentation.ui.setting.component.AccountManagementLogoutOption
import com.sopt.clody.presentation.ui.setting.component.AccountManagementNicknameOption
import com.sopt.clody.presentation.ui.setting.component.AccountManagementRevokeOption
import com.sopt.clody.presentation.ui.setting.component.LogoutDialog
import com.sopt.clody.presentation.ui.setting.component.NicknameChangeBottomSheet
import com.sopt.clody.presentation.ui.setting.component.SettingSeparateLine
import com.sopt.clody.presentation.ui.setting.component.SettingTopAppBar
import com.sopt.clody.presentation.ui.setting.navigation.SettingNavigator
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun AccountManagementRoute(
    navigator: SettingNavigator,
    accountManagementViewModel: AccountManagementViewModel = hiltViewModel()
) {
    val userInfoState by accountManagementViewModel.userInfoState.collectAsState()
    val userNicknameState by accountManagementViewModel.userNicknameState.collectAsState()
    val logOutState by accountManagementViewModel.logOutState.collectAsState()
    val revokeAccountState by accountManagementViewModel.revokeAccountState.collectAsState()
    var showNicknameChangeBottomSheet by remember { mutableStateOf(false) }
    val isValidNickname by accountManagementViewModel.isValidNickname.collectAsState()
    val nicknameMessage by accountManagementViewModel.nicknameMessage.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showRevokeDialog by remember { mutableStateOf(false) }
    val showFailureDialog by accountManagementViewModel.showFailureDialog.collectAsState()
    val failureDialogMessage by accountManagementViewModel.failureDialogMessage.collectAsState()

    LaunchedEffect(Unit) {
        accountManagementViewModel.fetchUserInfo()
    }

    LaunchedEffect(userNicknameState) {
        if (userNicknameState is UserNicknameState.Success) {
            accountManagementViewModel.fetchUserInfo()
        }
    }

    LaunchedEffect(revokeAccountState) {
        if (revokeAccountState is RevokeAccountState.Success) {
            navigator.navController.navigate("register_graph") {
                popUpTo("home") { inclusive = true }
            }
        }
    }

    LaunchedEffect(logOutState) {
        if (logOutState is LogOutState.Success) {
            navigator.navController.navigate("register_graph") {
                popUpTo("home") { inclusive = true }
            }
        }
    }

    AccountManagementScreen(
        accountManagementViewModel = accountManagementViewModel,
        userInfoState = userInfoState,
        userNicknameState = userNicknameState,
        showNicknameChangeBottomSheet = showNicknameChangeBottomSheet,
        updateNicknameChangeBottomSheet = { state -> showNicknameChangeBottomSheet = state },
        isValidNickname = isValidNickname,
        nicknameMessage = nicknameMessage,
        showLogoutDialog = showLogoutDialog,
        updateLogoutDialog = { state -> showLogoutDialog = state },
        showRevokeDialog = showRevokeDialog,
        updateRevokeDialog = { state -> showRevokeDialog = state },
        showFailureDialog = showFailureDialog,
        failureDialogMessage = failureDialogMessage,
        onBackClick = { navigator.navigateBack() }
    )
}

@Composable
fun AccountManagementScreen(
    accountManagementViewModel: AccountManagementViewModel,
    userInfoState: UserInfoState,
    userNicknameState: UserNicknameState,
    showNicknameChangeBottomSheet: Boolean,
    updateNicknameChangeBottomSheet: (Boolean) -> Unit,
    isValidNickname: Boolean,
    nicknameMessage: String,
    showLogoutDialog: Boolean,
    updateLogoutDialog: (Boolean) -> Unit,
    showRevokeDialog: Boolean,
    updateRevokeDialog: (Boolean) -> Unit,
    showFailureDialog: Boolean,
    failureDialogMessage: String,
    onBackClick: () -> Unit
) {
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
                val userInfo = userInfoState.data
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    AccountManagementNicknameOption(
                        userName = userInfo.name,
                        updateNicknameChangeBottomSheet = updateNicknameChangeBottomSheet
                    )

                    if (userInfo.platform == "kakao") {
                        AccountManagementLogoutOption(
                            userEmail = userInfo.email,
                            updateLogoutDialog = updateLogoutDialog
                        )
                    }

                    SettingSeparateLine()

                    AccountManagementRevokeOption(
                        updateRevokeDialog = updateRevokeDialog
                    )
                }
            }

            is UserInfoState.Failure -> {
                FailureScreen(
                    message = userInfoState.errorMessage,
                    confirmAction = { accountManagementViewModel.fetchUserInfo() }
                )
            }
        }
    }

    if (showNicknameChangeBottomSheet) {
        NicknameChangeBottomSheet(
            accountManagementViewModel = accountManagementViewModel,
            userName = (userInfoState as UserInfoState.Success).data.name,
            isValidNickname = isValidNickname,
            nicknameMessage = nicknameMessage,
            onDismiss = { updateNicknameChangeBottomSheet(false) }
        )
    }

    if (userNicknameState is UserNicknameState.Success) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            ClodyToastMessage(
                message = stringResource(R.string.account_management_nickname_change_toast),
                iconResId = R.drawable.ic_toast_check_on_18,
                backgroundColor = ClodyTheme.colors.gray04,
                contentColor = ClodyTheme.colors.white,
                durationMillis = 3000,
                onDismiss = { accountManagementViewModel.resetUserNicknameState() },
            )
        }
    }

    if (showLogoutDialog) {
        LogoutDialog(
            titleMassage = stringResource(R.string.account_management_logout_dialog_title),
            descriptionMassage = stringResource(R.string.account_management_logout_dialog_description),
            confirmOption = stringResource(R.string.account_management_logout_dialog_confirm),
            dismissOption = stringResource(R.string.account_management_logout_dialog_dismiss),
            confirmAction = { accountManagementViewModel.logOutAccount() },
            onDismiss = { updateLogoutDialog(false) }
        )
    }

    if (showRevokeDialog) {
        ClodyDialog(
            titleMassage = stringResource(R.string.account_management_revoke_dialog_title),
            descriptionMassage = stringResource(R.string.account_management_revoke_dialog_description),
            confirmOption = stringResource(R.string.account_management_revoke_dialog_confirm),
            dismissOption = stringResource(R.string.account_management_revoke_dialog_dismiss),
            confirmAction = { accountManagementViewModel.revokeAccount() },
            confirmButtonColor = ClodyTheme.colors.red,
            confirmButtonTextColor = ClodyTheme.colors.white,
            onDismiss = { updateRevokeDialog(false) }
        )
    }

    if (showFailureDialog) {
        FailureDialog(
            message = failureDialogMessage,
            onDismiss = { accountManagementViewModel.dismissFailureDialog() }
        )
    }
}
