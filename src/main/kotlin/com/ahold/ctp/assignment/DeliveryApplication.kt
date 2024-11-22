package com.ahold.ctp.assignment

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
//@ComponentScan("com/ahold/ctp/assignment")
class DeliveryApplication

fun main(args: Array<String>) {
    runApplication<DeliveryApplication>(*args)
}
