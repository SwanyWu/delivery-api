package com.ahold.ctp.assignment.dto

import com.ahold.ctp.assignment.model.DeliveryStatus
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.util.*

data class CreateDeliveryRequest(
    val vehicleId: String,
    val startedAt: ZonedDateTime,
    val status: DeliveryStatus
)
data class UpdateDeliveryRequest(
    val finishedAt: ZonedDateTime?,
    val status: DeliveryStatus
)

data class BulkUpdateDeliveryRequest(
    val id: UUID,
    val finishedAt: ZonedDateTime?,
    val status: DeliveryStatus
)


