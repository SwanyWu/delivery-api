package com.ahold.ctp.assignment.repository

import com.ahold.ctp.assignment.model.Delivery
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.ZonedDateTime
import java.util.*

@Repository
interface DeliveryRepository : JpaRepository<Delivery, UUID> {
    fun findAllByStartedAtBetween(start: ZonedDateTime, end: ZonedDateTime): List<Delivery>
}

