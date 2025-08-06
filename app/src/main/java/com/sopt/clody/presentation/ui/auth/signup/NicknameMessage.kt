package com.sopt.clody.presentation.ui.auth.signup

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.sopt.clody.R

enum class NicknameMessage(
    @StringRes val message: Int,
) {
    DEFAULT(R.string.nickname_message_default),
    INVALID(R.string.nickname_message_invalid),
    ;

    @Composable
    fun getMessage(): String = stringResource(message)
}
