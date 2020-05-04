/*
 * Copyright (c) 2020 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.utils

import java.time.format.DateTimeFormatter

enum class TimeFormat(val formatter: DateTimeFormatter) {
    FORMAT_12_HOUR(DateTimeFormatter.ofPattern("hh:mm a")),
    FORMAT_24_HOUR(DateTimeFormatter.ofPattern("HH:mm"))
}