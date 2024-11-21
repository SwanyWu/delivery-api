package com.ahold.ctp.delivery.repository

import com.ahold.ctp.delivery.model.Delivery
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.UUID

@Repository
interface DeliveryRepository : JpaRepository<Delivery, UUID> {
    fun findAllByStartedAtBetween(start: LocalDateTime, end: LocalDateTime): List<Delivery>
}

