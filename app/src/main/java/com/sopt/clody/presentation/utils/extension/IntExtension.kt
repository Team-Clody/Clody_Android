package com.sopt.clody.presentation.utils.extension

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.sopt.clody.R
import kotlinx.datetime.Month
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun Int.toLocalizedYearLabel(): String = stringResource(R.string.year_format, this)

@Composable
fun Int.toLocalizedMonthLabel(): String {
    val locale = LocalContext.current.resources.configuration.locales[0]
    return if (locale.language == "ko") {
        stringResource(R.string.month_format, this)
    } else {
        Month.of(this).getDisplayName(TextStyle.FULL, Locale.ENGLISH)
            .lowercase().replaceFirstChar { it.titlecase() }
    }
}
