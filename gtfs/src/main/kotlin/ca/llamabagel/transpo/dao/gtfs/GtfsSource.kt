/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.dao.gtfs

abstract class GtfsSource {

    abstract val agencies: AgencyDao
    abstract val calendars: CalendarDao
    abstract val calendarDates: CalendarDateDao
    abstract val stops: StopDao
    abstract val routes: RouteDao
    abstract val stopTimes: StopTimeDao
    abstract val trips: TripDao
    abstract val shapes: ShapeDao?

}