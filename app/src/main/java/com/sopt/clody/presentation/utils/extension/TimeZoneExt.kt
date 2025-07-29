package com.sopt.clody.presentation.utils.extension

import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

/**
*   @param time 서버로부터 수신받은 시간으로 "21:30" 와 같은 형태로 전달받는다.
* */
fun convertKSTtoUTZ(time: String): Triple<TimePeriod, Int, Int> {
    val kstZoneId = ZoneId.of("Asia/Seoul")
    val userZoneId = ZoneId.systemDefault()

    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    val localTime = LocalTime.parse(time, formatter)

    val kstDateTime = ZonedDateTime.of(LocalDate.now(kstZoneId), localTime, kstZoneId)
    val userDateTime = kstDateTime.withZoneSameInstant(userZoneId)

    val hour24 = userDateTime.hour
    val minute = userDateTime.minute
    val timePeriod = if (hour24 < 12) TimePeriod.AM else TimePeriod.PM
    val hour12 = when {
        hour24 == 0 -> 12
        hour24 > 12 -> hour24 - 12
        else -> hour24
    }

    return Triple(timePeriod, hour12, minute)
}

/**
 *  @param timePeriod 오전/오후
 *  @param hour 시간
 *  @param minute 분
 * */
fun convertUTZtoKST(timePeriod: TimePeriod, hour: String, minute: String): String {
    val userZoneId = ZoneId.systemDefault()
    val kstZoneId = ZoneId.of("Asia/Seoul")

    val hour24 = when (timePeriod) {
        TimePeriod.AM -> if (hour == "12") 0 else hour.toInt()
        TimePeriod.PM -> if (hour == "12") 12 else (hour + 12).toInt()
    }

    val userTime = LocalTime.of(hour24, minute.toInt())
    val userZoned = ZonedDateTime.of(LocalDate.now(userZoneId), userTime, userZoneId)
    val kstZoned = userZoned.withZoneSameInstant(kstZoneId)

    val kstHour = kstZoned.hour
    val kstMinute = kstZoned.minute

    return String.format("%02s:%02s", kstHour, kstMinute)
}
