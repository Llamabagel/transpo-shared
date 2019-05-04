/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.gtfs

/**
 * [https://developers.google.com/transit/gtfs/reference/#calendartxt]
 */
data class Calendar(val serviceId: CalendarServiceId,
                    val monday: Int,
                    val tuesday: Int,
                    val wednesday: Int,
                    val thursday: Int,
                    val friday: Int,
                    val saturday: Int,
                    val sunday: Int,
                    val startDate: String,
                    val endDate: String) : GtfsObject() {
    companion object
}

inline class CalendarServiceId(val value: String)
fun String?.asCalendarServiceId() = if (this == null) null else CalendarServiceId(this)
