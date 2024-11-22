package com.ahold.ctp.assignment.controller.exception

import com.ahold.ctp.assignment.service.DeliveryNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

@RestControllerAdvice
class ControllerExceptionHandler {
    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // 400
    fun handleInvalidArgumentException(ex: IllegalArgumentException): ResponseEntity<ControllerException> {
        val error = ControllerException(
            message = ex.message,
            status = HttpStatus.BAD_REQUEST.value(),
            errors = mutableListOf("Invalid request parameters."),
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
        val error = ControllerException(
            message = ex.message,
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            errors = mutableListOf("Som ting wong."),
            LocalDateTime.now()
        )
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error)
    }
}