package com.ahold.ctp.assignment.service


import com.ahold.ctp.assignment.dto.UpdateDeliveryRequest
import com.ahold.ctp.assignment.model.Delivery
import com.ahold.ctp.assignment.repository.DeliveryRepository
import com.ahold.ctp.assignment.util.defaultZone
import com.ahold.ctp.assignment.util.endOfDay
import com.ahold.ctp.assignment.util.logger
import com.ahold.ctp.assignment.util.startOfDay
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
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
        return repository.save(delivery)
    }

    fun updateDelivery(id: UUID, request: UpdateDeliveryRequest): Delivery {
        log.debug("Update an existing delivery with Id $id")

        val delivery = repository.findById(id)
            .orElseThrow {
                log.warn("Delivery not found with id: $id")
                DeliveryNotFoundException("Delivery not found with id: $id")// 404
            }

        delivery.apply {
            status = request.status
            finishedAt = request.finishedAt
        }

        return repository.save(delivery)
    }

    @Transactional
    fun bulkUpdateDeliveries(requests: List<UpdateDeliveryRequest>): List<Delivery> {
        val updatedDeliveries = mutableListOf<Delivery>()

        for (request in requests) {

            require (request.id != null) { "Invalid request: id ust be provided for bulk update." }

            val delivery = repository.findById(request.id)
                .orElseThrow {
                    log.warn("Delivery not found with id: $request.id")
                    DeliveryNotFoundException("Delivery not found with id: $request.id")
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
        log.debug("Get business summary for $yesterday")

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

class DeliveryNotFoundException(message: String) : RuntimeException(message)
