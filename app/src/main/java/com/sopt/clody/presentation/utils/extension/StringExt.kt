package com.sopt.clody.presentation.utils.extension

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.sopt.clody.R

@Composable
fun String.convertTo12HourFormat(): String {
    val (hourBefore, minuteBefore) = this.split(":").map { it.toInt() }

    val amPm = if (hourBefore < 12) stringResource(R.string.time_am) else stringResource(R.string.time_pm)

    val hourAfter = when {
        hourBefore == 0 -> 12
        hourBefore > 12 -> hourBefore - 12
        else -> hourBefore
    }

    val minuteAfter = if (minuteBefore == 0) "00" else minuteBefore.toString()

    return String.format(stringResource(R.string.notification_setting_selected_time, amPm, hourAfter, minuteAfter))
}

fun Triple<String, String, String>.to24HourFormat(): String {
    val (amPm, hour, minute) = this
    val hourInt = when {
        amPm == "오후" && hour.toInt() != 12 -> hour.toInt() + 12
        amPm == "오전" && hour.toInt() == 12 -> 0
        else -> hour.toInt()
    }
    return String.format("%02d:%02d", hourInt, minute.toInt())
}
