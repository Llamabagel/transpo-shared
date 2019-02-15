/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.gtfs

/**
 * [https://developers.google.com/app/gtfs/reference/#stop_timestxt]
 */
data class StopTime(val tripId: TripId,
                    val arrivalTime: String,
                    val departureTime: String,
                    val stopId: StopId,
                    val stopSequence: Int,
                    val stopHeadsign: String?,
                    val pickupType: Int?,
                    val dropOffType: Int?,
                    val shapeDistanceTraveled: Double?,
                    val timepoint: Int?)