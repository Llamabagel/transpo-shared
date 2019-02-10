package ca.llamabagel.transpo.dao.gtfs

import ca.llamabagel.transpo.dao.Dao
import ca.llamabagel.transpo.models.gtfs.Agency

interface AgencyDao : Dao<Agency> {

    /**
     * Get an agency by its id.
     *
     * @param id The agency's id.
     * @return An [Agency] object, or null if the agency with the specified id does not exist.
     */
    fun getById(id: String): Agency?

}