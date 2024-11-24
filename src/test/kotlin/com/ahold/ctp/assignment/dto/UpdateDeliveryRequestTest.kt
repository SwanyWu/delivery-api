package com.ahold.ctp.assignment.dto

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.OffsetDateTime

class UpdateDeliveryRequestTest{
    @Test
    fun `should throw IllegalArgumentException for invalid status`() {
        assertThrows<IllegalArgumentException> {
            UpdateDeliveryRequest(
                finishedAt = OffsetDateTime.now(),
                status = "INVALID_STATUS"
            )
        }

        assertThrows<IllegalArgumentException> {
            UpdateDeliveryRequest(
                finishedAt = OffsetDateTime.now(),
                status = ""
            )
        }

        assertThrows<IllegalArgumentException> {
            UpdateDeliveryRequest(
                finishedAt = null,
                status = "DELIVERED"
            )
        }
    }
}