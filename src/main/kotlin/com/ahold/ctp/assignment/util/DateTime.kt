package com.ahold.ctp.assignment.util

import java.time.OffsetDateTime


// atStartOfDay() is only available for LocalDate
fun OffsetDateTime.startOfDay(): OffsetDateTime {
    return this.toLocalDate().atStartOfDay().atOffset(this.offset)
}


fun OffsetDateTime.endOfDay(): OffsetDateTime =
    this.startOfDay().plusDays(1).minusNanos(1)


