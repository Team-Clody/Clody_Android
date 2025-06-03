package com.sopt.clody.presentation.utils.extension

fun String.convertTo12HourFormat(): String {
    val (hourBefore, minuteBefore) = this.split(":").map { it.toInt() }

    val amPm = if (hourBefore < 12) "오전" else "오후"

    val hourAfter = when {
        hourBefore == 0 -> 12
        hourBefore > 12 -> hourBefore - 12
        else -> hourBefore
    }

    val minuteAfter = if (minuteBefore == 0) "00" else minuteBefore

    return String.format("$amPm ${hourAfter}시 ${minuteAfter}분")
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
