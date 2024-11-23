package com.ahold.ctp.assignment.controller

import com.ahold.ctp.assignment.controller.exception.ControllerException
import com.ahold.ctp.assignment.dto.*
import com.ahold.ctp.assignment.model.Delivery
import com.ahold.ctp.assignment.service.DeliveryService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*
import java.util.*


@RestController
@RequestMapping("/deliveries")
class DeliveryController(private val service: DeliveryService) {

    @Operation(summary = "Create a delivery")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Successfully created a new delivery",
            content = [Content(schema = Schema(implementation = DeliveryResponse::class))]),
        ApiResponse(responseCode = "400", description = "Invalid request",
            content = [Content(schema = Schema(implementation = ControllerException::class))]),
        ApiResponse(responseCode = "500", description = "Internal server error",
            content = [Content(schema = Schema(implementation = ControllerException::class))])
    ])
    @PostMapping
    fun createDelivery(@Valid @RequestBody createDeliveryRequest: CreateDeliveryRequest): DeliveryResponse {
        val delivery = service.createDelivery(Delivery.of(createDeliveryRequest))
        return DeliveryResponse.from(delivery)
    }

    @Operation(summary = "Update an existing delivery")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Successfully updated an existing delivery",
            content = [Content(schema = Schema(implementation = DeliveryResponse::class))]),
        ApiResponse(responseCode = "400", description = "Invalid request",
            content = [Content(schema = Schema(implementation = ControllerException::class))]),
        ApiResponse(responseCode = "404", description = "Delivery not found",
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
        ApiResponse(responseCode = "200", description = "Successfully updated a list of existing deliveries",
            content = [Content(schema = Schema(implementation = BulkUpdateResponse::class))]),
        ApiResponse(responseCode = "400", description = "Invalid request",
            content = [Content(schema = Schema(implementation = ControllerException::class))]),
        ApiResponse(responseCode = "404", description = "Delivery not found",
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

    @Operation(summary = "Get business summary for yesterday")
    @ApiResponses(value = [
        ApiResponse(responseCode = "200", description = "Successfully retrieved business summary for yesterday",
            content = [Content(schema = Schema(implementation = BusinessSummaryResponse::class))]),
        ApiResponse(responseCode = "500", description = "Internal server error",
            content = [Content(schema = Schema(implementation = ControllerException::class))])
    ])
    @GetMapping("/business-summary")
    fun getBusinessSummary(): BusinessSummaryResponse {
        val summary = service.getBusinessSummary()
        return BusinessSummaryResponse(
            deliveries = summary.first,
            averageMinutesBetweenDeliveryStart = summary.second
        )
    }
}
