package com.sopt.clody.presentation.utils.extension

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.sopt.clody.R

object YearMonthLabelUtil {
    const val MIN_YEAR = 2000
    const val MAX_YEAR = 2030
}

@Composable
fun Int.toLocalizedYearLabel(): String = stringResource(R.string.year_format, this)

@Composable
fun Int.toLocalizedMonthLabel(): String = stringResource(R.string.month_format, this)
