package com.hophey.jetgo.utils

import java.time.Duration
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

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

fun String.toDayAndMonth(locale: String): String {
    return try {
        val utcDateTime = OffsetDateTime.parse(this)
        utcDateTime.format(DateTimeFormatter.ofPattern("dd MMMM", Locale(locale)))
    } catch (e: Exception) {
        this
    }
}