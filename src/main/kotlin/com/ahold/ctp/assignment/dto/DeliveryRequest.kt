package com.ahold.ctp.assignment.dto

import com.ahold.ctp.assignment.model.DeliveryStatus
import java.time.ZonedDateTime
import java.util.*

data class CreateDeliveryRequest(
    val vehicleId: String,
    val startedAt: ZonedDateTime, // no validation, suppose it can be in the future/past
    val status: DeliveryStatus
)

data class UpdateDeliveryRequest(
    val id: UUID? = null,
    val finishedAt: ZonedDateTime?,
    val status: DeliveryStatus
) {
    init {
        require((status == DeliveryStatus.DELIVERED).xor(finishedAt == null)) {
            "FinishedAt must be provided if and only if when status is DELIVERED"
        }
    }
}


