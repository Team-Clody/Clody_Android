package com.sopt.clody.presentation.ui.writediary.component.topbar

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.sopt.clody.R
import com.sopt.clody.presentation.ui.writediary.component.button.SendButton
import com.sopt.clody.ui.theme.ClodyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WriteDiaryTopBar(
    modifier: Modifier = Modifier,
    onClickBack: () -> Unit,
    onClickSend: () -> Unit,
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
) {
    TopAppBar(
        modifier = modifier.fillMaxWidth(),
        title = {},
        navigationIcon = {
            IconButton(onClick = onClickBack) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_nickname_back),
                    contentDescription = "뒤로가기",
                    tint = ClodyTheme.colors.gray01,
                )
            }
        },
        actions = {
            SendButton(
                modifier = Modifier
                    .padding(horizontal = 12.dp),
                onClick = onClickSend,
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = ClodyTheme.colors.white,
        ),
        windowInsets = windowInsets,
    )
}
