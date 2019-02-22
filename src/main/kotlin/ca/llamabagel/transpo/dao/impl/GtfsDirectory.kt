/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.dao.impl

import ca.llamabagel.transpo.dao.gtfs.*
import ca.llamabagel.transpo.models.gtfs.*
import java.nio.file.Files
import java.nio.file.Path

/**
 * A data source for GTFS data based out of a file directory containing the GTFS files.
 * This is currently designed to work with GTFS files provided by OC Transpo. Will require generalizations to support
 * most other GTFS data sets.
 *
 * @property path The path to the directory containing the GTFS files
 */
open class GtfsDirectory(val path: Path) : GtfsSource() {

    protected open val stopHeaders = listOf("stop_id", "stop_code", "stop_name", "stop_desc", "stop_lat", "stop_lon",
            "zone_id", "stop_url", "location_type", "parent_station", "time_zone", "wheelchair_boarding")

    init {
        val stopsCsv = path.resolve("stops.txt")
        if (Files.notExists(stopsCsv)) {
            Files.createFile(stopsCsv)
        }

        val stopsHeaders = "stop_id,stop_code,stop_name,stop_desc,stop_lat,stop_lon,zone_id,stop_url,location_type,parent_station,time_zone,wheelchair_boarding"
        if (Files.lines(stopsCsv).use { it.findFirst().get() } != stopsHeaders) {
            Files.write(stopsCsv, listOf(stopsHeaders))
        }
    }

    /**
     * GTFS directory and stops.txt implementation of the StopDao.
     * Reads the stops.txt file from a GTFS directory for its methods.
     */
    override val stops = object : StopDao {

        private val csvPath = path.resolve("stops.txt")

        override fun getById(id: StopId): Stop? {
            return getItemByKey(csvPath, ::CsvStop, Stop.key, id)
        }

        override fun getByCode(code: String): List<Stop> {
            return getItemsByKey(csvPath, ::CsvStop, {
                when (it) {
                    is CsvStop -> it.code
                    is Stop -> it.code
                    else -> throw IllegalArgumentException("$it was not a Stop or CsvStop object")
                }
            }, code)
        }

        override fun getAll(): List<Stop> {
            return getAllItems(csvPath, ::CsvStop)
        }

        override fun insert(vararg t: Stop): Boolean {
            return insertCsvRows(csvPath, this::getById, Stop.key, ::CsvStop, *t)
        }

        override fun update(vararg t: Stop): Boolean {
            return updateCsvRows(csvPath, ::CsvStop, ::CsvStop, Stop.key, *t)
        }

        override fun delete(vararg t: Stop): Boolean {
            return deleteCsvRows(csvPath, ::CsvStop, Stop.key, *t)
        }

    }

    /**
     * GTFS directory and stops.txt implementation of the RouteDao.
     * Reads the routes.txt file from a GTFS directory for its methods.
     */
    override val routes = object : RouteDao {

        private val csvPath = path.resolve("routes.txt")

        override fun getByNumber(number: String): Route? {
            return getItemByKey(csvPath, ::CsvRoute, {
                when (it) {
                    is CsvRoute -> it.shortName
                    is Route -> it.shortName
                    else -> throw IllegalArgumentException("$it was not a Route or CsvRoute object")
                }
            }, number)
        }

        override fun getById(id: RouteId): Route? {
            return getItemByKey(csvPath, ::CsvRoute, Route.key, id)
        }

        override fun getAll(): List<Route> {
            return getAllItems(csvPath, ::CsvRoute)
        }

        override fun insert(vararg t: Route): Boolean {
            return insertCsvRows(csvPath, this::getById, Route.key, ::CsvRoute, *t)
        }

        override fun update(vararg t: Route): Boolean {
            return updateCsvRows(csvPath, ::CsvRoute, ::CsvRoute, Route.key, *t)
        }

        override fun delete(vararg t: Route): Boolean {
            return deleteCsvRows(csvPath, ::CsvRoute, Route.key, *t)
        }

    }

