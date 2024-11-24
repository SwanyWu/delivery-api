package com.ahold.ctp.assignment.dto


import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.OffsetDateTime
import java.util.*


data class CreateDeliveryRequest(
    @field:NotBlank(message = "vehicleId cannot be null, empty, or blank") // simple validation via annotation
    val vehicleId: String?,
    val startedAt: OffsetDateTime?, // HttpMessageNotReadableException
    val status: String?
) {
    // HttpMessageNotReadableException
    init {
        require(startedAt != null) {
            "startedAt cannot be null and must be in UTC of format YYYY-MM-DD'T'HH:mm:ss.SSSZ, e.g. 2024-01-01T01:01:00.421Z"
        }
        // TODO: compare to defaulzone()
        require(!startedAt.isAfter(OffsetDateTime.now())) {
            "startedAt cannot be in the future"
        }
        require(status != null && status in listOf("DELIVERED", "IN_PROGRESS")) {
            "status must be either IN_PROGRESS or DELIVERED"
        }
    }
}

data class UpdateDeliveryRequest(
    val finishedAt: OffsetDateTime?,
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
    val finishedAt: OffsetDateTime?,
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


