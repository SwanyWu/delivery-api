package com.ahold.ctp.assignment.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime
import java.time.ZoneOffset

class DateTimeTest {
    @Test
    fun `startOfDay returns start of day for given ZonedDateTime`() {
        val dateTime = OffsetDateTime.of(2024, 11, 23, 23, 42, 0, 0, ZoneOffset.UTC )
        val expectedStartOfDay = OffsetDateTime.of(2024, 11, 23, 0, 0, 0, 0, ZoneOffset.UTC )

        assertEquals(expectedStartOfDay, dateTime.startOfDay())
    }

    @Test
    fun `endOfDay returns end of day for given ZonedDateTime`() {
        val dateTime = OffsetDateTime.of(2024, 11, 23, 2, 30, 0, 0, ZoneOffset.UTC )
        val expectedEndOfDay = OffsetDateTime.of(2024, 11, 23, 23, 59, 59, 999_999_999, ZoneOffset.UTC )

        assertEquals(expectedEndOfDay, dateTime.endOfDay())
    }
}