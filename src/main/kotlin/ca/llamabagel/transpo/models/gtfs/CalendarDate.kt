/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.gtfs

/**
 * [https://developers.google.com/transit/gtfs/reference/#calendar_datestxt]
 */
data class CalendarDate(val serviceId: CalendarServiceId,
                        val date: String,
                        val exceptionType: Int) : GtfsObject() {
    companion object
}