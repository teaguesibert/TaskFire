package com.jamesellerbee.taskfireandroid.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.TimeZone

fun Long.toDateString(zoneId: ZoneId = TimeZone.getDefault().toZoneId()): String {
    return DateTimeFormatter.ofPattern("MMM dd")
        .withZone(zoneId)
        .format(Instant.ofEpochMilli(this))
}

fun Long.toTimeString(zoneId: ZoneId = TimeZone.getDefault().toZoneId()): String {
    return DateTimeFormatter.ofPattern("h:mma")
        .withZone(zoneId)
        .format(Instant.ofEpochMilli(this))
}

fun Long.toDateTimeString(zoneId: ZoneId = TimeZone.getDefault().toZoneId()): String {
    return DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma")
        .withZone(zoneId)
        .format(Instant.ofEpochMilli(this))
}