package com.jamesellerbee.taskfireandroid.util

import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.TimeZone

fun Long.toDateString(): String {
    return DateTimeFormatter.ofPattern("MMM dd")
        .withZone(TimeZone.getDefault().toZoneId())
        .format(Instant.ofEpochMilli(this))
}

fun Long.toTimeString(): String {
    return DateTimeFormatter.ofPattern("h:mma")
        .withZone(TimeZone.getDefault().toZoneId())
        .format(Instant.ofEpochMilli(this))
}

fun Long.toDateTimeString(): String {
    return DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma")
        .withZone(TimeZone.getDefault().toZoneId())
        .format(Instant.ofEpochMilli(this))
}