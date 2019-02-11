/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.dao.gtfs

import ca.llamabagel.transpo.dao.Dao
import ca.llamabagel.transpo.models.gtfs.Stop

interface StopDao : Dao<Stop> {

    /**
     * Get a stop by its unique id.
     * @see [Stop.id]
     *
     * @param id The id of the stop to retrieve.
     * @return The [Stop] with the corresponding [id]. null if it doesn't exist.
     */
    fun getById(id: String): Stop?

    /**
     * Gets all stops with the given [code]. Since codes are not
     * necessarily unique, this may return any number of [Stop] objects.
     * @see [Stop.code]
     *
     * @param code The code of the [Stop] to retrieve.
     * @return The list of [Stop] objects that have the given [code].
     */
    fun getByCode(code: String): List<Stop>

}