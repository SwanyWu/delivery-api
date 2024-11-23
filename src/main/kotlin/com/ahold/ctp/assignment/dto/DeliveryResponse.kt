package com.ahold.ctp.assignment.dto

import com.ahold.ctp.assignment.model.Delivery
import com.ahold.ctp.assignment.model.DeliveryStatus
import com.fasterxml.jackson.annotation.JsonFormat
import java.time.ZonedDateTime
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
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "Europe/Amsterdam")
    val startedAt: ZonedDateTime,
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "Europe/Amsterdam")
    val finishedAt: ZonedDateTime?,
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

data class BusinessSummaryResponse(
    val deliveries: Int,
    val averageMinutesBetweenDeliveryStart: Int
)