    override val agencies: AgencyDao = object : AgencyDao {

        private val csvPath = path.resolve("agency.txt")

        override fun getById(id: AgencyId): Agency? {
            return getItemByKey(csvPath, ::CsvAgency, Agency.key, id)
        }

        override fun getAll(): List<Agency> {
            return getAllItems(csvPath, ::CsvAgency)
        }

        override fun insert(vararg t: Agency): Boolean {
            return insertCsvRows(csvPath, this::getById, Agency.key, ::CsvAgency, *t)
        }

        override fun update(vararg t: Agency): Boolean {
            return updateCsvRows(csvPath, ::CsvAgency, ::CsvAgency, Agency.key, *t)
        }

        override fun delete(vararg t: Agency): Boolean {
            return deleteCsvRows(csvPath, ::CsvAgency, Agency.key, *t)
        }
    }

    override val calendars: CalendarDao = object : CalendarDao {

        private val csvPath = path.resolve("calendar.txt")

        override fun getByServiceId(serviceId: CalendarServiceId): Calendar? {
            return getItemByKey(csvPath, ::CsvCalendar, Calendar.key, serviceId)
        }

        override fun getByDays(monday: Int, tuesday: Int, wednesday: Int, thursday: Int, friday: Int, saturday: Int, sunday: Int): List<Calendar> {
            val map = mutableMapOf<Int, Int>()
            if (monday != -1) map[1] = monday
            if (tuesday != -1) map[2] = tuesday
            if (wednesday != -1) map[3] = wednesday
            if (thursday != -1) map[4] = thursday
            if (friday != -1) map[5] = friday
            if (saturday != -1) map[6] = saturday
            if (sunday != -1) map[7] = sunday

            return getItemsByKey(csvPath, ::CsvCalendar, {
                val days = mutableMapOf<Int, Int>()
                when (it) {
                    is CsvCalendar -> {
                        if (monday != -1) days[1] = it.monday
                        if (tuesday != -1) days[2] = it.tuesday
                        if (wednesday != -1) days[3] = it.wednesday
                        if (thursday != -1) days[4] = it.thursday
                        if (friday != -1) days[5] = it.friday
                        if (saturday != -1) days[6] = it.saturday
                        if (sunday != -1) days[7] = it.sunday
                    }
                    is Calendar -> {
                        if (monday != -1) days[1] = it.monday
                        if (tuesday != -1) days[2] = it.tuesday
                        if (wednesday != -1) days[3] = it.wednesday
                        if (thursday != -1) days[4] = it.thursday
                        if (friday != -1) days[5] = it.friday
                        if (saturday != -1) days[6] = it.saturday
                        if (sunday != -1) days[7] = it.sunday
                    }
                    else -> throw IllegalArgumentException("$it was not a Calendar or CsvCalendar object")
                }

                return@getItemsByKey days
            }, map)
        }

        override fun getAll(): List<Calendar> {
            return getAllItems(csvPath, ::CsvCalendar)
        }

        override fun insert(vararg t: Calendar): Boolean {
            return insertCsvRows(csvPath, this::getByServiceId, Calendar.key, ::CsvCalendar, *t)
        }

        override fun update(vararg t: Calendar): Boolean {
            return updateCsvRows(csvPath, ::CsvCalendar, ::CsvCalendar, Calendar.key, *t)
        }

        override fun delete(vararg t: Calendar): Boolean {
            return deleteCsvRows(csvPath, ::CsvCalendar, Calendar.key, *t)
        }
    }

    override val calendarDates: CalendarDateDao = object : CalendarDateDao {

        private val csvPath = path.resolve("calendar_dates.txt")

        override fun getByServiceId(serviceId: CalendarServiceId): List<CalendarDate> {
            return getItemsByKey(csvPath, ::CsvCalendarDate, {
                when (it) {
                    is CsvCalendarDate -> it.serviceId
                    is CalendarDate -> it.serviceId
                    else -> throw IllegalArgumentException("$it was not a CalendarDate or CsvCalendarDate object")
                }
            }, serviceId)
        }

        override fun getByDate(date: String): List<CalendarDate> {
            return getItemsByKey(csvPath, ::CsvCalendarDate, {
                when (it) {
                    is CsvCalendarDate -> it.date
                    is CalendarDate -> it.date
                    else -> throw IllegalArgumentException("$it was not a CalendarDate or CsvCalendarDate object")
                }
            }, date)
        }

        override fun getAll(): List<CalendarDate> {
            return getAllItems(csvPath, ::CsvCalendarDate)
        }

        override fun insert(vararg t: CalendarDate): Boolean {
            return insertCsvRows(csvPath, { getItemByKey(csvPath, ::CsvCalendarDate, CalendarDate.key, it) },
                    CalendarDate.key, ::CsvCalendarDate, *t)
        }

        override fun update(vararg t: CalendarDate): Boolean {
            return updateCsvRows(csvPath, ::CsvCalendarDate, ::CsvCalendarDate, CalendarDate.key, *t)
        }

        override fun delete(vararg t: CalendarDate): Boolean {
            return deleteCsvRows(csvPath, ::CsvCalendarDate, CalendarDate.key, *t)
        }
    }

