package com.ahold.ctp.delivery.service


import com.ahold.ctp.delivery.dto.BulkUpdateDeliveryRequest
import com.ahold.ctp.delivery.dto.UpdateDeliveryRequest
import com.ahold.ctp.delivery.model.Delivery
import com.ahold.ctp.delivery.model.DeliveryStatus
import com.ahold.ctp.delivery.repository.DeliveryRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*

@Service
class DeliveryService(
    @Autowired
    private val repository: DeliveryRepository
) {

    fun createDelivery(delivery: Delivery): Delivery {
        if (delivery.status != DeliveryStatus.IN_PROGRESS && delivery.status != DeliveryStatus.DELIVERED) {
            throw IllegalArgumentException("Invalid status")
        }
        return repository.save(delivery)
    }

    @Transactional
    fun updateDelivery(id: UUID, request: UpdateDeliveryRequest): Delivery {
        val delivery = repository.findById(id)
            .orElseThrow { EntityNotFoundException("Delivery not found with id: $id") }

        if (request.status == DeliveryStatus.DELIVERED && request.finishedAt == null) {
            throw IllegalArgumentException("FinishedAt must be provided when status is DELIVERED.")
        }
        if (request.status != DeliveryStatus.DELIVERED && request.finishedAt != null) {
            throw IllegalArgumentException("FinishedAt should not be provided for statuses other than DELIVERED.")
        }

        delivery.apply {
            status = request.status
            finishedAt = request.finishedAt
        }

        return repository.save(delivery)
    }

    @Transactional
    fun bulkUpdateDeliveries(requests: List<BulkUpdateDeliveryRequest>): List<Delivery> {
        val updatedDeliveries = mutableListOf<Delivery>()

        for (request in requests) {
            val delivery = repository.findById(request.id)
                .orElseThrow { EntityNotFoundException("Delivery not found with id: ${request.id}") }

            if (request.status == DeliveryStatus.DELIVERED && request.finishedAt == null) {
                throw IllegalArgumentException("FinishedAt must be provided when status is DELIVERED.")
            }
            if (request.status != DeliveryStatus.DELIVERED && request.finishedAt != null) {
                throw IllegalArgumentException("FinishedAt should not be provided for statuses other than DELIVERED.")
            }

            delivery.apply {
                status = request.status
                finishedAt = request.finishedAt
            }

            updatedDeliveries.add(repository.save(delivery))
        }

        return updatedDeliveries
    }

    fun getBusinessSummary(): Map<String, Any> {
        val zoneId = ZoneId.of("Europe/Amsterdam")
        val yesterday = LocalDate.now(zoneId).minusDays(1)
        val startOfDay = yesterday.atStartOfDay(zoneId).toLocalDateTime()
        val endOfDay = startOfDay.plusDays(1)

        val deliveries = repository.findAllByStartedAtBetween(startOfDay, endOfDay)
        val count = deliveries.size

        val averageMinutes = if (deliveries.size > 1) {
            val intervals = deliveries.sortedBy { it.startedAt }
                .zipWithNext()
                .map { ChronoUnit.MINUTES.between(it.first.startedAt, it.second.startedAt) }
            intervals.average()
        } else {
            0.0
        }

        return mapOf(
            "deliveries" to count,
            "averageMinutesBetweenDeliveryStart" to averageMinutes.toInt()
        )
    }
}
