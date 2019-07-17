/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.dao.gtfs

import ca.llamabagel.transpo.dao.Dao
import ca.llamabagel.transpo.models.gtfs.*

interface StopTimeDao : Dao<StopTime> {
    /**
     * Gets all stop times for a trip.
     * @see listByTripId
     *
     * @param tripId The trip to get all stop times for
     * @return A list of all [StopTime]s on the trip
     */
    fun getByTripId(tripId: TripId): Sequence<StopTime>

    /**
     * Gets all stop times at a stop.
     * @see listByStopId
     *
     * @param stopId The stop to get all stop times for
     * @return A list of all [StopTime]s for that route.
     */
    fun getByStopId(stopId: StopId): Sequence<StopTime>
}

fun StopTimeDao.listByTripId(tripId: TripId) = getByTripId(tripId).toList()

fun StopTimeDao.listByStopId(stopId: StopId) = getByStopId(stopId).toList()