package com.ahold.ctp.assignment.dto

import com.ahold.ctp.assignment.model.Delivery
import com.ahold.ctp.assignment.model.DeliveryStatus
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime
import java.util.*

class DeliveryResponseTest {
    @Test
    fun `from should correctly map Delivery to DeliveryResponse`() {
        // given: a Delivery
        val id = UUID.randomUUID()
        val vehicleId = "vid-123"
        val startedAt = OffsetDateTime.parse("2024-11-22T07:14:11.990Z")
        val finishedAt = OffsetDateTime.parse("2024-11-24T07:14:11.990Z")
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