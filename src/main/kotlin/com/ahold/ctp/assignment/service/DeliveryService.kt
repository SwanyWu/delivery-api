package com.ahold.ctp.assignment.service


import com.ahold.ctp.assignment.repository.DeliveryRepository
import com.ahold.ctp.assignment.dto.BulkUpdateDeliveryRequest
import com.ahold.ctp.assignment.dto.UpdateDeliveryRequest
import com.ahold.ctp.assignment.model.Delivery
import com.ahold.ctp.assignment.model.DeliveryStatus
import com.ahold.ctp.assignment.util.defaultZone
import com.ahold.ctp.assignment.util.endOfDay
import com.ahold.ctp.assignment.util.logger
import com.ahold.ctp.assignment.util.startOfDay
import jakarta.persistence.EntityNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.ResponseStatus
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import java.util.*

@Service
class DeliveryService(
    private val repository: DeliveryRepository
) {
   private val log = logger(javaClass)

    fun createDelivery(delivery: Delivery): Delivery {
        log.debug("create a new delivery for $delivery")
        if (delivery.status != DeliveryStatus.IN_PROGRESS && delivery.status != DeliveryStatus.DELIVERED) {
            throw IllegalArgumentException("Invalid status")
        }
        return repository.save(delivery)
    }

    fun updateDelivery(id: UUID, request: UpdateDeliveryRequest): Delivery {
        log.debug("Update an existing delivery with Id $id")

        val delivery = repository.findById(id)
            .orElseThrow {
                log.warn("Delivery not found with id: $id")
                DeliveryNotFoundException("Delivery not found with id: $id")// 404
            }

        if ((request.status == DeliveryStatus.DELIVERED).xor(request.finishedAt != null)) {
            log.warn("Invalid request: status is {} and finishedAt is {}", request.status, request.finishedAt)
            throw InvalidArgumentException("FinishedAt must be provided if and only if when status is DELIVERED.")
        } // 400 bad request

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
                .orElseThrow {
                    log.warn("Delivery not found with id: $request.id")
                    DeliveryNotFoundException("Delivery not found with id: $request.id")// 404
                }

            if ((request.status == DeliveryStatus.DELIVERED).xor(request.finishedAt != null)) {
                log.warn("Invalid request: status is {} and finishedAt is {}", request.status, request.finishedAt)
                throw InvalidArgumentException("FinishedAt must be provided if and only if when status is DELIVERED.")
            }

            delivery.apply {
                status = request.status
                finishedAt = request.finishedAt
            }

            updatedDeliveries.add(repository.save(delivery))
        }

        return updatedDeliveries
    }

    fun getBusinessSummary(): Pair<Int, Int> {
        val yesterday = LocalDate.now(defaultZone()).minusDays(1)

        // use debug and run in debug mode, to avoid log swamp
        log.debug("get business summary for $yesterday ")

        val deliveries = repository.findAllByStartedAtBetween(yesterday.startOfDay(), yesterday.endOfDay())

        val averageMinutes = if (deliveries.size > 1) {
            val intervals = deliveries.sortedBy { it.startedAt }
                .zipWithNext()
                .map { ChronoUnit.MINUTES.between(it.first.startedAt, it.second.startedAt) }
            intervals.average()
        } else {
            0.0
        }

        return Pair(deliveries.size, averageMinutes.toInt())
    }
}

@ResponseStatus(HttpStatus.BAD_REQUEST) // Maps to 400 Bad Request
class InvalidArgumentException(message: String) : RuntimeException(message)
@ResponseStatus(HttpStatus.NOT_FOUND) // This will map to a 404
class DeliveryNotFoundException(message: String) : RuntimeException(message)
