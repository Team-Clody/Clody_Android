package com.sopt.clody.presentation.ui.setting.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sopt.clody.R
import com.sopt.clody.presentation.ui.component.dialog.ClodyDialog
import com.sopt.clody.presentation.ui.setting.component.LogoutDialog
import com.sopt.clody.presentation.ui.setting.component.NicknameChangeModalBottomSheet
import com.sopt.clody.presentation.ui.setting.component.SettingSeparateLine
import com.sopt.clody.presentation.ui.setting.component.SettingTopAppBar
import com.sopt.clody.presentation.ui.setting.navigation.SettingNavigator
import com.sopt.clody.ui.theme.ClodyTheme

@Composable
fun AccountManagementRoute(
    navigator: SettingNavigator
) {
    AccountManagementScreen(
        onBackClick = { navigator.navigateToBack() }
    )
}

@Composable
fun AccountManagementScreen(
    onBackClick: () -> Unit
) {
    var showChangeNicknameBottomSheet by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showRevokeDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { SettingTopAppBar(stringResource(R.string.account_management_topappbar_title), onBackClick) }
    ) { innerPadding ->
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
                    text = "사용자 이름", /* TODO : 사용자 이름 받아오기 */
                    style = ClodyTheme.typography.body1SemiBold
                )
                Text(
                    text = stringResource(R.string.account_management_user_name),
                    style = ClodyTheme.typography.body1Medium
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(R.string.account_management_button_nickname_change),
                    modifier = Modifier.clickable(onClick = { showChangeNicknameBottomSheet = true }),
                    color = ClodyTheme.colors.gray05,
                    style = ClodyTheme.typography.body4Medium
                )
            }

            Row(
                modifier = Modifier
                    .padding(top = 17.dp, bottom = 24.dp, start = 22.dp, end = 24.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_account_management_kakao),
                    modifier = Modifier
                        .size(24.dp),
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "사용자 아이디", style = ClodyTheme.typography.body1Medium) /* TODO : 사용자 아이디 받아오기 */
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(R.string.account_management_button_logout),
                    modifier = Modifier.clickable(onClick = { showLogoutDialog = true }),
                    color = ClodyTheme.colors.gray05,
                    style = ClodyTheme.typography.body4Medium
                )
            }

            SettingSeparateLine()

            Row(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = stringResource(R.string.account_management_text_revoke),
                    color = ClodyTheme.colors.gray05,
                    style = ClodyTheme.typography.body4Medium
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = stringResource(R.string.account_management_button_revoke),
                    modifier = Modifier.clickable(onClick = { showRevokeDialog = true }),
                    color = ClodyTheme.colors.gray05,
                    style = ClodyTheme.typography.body4Medium
                )
            }
        }
    }

    if (showChangeNicknameBottomSheet) {
        NicknameChangeModalBottomSheet(
            onDisMiss = { showChangeNicknameBottomSheet = false }
        )
    }

    if (showLogoutDialog) {
        LogoutDialog(
            onDismiss = { showLogoutDialog = false },
            titleMsg = stringResource(R.string.account_management_dialog_logout_title),
            descriptionMsg = stringResource(R.string.account_management_dialog_logout_description),
            confirmOpt = stringResource(R.string.account_management_dialog_logout_confirm),
            dismissOpt = stringResource(R.string.account_management_dialog_logout_dismiss),
            confirmAction = { /* 로그아웃 로직 함수 */ }
            confirmAction = { /* TODO : 로그아웃 로직 */ }
        )
    }

    if (showRevokeDialog) {
        ClodyDialog(
            onDismiss = { showRevokeDialog = false },
            titleMsg = stringResource(R.string.account_management_dialog_revoke_title),
            descriptionMsg = stringResource(R.string.account_management_dialog_revoke_description),
            confirmOpt = stringResource(R.string.account_management_dialog_revoke_confirm),
            dismissOpt = stringResource(R.string.account_management_dialog_revoke_dismiss),
            confirmAction = { /* 회원탈퇴 로직 함수 */ }
            confirmAction = { /* TODO : 회원탈퇴 로직  */ }
        )
    }
}
