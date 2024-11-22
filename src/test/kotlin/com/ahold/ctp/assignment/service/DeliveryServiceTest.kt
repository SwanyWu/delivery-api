package com.ahold.ctp.assignment.service

import com.ahold.ctp.assignment.dto.UpdateDeliveryRequest
import com.ahold.ctp.assignment.model.Delivery
import com.ahold.ctp.assignment.model.DeliveryStatus
import com.ahold.ctp.assignment.repository.DeliveryRepository
import com.ahold.ctp.assignment.util.defaultZone
import com.ahold.ctp.assignment.util.endOfDay
import com.ahold.ctp.assignment.util.startOfDay
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.verify
import java.time.ZonedDateTime
import java.util.*
import org.mockito.Mockito.*
import java.time.LocalDate

class DeliveryServiceTest {
    private lateinit var repository: DeliveryRepository
    private lateinit var service: DeliveryService

    private val delivery = Delivery(
        id = UUID.randomUUID(),
        vehicleId = "vid-xyz",
        startedAt = ZonedDateTime.now(),
        finishedAt = null,
        status = DeliveryStatus.IN_PROGRESS
    )

    @BeforeEach
    fun setup() {
        repository = mock(DeliveryRepository::class.java)
        service = DeliveryService(repository)
    }

    @Test
    fun `createDelivery should save a new delivery`() {
        // given: a new delivery stored in repo
        `when`(repository.save(delivery)).thenReturn(delivery)

        // when: ask service to save this delivery
        val result = service.createDelivery(delivery)

        // then: it's saved
        assertEquals(delivery, result)
        verify(repository).save(delivery)
    }

    @Test
    fun `updateDelivery should update an existing delivery`() {
        // given: a delivery store in the repo and an update request
        val updateRequest = UpdateDeliveryRequest(
            id = delivery.id,
            status = DeliveryStatus.DELIVERED,
            finishedAt = ZonedDateTime.now()
        )

        `when`(repository.findById(delivery.id)).thenReturn(Optional.of(delivery))
        `when`(repository.save(delivery)).thenReturn(delivery)

        // when: the service is asked to update existing delivery
        val result = service.updateDelivery(delivery.id, updateRequest)

        // then: it's updated
        assertEquals(updateRequest.status, result.status)
        assertEquals(updateRequest.finishedAt, result.finishedAt)
        verify(repository).findById(delivery.id)
        verify(repository).save(delivery)
    }

    @Test
    fun `updateDelivery should throw exception when delivery is not found`() {
        // given: an update request for a non-existent delivery
        val deliveryId = UUID.randomUUID()
        val updateRequest = UpdateDeliveryRequest(
            id = deliveryId,
            status = DeliveryStatus.DELIVERED,
            finishedAt = ZonedDateTime.now()
        )

        `when`(repository.findById(deliveryId)).thenReturn(Optional.empty())

        // when: the service is asked to update a non-existent delivery
        // then: throw DeliveryNotFoundException
        val exception = assertThrows<DeliveryNotFoundException> {
            service.updateDelivery(deliveryId, updateRequest)
        }
        verify(repository).findById(deliveryId)
    }

    @Test
    fun `bulkUpdateDeliveries should update all deliveries`() {
        // given: a bunch of deliveries stored in repo and corresponding update requests
        val delivery2 = delivery.copy(id = UUID.randomUUID())

        val requests = listOf(
            UpdateDeliveryRequest(
                id = delivery.id,
                status = DeliveryStatus.DELIVERED,
                finishedAt = ZonedDateTime.now()
            ),
            UpdateDeliveryRequest(id = delivery2.id, status = DeliveryStatus.IN_PROGRESS, finishedAt = null)
        )

        `when`(repository.findById(delivery.id)).thenReturn(Optional.of(delivery))
        `when`(repository.findById(delivery2.id)).thenReturn(Optional.of(delivery2))
        `when`(repository.save(any(Delivery::class.java))).thenAnswer { it.arguments[0] }

        // when: ask the service to update
        val result = service.bulkUpdateDeliveries(requests)

        // then: they're updated
        assertEquals(2, result.size)
        verify(repository, times(1)).findById(delivery.id)
        verify(repository, times(1)).findById(delivery2.id)
        verify(repository, times(2)).save(any(Delivery::class.java))
    }

    @Test
    fun `getBusinessSummary should return total deliveries and average minutes`() {
        // given: two deliveries from yesterday with 120 min interval
        val yesterday = LocalDate.now(defaultZone()).minusDays(1)
        val delivery1 = Delivery(
            id = UUID.randomUUID(),
            vehicleId = "vid-1",
            startedAt = yesterday.startOfDay().plusHours(1),
            finishedAt = null,
            status = DeliveryStatus.IN_PROGRESS
        )
        val delivery2 = Delivery(
            id = UUID.randomUUID(),
            vehicleId = "vid-2",
            startedAt = yesterday.startOfDay().plusHours(3),
            finishedAt = null,
            status = DeliveryStatus.IN_PROGRESS
        )

        `when`(
            repository.findAllByStartedAtBetween(
                yesterday.startOfDay(),
                yesterday.endOfDay()
            )
        ).thenReturn(listOf(delivery1, delivery2))

        // when: call getBusinessSummary
        val result = service.getBusinessSummary()

        // then: 2 deliveries are found
        assertEquals(2, result.first)
        assertEquals(120, result.second)
    }

    @Test
    fun `getBusinessSummary should return zero average for single delivery`() {
        // given: create a single delivery with a started time
        val yesterday = LocalDate.now(defaultZone()).minusDays(1)
        `when`(
            repository.findAllByStartedAtBetween(
                yesterday.startOfDay(),
                yesterday.endOfDay()
            )
        ).thenReturn(listOf(delivery))

        // when: call getBusinessSummary
        val result = service.getBusinessSummary()

        // then: the result contains 1 delivery and average minutes is 0
        assertEquals(1, result.first)
        assertEquals(0, result.second)
    }
}