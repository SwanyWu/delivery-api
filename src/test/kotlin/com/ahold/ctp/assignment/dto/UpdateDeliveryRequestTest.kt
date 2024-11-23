package com.ahold.ctp.assignment.dto

import com.ahold.ctp.assignment.model.DeliveryStatus
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.Test
import java.time.ZonedDateTime
import java.util.*

class UpdateDeliveryRequestTest{
    @Test
    fun `should throw IllegalArgumentException for invalid status`() {
        assertThrows<IllegalArgumentException> {
            UpdateDeliveryRequest(
                finishedAt = ZonedDateTime.now(),
                status = "INVALID_STATUS"
            )
        }

        assertThrows<IllegalArgumentException> {
            UpdateDeliveryRequest(
                finishedAt = ZonedDateTime.now(),
                status = ""
            )
        }

        assertThrows<IllegalArgumentException> {
            UpdateDeliveryRequest(
                finishedAt = null,
                status = "DELIVERED"
            )
        }

        assertThrows<IllegalArgumentException> {
            UpdateDeliveryRequest(
                finishedAt = ZonedDateTime.now(),
                status = "IN_PROGRESS"
            )
        }
    }
}