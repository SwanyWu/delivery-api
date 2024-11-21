package com.ahold.ctp.delivery.dto

import com.ahold.ctp.delivery.model.DeliveryStatus
import java.time.LocalDateTime
import java.util.*

data class CreateDeliveryRequest(
    val vehicleId: String,
    val startedAt: LocalDateTime,
    val status: DeliveryStatus
)
data class UpdateDeliveryRequest(
    val finishedAt: LocalDateTime?,
    val status: DeliveryStatus
)

data class BulkUpdateDeliveryRequest(
    val id: UUID,
    val finishedAt: LocalDateTime?,
    val status: DeliveryStatus
)


