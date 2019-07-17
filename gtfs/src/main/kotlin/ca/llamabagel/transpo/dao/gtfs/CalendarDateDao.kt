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
     * @see [listByServiceId]
     *
     * @param serviceId The service id to search for
     * @return [Sequence] of all [CalendarDate]s with that service id
     */
    fun getByServiceId(serviceId: CalendarServiceId): Sequence<CalendarDate>

    /**
     * Gets all [CalendarDate] items for a specified date.
     * The date should be provided in YYYYMMDD format.
     * @see [listByDate]
     *
     * @param date The date in YYYYMMDD format.
     * @return A [Sequence] of all [CalendarDate]s on this date.
     */
    fun getByDate(date: String): Sequence<CalendarDate>
}

fun CalendarDateDao.listByServiceId(serviceId: CalendarServiceId) = getByServiceId(serviceId).toList()

fun CalendarDateDao.listByDate(date: String) = getByDate(date).toList()