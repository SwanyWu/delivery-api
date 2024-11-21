package com.ahold.ctp.delivery.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
data class Delivery(
    @Id
    @GeneratedValue
    val id: UUID = UUID.randomUUID(),
    val vehicleId: String,
    val startedAt: LocalDateTime,
    var finishedAt: LocalDateTime? = null,
    @Enumerated(EnumType.STRING)
    var status: DeliveryStatus
)


enum class DeliveryStatus {
    IN_PROGRESS, DELIVERED
}
