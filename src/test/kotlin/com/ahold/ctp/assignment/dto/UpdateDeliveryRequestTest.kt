package com.ahold.ctp.assignment.dto

import com.ahold.ctp.assignment.model.DeliveryStatus
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime
import java.util.*

class UpdateDeliveryRequestTest{
    @Test
    fun `should throw IllegalArgumentException when status is DELIVERED and finishedAt is null`() {
        assertThrows<IllegalArgumentException> {
            UpdateDeliveryRequest(
                id = UUID.randomUUID(),
                finishedAt = null,
                status = DeliveryStatus.DELIVERED
            )
        }
    }

    @Test
    fun `should throw IllegalArgumentException when status is IN_PROGRESS and finishedAt is not null`() {
        assertThrows<IllegalArgumentException> {
            UpdateDeliveryRequest(
                id = UUID.randomUUID(),
                finishedAt = ZonedDateTime.now(),
                status = DeliveryStatus.IN_PROGRESS
            )
        }
    }
}