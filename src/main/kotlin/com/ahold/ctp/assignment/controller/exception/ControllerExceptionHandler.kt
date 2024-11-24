package com.ahold.ctp.assignment.controller.exception

import com.ahold.ctp.assignment.service.DeliveryNotFoundException
import com.ahold.ctp.assignment.util.logger
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

@RestControllerAdvice
class ControllerExceptionHandler {
    private val log = logger(javaClass)

    //  service validation exception
    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    fun handleInvalidArgumentException(ex: IllegalArgumentException): ResponseEntity<ControllerException> {
        val error = ControllerException(
            message = ex.message,
            status = HttpStatus.BAD_REQUEST.value(),
            errors = mutableListOf("Invalid input."),
            timestamp = LocalDateTime.now()
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error)
    }

    //  spring boot validation exception
    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleMethodArgumentNotValidException(
        ex: MethodArgumentNotValidException
    ): ResponseEntity<ControllerException> {
        log.debug("Caught MethodArgumentNotValidException: $ex")
        val errors = ex.bindingResult.fieldErrors.joinToString(", ") { fieldError: FieldError ->
            "${fieldError.field}: ${fieldError.defaultMessage}"
        }

        val error = ControllerException(
            message = errors,
            status = HttpStatus.BAD_REQUEST.value(),
            errors = mutableListOf("Invalid input."),
            timestamp = LocalDateTime.now()
        )

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error)
    }

    // init block in (Update)DeliveryRequest
    @ExceptionHandler(HttpMessageNotReadableException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleJsonMappingException(ex: HttpMessageNotReadableException): ResponseEntity<ControllerException> {
        log.debug("Caught HttpMessageNotReadableException: $ex")
        val cause = ex.cause?.message ?: "Invalid request body"
        val rootCauseMessage = if (cause.contains("OffsetDateTime")) {
            "Invalid date format. Please provide it in UTC of format: YYYY-MM-DD'T'HH:mm:ss.SSSZ"
        } else {
            ex.cause?.cause?.message
        }
        val error = ControllerException(
            message = rootCauseMessage,
            status = HttpStatus.BAD_REQUEST.value(),
            errors = mutableListOf("Invalid input."),
            timestamp = LocalDateTime.now()
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error)
    }

    @ExceptionHandler(DeliveryNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // 404
    fun handleEntityNotFoundException(ex: DeliveryNotFoundException): ResponseEntity<ControllerException> {
        val error = ControllerException(
            message = ex.message,
            status = HttpStatus.NOT_FOUND.value(),
            errors = mutableListOf("Entity not found."),
            timestamp = LocalDateTime.now()
        )
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error)
    }

    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(ex: RuntimeException): ResponseEntity<ControllerException> {
        log.debug("Caught RuntimeException: $ex")
        val error = ControllerException(
            message = ex.message,
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            errors = mutableListOf("Som ting wong."),
            LocalDateTime.now()
        )
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error)
    }
}