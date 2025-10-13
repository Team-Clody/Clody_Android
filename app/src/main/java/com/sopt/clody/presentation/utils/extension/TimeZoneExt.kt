package com.sopt.clody.presentation.utils.extension

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

/**
 *   @param time 서버로부터 수신받은 시간으로 "21:30" 와 같은 형태로 전달받는다.
 * */
fun convertKSTtoUTZ(time: String, referenceDate: LocalDate = LocalDate.now()): Triple<TimePeriod, String, String> {
    val kstZoneId = ZoneId.of("Asia/Seoul")
    val userZoneId = ZoneId.systemDefault()

    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    val localTime = LocalTime.parse(time, formatter)

    val kstDateTime = ZonedDateTime.of(referenceDate, localTime, kstZoneId)
    val userDateTime = kstDateTime.withZoneSameInstant(userZoneId)

    val hour24 = userDateTime.hour
    val minute = userDateTime.minute

    val timePeriod = if (hour24 < 12) TimePeriod.AM else TimePeriod.PM
    val hour12 = when {
        hour24 == 0 -> 12
        hour24 > 12 -> hour24 - 12
        else -> hour24
    }

    val hourFormatted = hour12.toString()
    val minuteFormatted = String.format("%02d", minute)

    return Triple(timePeriod, hourFormatted, minuteFormatted)
}

/**
 *  @param timePeriod 오전/오후
 *  @param hour 시간
 *  @param minute 분
 * */
fun convertUTZtoKST(timePeriod: TimePeriod, hour: String, minute: String, referenceDate: LocalDate = LocalDate.now()): String {
    val userZoneId = ZoneId.systemDefault()
    val kstZoneId = ZoneId.of("Asia/Seoul")

    val hour24 = when (timePeriod) {
        TimePeriod.AM -> if (hour == "12") 0 else hour.toInt()
        TimePeriod.PM -> if (hour == "12") 12 else hour.toInt() + 12
    }

    val userTime = LocalTime.of(hour24, minute.toInt())
    val userZoned = ZonedDateTime.of(referenceDate, userTime, userZoneId)
    val kstZoned = userZoned.withZoneSameInstant(kstZoneId)

    val kstHour = kstZoned.hour
    val kstMinute = kstZoned.minute

    return String.format(java.util.Locale.ROOT, "%02d:%02d", kstHour, kstMinute)
}

/**
 *  일기작성 API 호출 시 유저의 현재 시점(연/월/일/시각)을 KST 시간대로 변환 후 "yyyy-MM-dd'T'HH:mm:ss" 형식으로 전달한다.
 *  @param year 작성된 일기의 연도
 *  @param month 작성된 일기의 월
 *  @param day 작성된 일기의 일
 * */
fun convertDateToKstDateTime(year: Int, month: Int, day: Int): String {
    val userZone = ZoneId.systemDefault()
    val kstZone = ZoneId.of("Asia/Seoul")

    val userNow = ZonedDateTime.now(userZone)
    val userDateTime = LocalDateTime.of(
        LocalDate.of(year, month, day),
        userNow.toLocalTime(),
    ).atZone(userZone)

    val kstDateTime = userDateTime.withZoneSameInstant(kstZone)

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss")
    return kstDateTime.format(formatter)
}
