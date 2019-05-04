/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.dao.gtfs

import ca.llamabagel.transpo.dao.Dao
import ca.llamabagel.transpo.models.gtfs.CalendarDate
import ca.llamabagel.transpo.models.gtfs.CalendarServiceId

interface CalendarDateDao : Dao<CalendarDate> {

    /**
     * Gets all [CalendarDate] items by their serviceId.
     *
     * @param serviceId The service id to search for
     * @return List of all [CalendarDate]s with that service id
     */
    fun getByServiceId(serviceId: CalendarServiceId): List<CalendarDate>

    /**
     * Gets all [CalendarDate] items for a specified date.
     * The date should be provided in YYYYMMDD format.
     *
     * @param date The date in YYYYMMDD format.
     * @return A list of all [CalendarDate]s on this date.
     */
    fun getByDate(date: String): List<CalendarDate>

}