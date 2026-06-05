package com.hophey.jetgo.utils

import java.time.Duration
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun String.toAirportLocalTime(utcDiff: Int): String {
    return try {
        val utcDateTime = OffsetDateTime.parse(this)
        val zoneOffset = ZoneOffset.ofHours(utcDiff)
        val airportLocalTime = utcDateTime.withOffsetSameInstant(zoneOffset)
        airportLocalTime.format(DateTimeFormatter.ofPattern("HH:mm"))
    } catch (e: Exception) {
        this
    }
}

fun String.getTimeDiffInHoursFormatted(otherTime: String): String {
    return try {
        val time1 = OffsetDateTime.parse(this)
        val time2 = OffsetDateTime.parse(otherTime)

        val duration = Duration.between(time1, time2).abs()
        val hours = duration.toMinutes() / 60.0

        String.format("%.1f", hours)
    } catch (e: Exception) {
        "0.0 ч"
    }
}

fun String.toDayAndMonth(utcDiff: Int): String {
    return try {
        val utcDateTime = OffsetDateTime.parse(this)
        val zoneOffset = ZoneOffset.ofHours(utcDiff)
        val airportLocalTime = utcDateTime.withOffsetSameInstant(zoneOffset)
        airportLocalTime.format(DateTimeFormatter.ofPattern("dd MMMM"))
    } catch (e: Exception) {
        this
    }
}

fun String.toDayAndMonth(): String{
    return try {
        val formatter = DateTimeFormatter.ofPattern("dd MMMM")
        val date = LocalDate.parse(this)
        date.format(formatter)
    } catch (e: Exception){
        this
    }
}