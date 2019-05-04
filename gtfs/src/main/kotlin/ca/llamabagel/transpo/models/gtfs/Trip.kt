/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.models.gtfs

/**
 * [https://developers.google.com/transit/gtfs/reference/#tripstxt]
 */
data class Trip(val routeId: RouteId,
                val serviceId: CalendarServiceId,
                val tripId: TripId,
                val headsign: String?,
                val shortName: String?,
                val directionId: Int?,
                val blockId: String?,
                val shapeId: ShapeId?,
                val wheelchairAccessible: Int?,
                val bikesAllowed: Int?) : GtfsObject(){
    companion object
}

inline class TripId(val value: String)
fun String?.asTripId() = if (this == null) null else TripId(this)
