package com.ahold.ctp.assignment.controller.exception

import java.time.LocalDateTime

data class ControllerException(
    val message: String? = null,
    val status: Int = 0,
    val errors: MutableList<String?>,
    val timestamp: LocalDateTime? = null,
)