/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.dao.gtfs

import ca.llamabagel.transpo.dao.Dao
import ca.llamabagel.transpo.dao.UpdatableDao
import ca.llamabagel.transpo.models.gtfs.Calendar
import ca.llamabagel.transpo.models.gtfs.CalendarServiceId

interface CalendarDao : UpdatableDao<Calendar> {

    /**
     * Gets a [Calendar] by its serviceId. The serviceId is essentially the "name" of this
     * calendar item.
     *
     * @param serviceId The service id of the calendar
     * @return The corresponding [Calendar], or null if it doesn't exist
     */
    fun getByServiceId(serviceId: CalendarServiceId): Calendar?

    /**
     * Gets all [Calendar]s that match the provided values as weekday values.
     * If a day is not specified it will not be considered when looking for matching Calendars.
     *
     * Example usage:
     *
     * ```
     * // Gets all Calendars that have Monday set to '1'
     * val mondays = calendarDao.getByDays(monday = 1)
     *
     * // Gets all Calendars that have Tuesday set to '1' AND Thursday set to '0'
     * val days = calendarDao.getByDays(tuesday = 1, thursday = 0)
     *
     * // Gets all Calendars
     * val all = calendarDao.getByDays()
     * ```
     */
    fun getByDays(monday: Int = -1,
                  tuesday: Int = -1,
                  wednesday: Int = -1,
                  thursday: Int = -1,
                  friday: Int = -1,
                  saturday: Int = -1,
                  sunday: Int = -1) : List<Calendar>

}