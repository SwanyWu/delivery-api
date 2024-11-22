package com.ahold.ctp.assignment.util

import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

fun defaultZone(): ZoneId = ZoneId.of("Europe/Amsterdam")

fun LocalDate.startOfDay(): ZonedDateTime =
    this.atStartOfDay(defaultZone())

fun LocalDate.endOfDay(): ZonedDateTime =
    this.startOfDay().plusDays(1)
