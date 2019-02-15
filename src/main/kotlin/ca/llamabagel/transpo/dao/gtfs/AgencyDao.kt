/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.dao.gtfs

import ca.llamabagel.transpo.dao.Dao
import ca.llamabagel.transpo.models.gtfs.Agency
import ca.llamabagel.transpo.models.gtfs.AgencyId

interface AgencyDao : Dao<Agency> {

    /**
     * Get an agency by its id.
     *
     * @param id The agency's id.
     * @return An [Agency] object, or null if the agency with the specified id does not exist.
     */
    fun getById(id: AgencyId): Agency?

}