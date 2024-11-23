package com.ahold.ctp.assignment.service


import com.ahold.ctp.assignment.dto.BulkUpdateDeliveryRequest
import com.ahold.ctp.assignment.dto.UpdateDeliveryRequest
import com.ahold.ctp.assignment.model.Delivery
import com.ahold.ctp.assignment.model.DeliveryStatus
import com.ahold.ctp.assignment.repository.DeliveryRepository
import com.ahold.ctp.assignment.util.defaultZone
import com.ahold.ctp.assignment.util.endOfDay
import com.ahold.ctp.assignment.util.logger
import com.ahold.ctp.assignment.util.startOfDay
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import java.util.*

@Service
class DeliveryService(
    private val repository: DeliveryRepository
) {
    private val log = logger(javaClass)

    fun createDelivery(delivery: Delivery): Delivery {
        log.debug("creating a delivery for $delivery")
        return repository.save(delivery)
    }

    @Transactional
    fun updateDelivery(id: UUID, request: UpdateDeliveryRequest): Delivery {
        log.debug("Update an existing delivery with Id $id")

        val delivery = repository.findById(id)
            .orElseThrow {
                log.debug("Delivery not found with id: $id")
                DeliveryNotFoundException("Delivery not found with id: $id")
            }

        compareUpdateAndExistingDelivery(
            updateStatus = DeliveryStatus.valueOf(requireNotNull(request.status)), // handled illegalArgException
            updateFinishedAt = request.finishedAt,
            existingDelivery = delivery
        )

        delivery.apply {
            status = DeliveryStatus.valueOf(request.status!!)
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
                    log.debug("Delivery not found with id: $request.id")
                    DeliveryNotFoundException("Delivery not found with id: $request.id")
                }

            compareUpdateAndExistingDelivery(
                updateStatus = DeliveryStatus.valueOf(requireNotNull(request.status)), // handled illegalArgException
                updateFinishedAt = request.finishedAt,
                existingDelivery = delivery
            )

            delivery.apply {
                status = DeliveryStatus.valueOf(request.status!!)
                finishedAt = request.finishedAt
            }

            updatedDeliveries.add(repository.save(delivery))
        }

        return updatedDeliveries
    }

    fun getBusinessSummary(): Pair<Int, Int> {
        val yesterday = ZonedDateTime.now(defaultZone()).minusDays(1)

        log.debug("Retrieving business summary for $yesterday")

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

    private fun compareUpdateAndExistingDelivery(
        updateStatus: DeliveryStatus,
        updateFinishedAt: ZonedDateTime?,
        existingDelivery: Delivery
    ) {
        if (existingDelivery.status == DeliveryStatus.DELIVERED) {
            throw IllegalArgumentException("delivery with id ${existingDelivery.id} is already DELIVERED, can't be updated")
        }

        if (updateStatus == DeliveryStatus.DELIVERED && updateFinishedAt!! < existingDelivery.startedAt
        ) {
            throw IllegalArgumentException("DELIVERED update must have a larger finishedAt than startedAt of the existing IN_PROGRESS delivery")
        }

    }
}

class DeliveryNotFoundException(message: String) : RuntimeException(message)
