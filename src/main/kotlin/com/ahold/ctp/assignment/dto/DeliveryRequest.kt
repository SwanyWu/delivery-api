package com.ahold.ctp.assignment.dto


import com.ahold.ctp.assignment.model.DeliveryStatus
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.OffsetDateTime
import java.util.*


data class CreateDeliveryRequest(
    @field:NotBlank(message = "vehicleId cannot be null, empty, or blank")
    val vehicleId: String?,
    val startedAt: OffsetDateTime?, // HttpMessageNotReadableException
    val status: String? // nullable for customised error message
) {
    // HttpMessageNotReadableException
    init {
        validateRequest(status, startedAt)
    }
}

data class UpdateDeliveryRequest(
    val finishedAt: OffsetDateTime?,
    val status: String?
) {
    init {
        validateRequest(status, finishedAt)
    }
}

data class BulkUpdateDeliveryRequest(
    @field:NotNull(message = "please provide a valid UUID for bulk update")
    val id: UUID,
    val finishedAt: OffsetDateTime?,
    val status: String?
) {
    init {
        validateRequest(status, finishedAt)
    }
}

private fun validateRequest(status: String?, dateTime: OffsetDateTime?) {
    require(status != null && status in DeliveryStatus.asList()) {
        "status must be either IN_PROGRESS or DELIVERED"
    }
    require(dateTime != null && !dateTime.isAfter(OffsetDateTime.now())) {
        "please provide a valid dateTime in UTC up until now"
    }
}



