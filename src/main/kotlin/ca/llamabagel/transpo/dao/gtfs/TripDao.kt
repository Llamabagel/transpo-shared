/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.dao.gtfs

import ca.llamabagel.transpo.dao.Dao
import ca.llamabagel.transpo.models.gtfs.*

interface TripDao : Dao<Trip> {

    /**
     * Gets a list of trips by run by a specified route based on the route's ID.
     * @see [Route.id]
     *
     * @param route The route to get a list of trips for
     * @return A list of [Trip]s that are run by the requested route.
     */
    fun getByRouteId(routeId: RouteId): List<Trip>

    /**
     * Gets a list of trips by run by a specified route and in a specified direction.
     *
     * @param route The route to get a list of trips for
     * @param directionId The direction of the trip (0 or 1)
     * @return A list of [Trip]s that are run by the requested route.
     */
    fun getByRouteId(routeId: RouteId, directionId: Int): List<Trip>

    /**
     * Gets a trip by its id.
     *
     * @param id The trip's id.
     * @return The [Trip] object, null if it does not exist.
     */
    fun getByTripId(id: TripId): Trip?

    /**
     * Gets a list of trips for a specified service id.
     * @see [Calendar]
     *
     * @param serviceId The id of the service type. Taken from [Calendar].
     * @return A list of matching [Trip]s.
     */
    fun getByServiceId(serviceId: CalendarServiceId): List<Trip>
}