package ca.llamabagel.transpo.dao.gtfs

import ca.llamabagel.transpo.dao.Dao
import ca.llamabagel.transpo.models.gtfs.Trip
import ca.llamabagel.transpo.models.gtfs.Route
import ca.llamabagel.transpo.models.gtfs.Calendar

interface TripDao : Dao<Trip> {

    /**
     * Gets a list of trips by run by a specified route based on the route's ID.
     * @see [Route.id]
     *
     * @param route The route to get a list of trips for
     * @return A list of [Trip]s that are run by the requested route.
     */
    fun getByRoute(route: Route): List<Trip>

    /**
     * Gets a list of trips by run by a specified route and in a specified direction.
     *
     * @param route The route to get a list of trips for
     * @param directionId The direction of the trip (0 or 1)
     * @return A list of [Trip]s that are run by the requested route.
     */
    fun getByRoute(route: Route, directionId: Int): List<Trip>

    /**
     * Gets a trip by its id.
     *
     * @param id The trip's id.
     * @return The [Trip] object, null if it does not exist.
     */
    fun getTripById(id: String): Trip?

    /**
     * Gets a list of trips for a specified service id.
     * @see [Calendar]
     *
     * @param serviceId The id of the service type. Taken from [Calendar].
     * @return A list of matching [Trip]s.
     */
    fun getByServiceId(serviceId: String): List<Trip>
}