/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.dao.gtfs

import ca.llamabagel.transpo.dao.UpdatableDao
import ca.llamabagel.transpo.models.gtfs.*

interface TripDao : UpdatableDao<Trip> {

    /**
     * Gets a list of trips by run by a specified route based on the route's ID.
     * @see [Route.id]
     * @see [listByRouteId]
     *
     * @param routeId The route to get a list of trips for
     * @return A list of [Trip]s that are run by the requested route.
     */
    fun getByRouteId(routeId: RouteId): Sequence<Trip>

    /**
     * Gets a list of trips by run by a specified route and in a specified direction.
     * @see [listByRouteId]
     *
     * @param routeId The route to get a list of trips for
     * @param directionId The direction of the trip (0 or 1)
     * @return A list of [Trip]s that are run by the requested route.
     */
    fun getByRouteId(routeId: RouteId, directionId: Int): Sequence<Trip>

    /**
     * Gets a trip by its id.
     * @see [listByServiceId]
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
    fun getByServiceId(serviceId: CalendarServiceId): Sequence<Trip>
}

fun TripDao.listByRouteId(routeId: RouteId) = getByRouteId(routeId).toList()

fun TripDao.listByRouteId(routeId: RouteId, directionId: Int) = getByRouteId(routeId, directionId).toList()

fun TripDao.listByServiceId(serviceId: CalendarServiceId) = getByServiceId(serviceId).toList()