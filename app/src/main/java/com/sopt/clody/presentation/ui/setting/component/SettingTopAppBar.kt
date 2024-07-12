package com.sopt.clody.presentation.ui.setting.component

import androidx.compose.foundation.Image
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.sopt.clody.R
import com.sopt.clody.ui.theme.ClodyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingTopAppBar(
    title: String,
    onClickBack: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    CenterAlignedTopAppBar(
        title = {
            Text(text = title, style = ClodyTheme.typography.head4)
        },
        navigationIcon = {
            IconButton(onClick = onClickBack) {
                Image(
                    painter = painterResource(id = R.drawable.ic_setting_back),
                    contentDescription = null
                )
            }
        },
        scrollBehavior = scrollBehavior,
    )
}