    override val stopTimes: StopTimeDao = object : StopTimeDao {

        private val csvPath = path.resolve("stop_times.txt")

        override fun getByTripId(tripId: TripId): List<StopTime> {
            return getItemsByKey(csvPath, ::CsvStopTime, {
                when (it) {
                    is CsvStopTime -> it.tripId
                    is StopTime -> it.tripId
                    else -> throw java.lang.IllegalArgumentException("$it was not a StopTime or CsvStopTime object")
                }
            }, tripId)
        }

        override fun getByStopId(stopId: StopId): List<StopTime> {
            return getItemsByKey(csvPath, ::CsvStopTime, {
                when (it) {
                    is CsvStopTime -> it.stopId
                    is StopTime -> it.stopId
                    else -> throw java.lang.IllegalArgumentException("$it was not a StopTime or CsvStopTime object")
                }
            }, stopId)
        }

        override fun getAll(): List<StopTime> {
            return getAllItems(csvPath, ::CsvStopTime)
        }

        override fun insert(vararg t: StopTime): Boolean {
            return insertCsvRows(csvPath, { getItemByKey(csvPath, ::CsvStopTime, StopTime.key, it) }, StopTime.key, ::CsvStopTime, *t)
        }

        override fun update(vararg t: StopTime): Boolean {
            return updateCsvRows(csvPath, ::CsvStopTime, ::CsvStopTime, StopTime.key, *t)
        }

        override fun delete(vararg t: StopTime): Boolean {
            return deleteCsvRows(csvPath, ::CsvStopTime, StopTime.key, *t)
        }
    }

    override val trips: TripDao = object : TripDao {

        private val csvPath = path.resolve("trips.txt")

        override fun getByRouteId(routeId: RouteId): List<Trip> {
            return getItemsByKey(csvPath, ::CsvTrip, {
                when (it) {
                    is CsvTrip -> it.routeId
                    is Trip -> it.routeId
                    else -> throw java.lang.IllegalArgumentException("$it was not a Trip or CsvTrip object")
                }
            }, routeId)
        }

        override fun getByRouteId(routeId: RouteId, directionId: Int): List<Trip> {
            return getItemsByKey(csvPath, ::CsvTrip, {
                when (it) {
                    is CsvTrip -> "${it.routeId.value}//${it.directionId}"
                    is Trip -> "${it.routeId.value}//${it.directionId}"
                    else -> throw java.lang.IllegalArgumentException("$it was not a Trip or CsvTrip object")
                }
            }, "${routeId.value}//$directionId")
        }

        override fun getByTripId(id: TripId): Trip? {
            return getItemByKey(csvPath, ::CsvTrip, Trip.key, id)
        }

        override fun getByServiceId(serviceId: CalendarServiceId): List<Trip> {
            return getItemsByKey(csvPath, ::CsvTrip, {
                when (it) {
                    is CsvTrip -> it.serviceId
                    is Trip -> it.serviceId
                    else -> throw java.lang.IllegalArgumentException("$it was not a Trip or CsvTrip object")
                }
            }, serviceId)
        }

        override fun getAll(): List<Trip> {
            return getAllItems(csvPath, ::CsvTrip)
        }

        override fun insert(vararg t: Trip): Boolean {
            return insertCsvRows(csvPath, this::getByTripId, Trip.key, ::CsvTrip, *t)
        }

        override fun update(vararg t: Trip): Boolean {
            return updateCsvRows(csvPath, ::CsvTrip, ::CsvTrip, Trip.key, *t)
        }

        override fun delete(vararg t: Trip): Boolean {
            return deleteCsvRows(csvPath, ::CsvTrip, Trip.key, *t)
        }
    }

}
