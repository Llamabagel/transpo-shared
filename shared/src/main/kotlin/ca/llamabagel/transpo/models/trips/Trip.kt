/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.trips

import ca.llamabagel.transpo.models.LatLng
import ca.llamabagel.transpo.utils.TimeFormat
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAmount
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * A trip. A bus. Something that will arrive at your bus stop!
 *
 * @property destination The final destination of the trip
 * @property startTime The time the trip started/will start. A unique identifier for a trip.
 * @property adjustedScheduleTime The number of minutes until the trip arrives.
 * @property adjustmentAge The number of minutes ago the info was updated. -1 if the trip is not gps updated
 * @property lastTripOfSchedule Whether this bus is the last trip of a route for the day.
 * @property busType The type of bus being used.
 * @property position The [LatLng] of the bus. Could be `null` if [adjustmentAge] is `-1`.
 * @property gpsSpeed The current speed of the bus. Could be `null` if [adjustmentAge] is `-1`.
 * @property hasBikeRack Whether this bus is equipped with a bike rack.
 * @property punctuality A measure, in minutes, of how far off this trip's [adjustedScheduleTime] is from its scheduled time.
 */
@Serializable
data class Trip(
        val destination: String,
        val startTime: String,
        val adjustedScheduleTime: Long,
        val adjustmentAge: Float,
        val lastTripOfSchedule: Boolean,
        val busType: String,
        val hasBikeRack: Boolean,
        val punctuality: Int,
        val position: LatLng? = null,
        val gpsSpeed: Float? = null
) {

    /**
     * Get a date object corresponding to the arrival time of this trip
     * @param date The date at which this trip data was retrieved. The current time by default.
     * @return The date that this trip will arrive
     */
    fun getArrivalDate(date: OffsetDateTime = OffsetDateTime.now()) = date + Duration.ofMinutes(adjustedScheduleTime)

    /**
     * Get a formatted string format of the time that this trip will arrive.
     * @param date The date at which this trip data was retrieved. The current time by default.
     * @param format The [TimeFormat] to format the time as, either 12 hour or 24 hour time.
     * @return The time that this trip will arrive in HH:mm format
     */
    fun getArrivalTimeString(
            date: OffsetDateTime = OffsetDateTime.now(),
            format: TimeFormat = TimeFormat.FORMAT_12_HOUR
    ): String = getArrivalDate(date).format(format.formatter)
}
