package com.ahold.ctp.assignment.dto

import com.fasterxml.jackson.annotation.JsonFormat
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.ZonedDateTime
import java.util.*


data class CreateDeliveryRequest(
    @field:NotBlank(message = "vehicleId cannot be null, empty, or blank") // simple validation via annotation
    val vehicleId: String?,
    val startedAt: ZonedDateTime?,
    val status: String?
) {
    init {
        require(startedAt != null) {
            "startedAt cannot be null and must be of format YYYY-MM-DD'T'HH:mm:ss.SSSZ, e.g. 2024-01-01T01:01:00.421Z"
        }
        require(!startedAt.isAfter(ZonedDateTime.now())) {
            "startedAt cannot be in the future"
        }
        require(status != null && status in listOf("DELIVERED", "IN_PROGRESS")) {
            "status must be either IN_PROGRESS or DELIVERED"
        }
    }
}

data class UpdateDeliveryRequest(
    val finishedAt: ZonedDateTime?,
    val status: String? // make it nullable for customised error message
) {
    // handled in ControllerExceptionHandler
    init {
        require(status != null && status in listOf("DELIVERED", "IN_PROGRESS")) {
            "status must be either IN_PROGRESS or DELIVERED"
        }
        require((status == "DELIVERED").xor(finishedAt == null)) {
            "finishedAt must be provided if and only if when status is DELIVERED"
        }
    }
}

data class BulkUpdateDeliveryRequest(
    @field:NotNull(message = "please provide a valid UUID for bulk update")
    val id: UUID,
    val finishedAt: ZonedDateTime?,
    val status: String? // make it nullable for customised error message
) {
    // handled in ControllerExceptionHandler
    init {
        require(status != null && status in listOf("DELIVERED", "IN_PROGRESS")) {
            "status must be either IN_PROGRESS or DELIVERED"
        }
        require((status == "DELIVERED").xor(finishedAt == null)) {
            "finishedAt must be provided if and only if when status is DELIVERED"
        }
    }
}


