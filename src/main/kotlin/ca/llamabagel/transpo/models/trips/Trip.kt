/*
 * Copyright (c) 2017. Llamabagel
 *
 * This file is a part of Route 613.
 */

package ca.llamabagel.transpo.models.trips

import ca.llamabagel.transpo.models.LatLng
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by derek on 8/4/2017.
 * A trip. A bus. Something that will arrive at your bus stop!
 * @property destination The final destination of the trip
 * @property startTime The time the trip started/will start. A unique identifier for a trip.
 * @property adjustedScheduleTime The number of minutes until the trip arrives.
 * @property adjustmentAge The number of minutes ago the info was updated. -1 if the trip is not gps updated
 * @property lastTripOfSchedule Whether this bus is the last trip of a route for the day.
 * @property busType The type of bus being used.
 * @property latitude Latitude of the location of the bus. Could be null if [adjustmentAge] is -1.
 * @property longitude Longitude of the location of the bus. Could be null if [adjustmentAge] is -1.
 * @property gpsSpeed The current speed of the bus. Could be null if [adjustmentAge] is -1.
 * @property hasBikeRack Whether this bus is equipped with a bike rack.
 * @property punctuality A measure, in minutes, of how far off this trip's [adjustedScheduleTime] is from its scheduled time.
 */

data class Trip(
        @SerializedName("destination") val destination: String,
        @SerializedName("startTime") val startTime: String,
        @SerializedName("adjustedScheduleTime") val adjustedScheduleTime: Int,
        @SerializedName("adjustmentAge") val adjustmentAge: Float,
        @SerializedName("lastTripOfSchedule") val lastTripOfSchedule: Boolean,
        @SerializedName("busType") val busType: String,
        @SerializedName("latitude") val latitude: Double?,
        @SerializedName("longitude") val longitude: Double?,
        @SerializedName("gpsSpeed") val gpsSpeed: Float?,
        @SerializedName("hasBikeRack") val hasBikeRack: Boolean,
        @SerializedName("punctuality") val punctuality: Int
) {

    /**
     * Get a date object corresponding to the arrival time of this trip
     * @param date The date at which this trip data was retrieved. The current time by default.
     * @return The date that this trip will arrive
     */
    fun getArrivalDate(date: Date = Date()) = Date(date.time + TimeUnit.MINUTES.toMillis(adjustedScheduleTime.toLong()))

    /**
     * Get a formatted string format of the time that this trip will arrive.
     * @param date The date at which this trip data was retrieved. The current time by default.
     * @return The time that this trip will arrive in HH:mm format
     * TODO: Check to make sure the time doesn't wrap the clock, and also maybe handle 24hour clock stuff here?
     */
    fun getArrivalTimeString(date: Date = Date()): String {
        val simpleDateFormat = SimpleDateFormat("HH:mm", Locale.CANADA)
        return simpleDateFormat.format(getArrivalDate(date))
    }

    /**
     * Gets the location of this bus in a LatLng object.
     * @return A LatLng object of this trip's latitude and longitude, or null if the location does not exist.
     */
    fun getLocation() = if (latitude != null && longitude != null) LatLng(latitude, longitude) else null

}
