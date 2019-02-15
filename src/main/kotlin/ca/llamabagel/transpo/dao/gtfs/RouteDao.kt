/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.dao.gtfs

import ca.llamabagel.transpo.dao.Dao
import ca.llamabagel.transpo.models.gtfs.Route
import ca.llamabagel.transpo.models.gtfs.RouteId

interface RouteDao : Dao<Route> {

    /**
     * Gets a [Route] by its route number.
     *
     * @param number The number of the Route
     * @return The [Route] with the corresponding route number. null if it doesn't exist.
     */
    fun getByNumber(number: String): Route?

    /**
     * Gets a [Route] by its unique id.
     *
     * @param id The id of the Route
     * @return The [Route] with the corresponding id. null if it doesn't exist.
     */
    fun getById(id: RouteId): Route?

}