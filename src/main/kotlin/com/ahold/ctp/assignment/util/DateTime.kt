package com.ahold.ctp.assignment.util

import java.time.ZoneId
import java.time.ZonedDateTime

fun defaultZone(): ZoneId = ZoneId.of("Europe/Amsterdam")

// atStartOfDay() is only available for LocalDate
fun ZonedDateTime.startOfDay(): ZonedDateTime =
    this.toLocalDate().atStartOfDay(defaultZone())

fun ZonedDateTime.endOfDay(): ZonedDateTime =
    this.startOfDay().plusDays(1).minusNanos(1)


