package com.ahold.ctp.assignment.model

import com.ahold.ctp.assignment.dto.CreateDeliveryRequest
import jakarta.persistence.*
import java.time.OffsetDateTime
import java.util.*

@Entity
@Table(name = "deliveries")
data class Delivery(
    @Id
    val id: UUID,
    val vehicleId: String,
    @Column(name = "startedAt", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    val startedAt: OffsetDateTime,
    @Column(name = "finishedAt", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    var finishedAt: OffsetDateTime?,
    @Enumerated(EnumType.STRING)
    var status: DeliveryStatus
) {
    // can also use lombok @NoArgsConstructor but this is more explicit imo
    constructor() : this(UUID.randomUUID(), "default-vid", OffsetDateTime.now(), null, DeliveryStatus.IN_PROGRESS)

    companion object {
        fun of(request: CreateDeliveryRequest): Delivery {

            require(request.status != null && request.status in listOf("DELIVERED", "IN_PROGRESS")) {
                "invalid request: status must be either IN_PROGRESS or DELIVERED"
            }

            return Delivery(
                id = UUID.randomUUID(),
                vehicleId = request.vehicleId!!,
                startedAt = request.startedAt!!,
                // auto-complete finishedAt for a delivery with status DELIVERED
                finishedAt = if (request.status == "DELIVERED") request.startedAt else null,
                status = DeliveryStatus.valueOf(request.status)
            )
        }
    }
}


enum class DeliveryStatus {
    IN_PROGRESS, DELIVERED
}

