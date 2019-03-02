/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.dao.impl

import ca.llamabagel.transpo.dao.gtfs.*
import ca.llamabagel.transpo.models.gtfs.*
import java.nio.file.Path

/**
 * A data source for GTFS data based out of a file directory containing the GTFS files.
 * This is currently designed to work with GTFS files provided by OC Transpo. Will require generalizations to support
 * most other GTFS data sets.
 *
 * @property path The path to the directory containing the GTFS files
 */
open class GtfsDirectory(val path: Path) : GtfsSource() {

    protected open val stopsTable = csvTable<Stop> {
        path = this@GtfsDirectory.path.resolve("stops.txt")
        headers = listOf("stop_id", "stop_code", "stop_name", "stop_desc", "stop_lat", "stop_lon",
                "zone_id", "stop_url", "location_type", "parent_station", "time_zone", "wheelchair_boarding")

        objectInitializer {
            Stop(it[0].asStopId()!!, it[1].nullIfBlank(), it[2]!!, it[3].nullIfBlank(), it[4]?.toDoubleOrNull()
                    ?: Double.NaN, it[5]?.toDoubleOrNull()
                    ?: Double.NaN, it[6]?.toIntOrNull(), it[7].nullIfBlank(), it[8]?.toIntOrNull(), it[9].nullIfBlank(), it[10].nullIfBlank(), it[11]?.toIntOrNull())
        }

        partsInitializer {
            listOf(it.id.value, it.code, it.name, it.description, it.latitude.toString(), it.longitude.toString(),
                    it.zoneId?.toString(), it.stopUrl, it.locationType?.toString(), it.parentStation, it.timeZone,
                    it.wheelchairBoarding?.toString())
        }
    }

    protected open val routesTable = csvTable<Route> {
        path = this@GtfsDirectory.path.resolve("routes.txt")
        headers = listOf("route_id", "agency_id", "route_short_name", "route_long_name", "route_desc", "route_type", "route_url", "route_color", "route_text_color", "route_sort_order")

        objectInitializer {
            Route(it[0].asRouteId()!!, it[1].nullIfBlank().asAgencyId(), it[2]!!, it[3]!!, it[4].nullIfBlank(), it[5]!!.toInt(), it[6].nullIfBlank(), it[7].nullIfBlank(), it[8].nullIfBlank(), it[9]?.toIntOrNull())
        }

        partsInitializer {
            listOf(it.id.value, it.agencyId?.value, it.shortName, it.longName, it.description, it.type.toString(), it.url, it.color, it.textColor, it.sortOrder?.toString())
        }
    }

    protected open val agencyTable = csvTable<Agency> {
        path = this@GtfsDirectory.path.resolve("agency.txt")
        headers = listOf("agency_id", "agency_name", "agency_url", "agency_timezone", "agency_lang", "agency_phone", "agency_fare_url", "agency_email")

        objectInitializer {
            Agency(it[0].asAgencyId()!!, it[1]!!, it[2]!!, it[3]!!, it[4].nullIfBlank(), it[5].nullIfBlank(), it[6].nullIfBlank(), it[7].nullIfBlank())
        }

        partsInitializer {
            listOf(it.id.value, it.name, it.url, it.timeZone, it.language, it.phone, it.fareUrl, it.email)
        }
    }

    protected open val calendarsTable = csvTable<Calendar> {
        path = this@GtfsDirectory.path.resolve("calendar.txt")
        headers = listOf("service_id", "monday", "tuesday", "wednesday", "thursday", "friday", "saturday", "sunday", "start_date", "end_date")

        objectInitializer {
            Calendar(it[0].asCalendarServiceId()!!, it[1]!!.toInt(), it[2]!!.toInt(), it[3]!!.toInt(), it[4]!!.toInt(), it[5]!!.toInt(), it[6]!!.toInt(), it[7]!!.toInt(), it[8]!!, it[9]!!)
        }

        partsInitializer {
            listOf(it.serviceId.value, it.monday.toString(), it.tuesday.toString(), it.wednesday.toString(), it.thursday.toString(), it.friday.toString(), it.saturday.toString(), it.sunday.toString(), it.startDate, it.endDate)
        }
    }

    protected open val calendarDatesTable = csvTable<CalendarDate> {
        path = this@GtfsDirectory.path.resolve("calendar_dates.txt")
        headers = listOf("service_id", "date", "exception_type")

        objectInitializer {
            CalendarDate(it[0].asCalendarServiceId()!!, it[1]!!, it[2]!!.toInt())
        }

        partsInitializer {
            listOf(it.serviceId.value, it.date, it.exceptionType.toString())
        }
    }

