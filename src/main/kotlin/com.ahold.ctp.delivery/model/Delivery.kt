package com.ahold.ctp.delivery.model

import com.ahold.ctp.delivery.dto.CreateDeliveryRequest
import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.*

@Entity
data class Delivery(
    @Id
    @GeneratedValue
    val id: UUID,
    val vehicleId: String,
    val startedAt: LocalDateTime,
    var finishedAt: LocalDateTime?,
    @Enumerated(EnumType.STRING)
    var status: DeliveryStatus
) {
    companion object {
        fun of(createDeliveryRequest: CreateDeliveryRequest) = Delivery(
            id = UUID.randomUUID(),
            vehicleId = createDeliveryRequest.vehicleId,
            startedAt = createDeliveryRequest.startedAt,
            finishedAt = null,
            status = createDeliveryRequest.status
        )
    }
}



enum class DeliveryStatus {
    IN_PROGRESS, DELIVERED
}
