package com.ahold.ctp.delivery.dto

import com.ahold.ctp.delivery.model.Delivery
import com.ahold.ctp.delivery.model.DeliveryStatus
import java.time.LocalDateTime
import java.util.*

data class BulkUpdateResponse(
    val deliveries: List<DeliveryResponse>
) {
    companion object {
        fun from(deliveries: List<Delivery>): BulkUpdateResponse {
            return BulkUpdateResponse(deliveries.map { DeliveryResponse.from(it) })
        }
    }
}

data class DeliveryResponse(
    val id: UUID,
    val vehicleId: String,
    val startedAt: LocalDateTime,
    val finishedAt: LocalDateTime?,
    val status: DeliveryStatus
) {
    companion object {
        fun from(delivery: Delivery): DeliveryResponse {
            return DeliveryResponse(
                id = delivery.id,
                vehicleId = delivery.vehicleId,
                startedAt = delivery.startedAt,
                finishedAt = delivery.finishedAt,
                status = delivery.status
            )
        }
    }
}