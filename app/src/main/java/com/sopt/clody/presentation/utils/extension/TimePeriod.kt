package com.sopt.clody.presentation.utils.extension

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.sopt.clody.R

enum class TimePeriod(@StringRes val labelResId: Int) {
    AM(R.string.time_am),
    PM(R.string.time_pm),
    ;

    @Composable
    fun getLabel(): String = stringResource(labelResId)
}
