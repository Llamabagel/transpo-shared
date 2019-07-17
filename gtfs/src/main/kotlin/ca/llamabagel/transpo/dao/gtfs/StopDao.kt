/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.dao.gtfs

import ca.llamabagel.transpo.dao.UpdatableDao
import ca.llamabagel.transpo.models.gtfs.Stop
import ca.llamabagel.transpo.models.gtfs.StopId

interface StopDao : UpdatableDao<Stop> {

    /**
     * Get a stop by its unique id.
     * @see [Stop.id]
     *
     * @param id The id of the stop to retrieve.
     * @return The [Stop] with the corresponding [id]. null if it doesn't exist.
     */
    fun getById(id: StopId): Stop?

    /**
     * Gets all stops with the given [code]. Since codes are not
     * necessarily unique, this may return any number of [Stop] objects.
     * @see [Stop.code]
     * @see [listByCode]
     *
     * @param code The code of the [Stop] to retrieve.
     * @return The list of [Stop] objects that have the given [code].
     */
    fun getByCode(code: String): Sequence<Stop>
}

/**
 * Convenience extension to get all stops with the given [code] as a [List]
 */
fun StopDao.listByCode(code: String) = getByCode(code).toList()