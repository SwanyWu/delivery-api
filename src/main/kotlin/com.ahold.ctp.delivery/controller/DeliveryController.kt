package com.ahold.ctp.delivery.controller

import com.ahold.ctp.delivery.model.Delivery
import com.ahold.ctp.delivery.dto.*
import com.ahold.ctp.delivery.service.DeliveryService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.util.*

@RestController
@RequestMapping("/deliveries")
class DeliveryController(private val service: DeliveryService) {

    @PostMapping
    fun createDelivery(@RequestBody delivery: Delivery): ResponseEntity<Delivery> {
        val createdDelivery = service.createDelivery(delivery)
        return ResponseEntity.created(URI("/deliveries/${createdDelivery.id}")).body(createdDelivery)
    }

    @PatchMapping("/{id}")
    fun updateDelivery(
        @PathVariable id: UUID,
        @RequestBody request: UpdateDeliveryRequest
    ): ResponseEntity<DeliveryResponse> {
        val updatedDelivery = service.updateDelivery(id, request)
        return ResponseEntity.ok(DeliveryResponse.from(updatedDelivery))
    }

    @PatchMapping("/bulk-update")
    fun bulkUpdateDeliveries(
        @RequestBody requests: List<BulkUpdateDeliveryRequest>
    ): ResponseEntity<BulkUpdateResponse> {
        val updatedDeliveries = service.bulkUpdateDeliveries(requests)
        return ResponseEntity.ok(BulkUpdateResponse.from(updatedDeliveries))
    }

    @GetMapping("/business-summary")
    fun getBusinessSummary(): ResponseEntity<Map<String, Any>> {
        val summary = service.getBusinessSummary()
        return ResponseEntity.ok(summary)
    }
}
