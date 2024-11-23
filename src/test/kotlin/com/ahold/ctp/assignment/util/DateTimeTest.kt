package com.ahold.ctp.assignment.util

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.ZoneId
import java.time.ZonedDateTime

class DateTimeTest {
    @Test
    fun `startOfDay returns start of day for given ZonedDateTime`() {
        val dateTime = ZonedDateTime.of(2024, 11, 23, 15, 30, 0, 0, ZoneId.of("Europe/Amsterdam"))
        val expectedStartOfDay = ZonedDateTime.of(2024, 11, 23, 0, 0, 0, 0, ZoneId.of("Europe/Amsterdam"))

        assertEquals(expectedStartOfDay, dateTime.startOfDay())
    }

    @Test
    fun `endOfDay returns end of day for given ZonedDateTime`() {
        val dateTime = ZonedDateTime.of(2024, 11, 23, 15, 30, 0, 0, ZoneId.of("Europe/Amsterdam"))
        val expectedEndOfDay = ZonedDateTime.of(2024, 11, 23, 23, 59, 59, 999_999_999, ZoneId.of("Europe/Amsterdam"))

        assertEquals(expectedEndOfDay, dateTime.endOfDay())
    }
}