    protected open val stopTimesTable = csvTable<StopTime> {
        path = this@GtfsDirectory.path.resolve("stop_times.txt")
        headers = listOf("trip_id", "arrival_time", "departure_time", "stop_id", "stop_sequence", "stop_headsign", "pickup_type", "drop_off_type", "shape_distance_traveled", "timepoint")

        objectInitializer {
            StopTime(it[0].asTripId()!!, it[1]!!, it[2]!!, it[3].asStopId()!!, it[4]!!.toInt(), it[5].nullIfBlank(), it[6]?.toIntOrNull(), it[7]?.toIntOrNull(), it[8]?.toDoubleOrNull(), it[9]?.toIntOrNull())
        }

        partsInitializer {
            listOf(it.tripId.value, it.arrivalTime, it.departureTime, it.stopId.value, it.stopSequence.toString(), it.stopHeadsign, it.pickupType?.toString(), it.dropOffType?.toString(), it.shapeDistanceTraveled?.toString(), it.timepoint?.toString())
        }
    }

    protected open val tripsTable = csvTable<Trip> {
        path = this@GtfsDirectory.path.resolve("trips.txt")
        headers = listOf("route_id", "service_id", "trip_id", "headsign", "short_name", "direction_id", "block_id", "shape_id", "wheelchair_accessible", "bikes_allowed")

        objectInitializer {
            Trip(it[0].asRouteId()!!, it[1].asCalendarServiceId()!!, it[2].asTripId()!!, it[3].nullIfBlank(), it[4].nullIfBlank(), it[5]?.toIntOrNull(), it[6].nullIfBlank(), it[7].nullIfBlank(), it[8]?.toIntOrNull(), it[9]?.toIntOrNull())
        }

        partsInitializer {
            listOf(it.routeId.value, it.serviceId.value, it.tripId.value, it.headsign, it.shortName, it.directionId?.toString(), it.blockId, it.shapeId, it.wheelchairAccessible?.toString(), it.bikesAllowed?.toString())
        }
    }

    override val stops = object : StopDao {

        override fun getById(id: StopId): Stop? {
            return stopsTable.getItemByKey(Stop.key, id)
        }

        override fun getByCode(code: String): List<Stop> {
            return stopsTable.getItemsByKey(Stop::code, code)
        }

        override fun getAll(): List<Stop> {
            return stopsTable.getAllItems()
        }

        override fun insert(vararg t: Stop): Boolean {
            return stopsTable.insertCsvRows(Stop.key, this::getById, *t)
        }

        override fun update(vararg t: Stop): Boolean {
            return stopsTable.updateCsvRows(Stop.key, *t)
        }

        override fun delete(vararg t: Stop): Boolean {
            return stopsTable.deleteCsvRows(Stop.key, *t)
        }
    }

    override val routes: RouteDao = object : RouteDao {

        override fun getByNumber(number: String): Route? {
            return routesTable.getItemByKey(Route::shortName, number)
        }

        override fun getById(id: RouteId): Route? {
            return routesTable.getItemByKey(Route.key, id)
        }

        override fun getAll(): List<Route> {
            return routesTable.getAllItems()
        }

        override fun insert(vararg t: Route): Boolean {
            return routesTable.insertCsvRows(Route.key, this::getById, *t)
        }

        override fun update(vararg t: Route): Boolean {
            return routesTable.updateCsvRows(Route.key, *t)
        }

        override fun delete(vararg t: Route): Boolean {
            return routesTable.deleteCsvRows(Route.key, *t)
        }
    }

    override val agencies: AgencyDao = object : AgencyDao {

        override fun getById(id: AgencyId): Agency? {
            return agencyTable.getItemByKey(Agency.key, id)
        }

        override fun getAll(): List<Agency> {
            return agencyTable.getAllItems()
        }

        override fun insert(vararg t: Agency): Boolean {
            return agencyTable.insertCsvRows(Agency.key, ::getById, *t)
        }

        override fun update(vararg t: Agency): Boolean {
            return agencyTable.updateCsvRows(Agency.key, *t)
        }

        override fun delete(vararg t: Agency): Boolean {
            return agencyTable.deleteCsvRows(Agency.key, *t)
        }
    }

