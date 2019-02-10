package ca.llamabagel.transpo.dao.gtfs

import ca.llamabagel.transpo.dao.Dao
import ca.llamabagel.transpo.models.gtfs.Stop
import ca.llamabagel.transpo.models.gtfs.StopTime
import ca.llamabagel.transpo.models.gtfs.Trip

interface StopTimeDao : Dao<StopTime> {

    /**
     * Gets all stop times for a trip.
     *
     * @param trip The trip to get all stop times for
     * @return A list of all [StopTime]s on the trip
     */
    fun getByTrip(trip: Trip): List<StopTime>

    /**
     * Gets all stop times at a stop.
     *
     * @param stop The stop to get all stop times for
     * @return A list of all [StopTime]s for that route.
     */
    fun getByStop(stop: Stop): List<StopTime>

}