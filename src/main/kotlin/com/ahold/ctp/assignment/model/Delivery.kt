package com.ahold.ctp.assignment.model

import com.ahold.ctp.assignment.dto.CreateDeliveryRequest
import jakarta.persistence.*
import java.time.ZonedDateTime
import java.util.*

@Entity
@Table(name = "deliveries")
data class Delivery(
    @Id
    val id: UUID,
    val vehicleId: String,
    val startedAt: ZonedDateTime,
    var finishedAt: ZonedDateTime?,
    @Enumerated(EnumType.STRING)
    var status: DeliveryStatus
) {
    // can also use lombok @NoArgsConstructor but this is more explicit imo
    constructor() : this(UUID.randomUUID(), "default-vid", ZonedDateTime.now(), null, DeliveryStatus.IN_PROGRESS)

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
