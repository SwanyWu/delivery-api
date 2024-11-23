package com.ahold.ctp.assignment.dto

import com.ahold.ctp.assignment.model.Delivery
import com.ahold.ctp.assignment.model.DeliveryStatus
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime
import java.util.*

class DeliveryResponseTest {
    @Test
    fun `from should correctly map Delivery to DeliveryResponse`() {
        // given: a Delivery
        val id = UUID.randomUUID()
        val vehicleId = "vid-123"
        val startedAt = ZonedDateTime.parse("2024-11-22T10:15:30+01:00[Europe/Amsterdam]")
        val finishedAt = ZonedDateTime.parse("2024-11-22T12:45:30+01:00[Europe/Amsterdam]")
        val status = DeliveryStatus.IN_PROGRESS

        val delivery = Delivery(
            id = id,
            vehicleId = vehicleId,
            startedAt = startedAt,
            finishedAt = finishedAt,
            status = status
        )

        // when: convert it to DeliveryResponse
        val response = DeliveryResponse.from(delivery)

        // then: it's converted correctly
        assertEquals(id, response.id)
        assertEquals(vehicleId, response.vehicleId)
        assertEquals(startedAt, response.startedAt)
        assertEquals(finishedAt, response.finishedAt)
        assertEquals(status, response.status)
    }
}