    override val calendars: CalendarDao = object : CalendarDao {

        override fun getByServiceId(serviceId: CalendarServiceId): Calendar? {
            return calendarsTable.getItemByKey(Calendar.key, serviceId)
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

            return calendarsTable.getItemsByKey({
                val days = mutableMapOf<Int, Int>()

                // If no days are set return a key that can't match anything
                if (monday == -1 && tuesday == -1 && wednesday == -1 && thursday == -1 && friday == -1 && saturday == -1 && sunday == -1) {
                    days[0] = 2
                    return@getItemsByKey days
                }

                if (monday != -1) days[1] = it.monday
                if (tuesday != -1) days[2] = it.tuesday
                if (wednesday != -1) days[3] = it.wednesday
                if (thursday != -1) days[4] = it.thursday
                if (friday != -1) days[5] = it.friday
                if (saturday != -1) days[6] = it.saturday
                if (sunday != -1) days[7] = it.sunday


                return@getItemsByKey days
            }, map)
        }

        override fun getAll(): List<Calendar> {
            return calendarsTable.getAllItems()
        }

        override fun insert(vararg t: Calendar): Boolean {
            return calendarsTable.insertCsvRows(Calendar.key, this::getByServiceId, *t)
        }

        override fun update(vararg t: Calendar): Boolean {
            return calendarsTable.updateCsvRows(Calendar.key, *t)
        }

        override fun delete(vararg t: Calendar): Boolean {
            return calendarsTable.deleteCsvRows(Calendar.key, *t)
        }
    }

    override val calendarDates: CalendarDateDao = object : CalendarDateDao {

        override fun getByServiceId(serviceId: CalendarServiceId): List<CalendarDate> {
            return calendarDatesTable.getItemsByKey(CalendarDate::serviceId, serviceId)
        }

        override fun getByDate(date: String): List<CalendarDate> {
            return calendarDatesTable.getItemsByKey(CalendarDate::date, date)
        }

        override fun getAll(): List<CalendarDate> {
            return calendarDatesTable.getAllItems()
        }

        override fun insert(vararg t: CalendarDate): Boolean {
            return calendarDatesTable.insertCsvRows(CalendarDate.key, { calendarDatesTable.getItemByKey(CalendarDate.key, it) }, *t)
        }

        override fun delete(vararg t: CalendarDate): Boolean {
            return calendarDatesTable.deleteCsvRows(CalendarDate.key, *t)
        }
    }

    override val stopTimes: StopTimeDao = object : StopTimeDao {

        override fun getByTripId(tripId: TripId): List<StopTime> {
            return stopTimesTable.getItemsByKey(StopTime::tripId, tripId)
        }

        override fun getByStopId(stopId: StopId): List<StopTime> {
            return stopTimesTable.getItemsByKey(StopTime::stopId, stopId)
        }

        override fun getAll(): List<StopTime> {
            return stopTimesTable.getAllItems()
        }

        override fun insert(vararg t: StopTime): Boolean {
            return stopTimesTable.insertCsvRows(StopTime.key, { stopTimesTable.getItemByKey(StopTime.key, it) }, *t)
        }

        override fun delete(vararg t: StopTime): Boolean {
            return stopTimesTable.deleteCsvRows(StopTime.key, *t)
        }
    }

    override val trips: TripDao = object : TripDao {

        override fun getByRouteId(routeId: RouteId): List<Trip> {
            return tripsTable.getItemsByKey(Trip::routeId, routeId)
        }

        override fun getByRouteId(routeId: RouteId, directionId: Int): List<Trip> {
            return tripsTable.getItemsByKey({ "${it.routeId.value}//${it.directionId}" }, "${routeId.value}//$directionId")
        }

        override fun getByTripId(id: TripId): Trip? {
            return tripsTable.getItemByKey(Trip.key, id)
        }

        override fun getByServiceId(serviceId: CalendarServiceId): List<Trip> {
            return tripsTable.getItemsByKey(Trip::serviceId, serviceId)
        }

        override fun getAll(): List<Trip> {
            return tripsTable.getAllItems()
        }

        override fun insert(vararg t: Trip): Boolean {
            return tripsTable.insertCsvRows(Trip.key, this::getByTripId, *t)
        }

        override fun update(vararg t: Trip): Boolean {
            return tripsTable.updateCsvRows(Trip.key, *t)
        }

        override fun delete(vararg t: Trip): Boolean {
            return tripsTable.deleteCsvRows(Trip.key, *t)
        }
    }

}
