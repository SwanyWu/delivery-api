package com.ahold.ctp.delivery.controller

import com.ahold.ctp.delivery.dto.*
import com.ahold.ctp.delivery.model.Delivery
import com.ahold.ctp.delivery.service.DeliveryService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/deliveries")
class DeliveryController(private val service: DeliveryService) {

    @Operation(summary = "Create a new delivery")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Successfully created delivery",
            content = [Content(schema = Schema(implementation = DeliveryResponse::class))]),
        ApiResponse(responseCode = "400", description = "Invalid request",
            content = [Content(schema = Schema(implementation = ControllerException::class))]),
        ApiResponse(responseCode = "500", description = "Internal server error",
            content = [Content(schema = Schema(implementation = ControllerException::class))])
    ])
    @PostMapping
    fun createDelivery(@RequestBody createDeliveryRequest: CreateDeliveryRequest): DeliveryResponse {
        val delivery = service.createDelivery(Delivery.of(createDeliveryRequest))
        return DeliveryResponse(
            id = delivery.id,
            vehicleId = delivery.vehicleId,
            startedAt = delivery.startedAt,
            finishedAt = delivery.finishedAt,
            status = delivery.status
        )
    }

    @Operation(summary = "Update an existing delivery")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Successfully created delivery",
            content = [Content(schema = Schema(implementation = DeliveryResponse::class))]),
        ApiResponse(responseCode = "400", description = "Invalid request",
            content = [Content(schema = Schema(implementation = ControllerException::class))]),
        ApiResponse(responseCode = "500", description = "Internal server error",
            content = [Content(schema = Schema(implementation = ControllerException::class))])
    ])
    @PatchMapping("/{id}")
    fun updateDelivery(
        @PathVariable id: UUID,
        @RequestBody request: UpdateDeliveryRequest
    ): DeliveryResponse {
        val updatedDelivery = service.updateDelivery(id, request)
        return DeliveryResponse.from(updatedDelivery)
    }

    @Operation(summary = "Update multiple deliveries")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Successfully created delivery",
            content = [Content(schema = Schema(implementation = BulkUpdateResponse::class))]),
        ApiResponse(responseCode = "400", description = "Invalid request",
            content = [Content(schema = Schema(implementation = ControllerException::class))]),
        ApiResponse(responseCode = "500", description = "Internal server error",
            content = [Content(schema = Schema(implementation = ControllerException::class))])
    ])
    @PatchMapping("/bulk-update")
    fun bulkUpdateDeliveries(
        @RequestBody requests: List<BulkUpdateDeliveryRequest>
    ): BulkUpdateResponse {
        val updatedDeliveries = service.bulkUpdateDeliveries(requests)
        return BulkUpdateResponse.from(updatedDeliveries)
    }

    @Operation(summary = "Get business summary")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Successfully retrieved business summary",
            content = [Content(schema = Schema(implementation = BusinessSummaryResponse::class))]),
        ApiResponse(responseCode = "500", description = "Internal server error",
            content = [Content(schema = Schema(implementation = ControllerException::class))])
    ])
    @GetMapping("/business-summary")
    fun getBusinessSummary(): BusinessSummaryResponse {
        val summary = service.getBusinessSummary()
        return BusinessSummaryResponse(
            deliveries = 42,
            averageMinutesBetweenDeliveryStart = 24
        )
    }
}
