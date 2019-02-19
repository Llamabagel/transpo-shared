/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.dao.impl

import ca.llamabagel.transpo.dao.gtfs.*
import ca.llamabagel.transpo.models.gtfs.*
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

/**
 * A data source for GTFS data based out of a file directory containing the GTFS files.
 * This is currently designed to work with GTFS files provided by OC Transpo. Will require generalizations to support
 * most other GTFS data sets.
 *
 * @property path The path to the directory containing the GTFS files
 */
class GtfsDirectory(val path: Path) : GtfsSource() {

    /**
     * GTFS directory and stops.txt implementation of the StopDao.
     * Reads the stops.txt file from a GTFS directory for its methods.
     */
    override val stops = object : StopDao {

        override fun getById(id: StopId): Stop? {
            return getItemByKey(path.resolve("stops.txt"), ::CsvStop, stopKey, id)
        }

        override fun getByCode(code: String): List<Stop> {
            return getItemsByKey(path.resolve("stops.txt"), ::CsvStop, {
                when (it) {
                    is CsvStop -> it.code
                    is Stop -> it.code
                    else -> throw IllegalArgumentException("$it was not a Stop or CsvStop object")
                }
            }, code)
        }

        override fun getAll(): List<Stop> {
            return getAllItems(path.resolve("stops.txt"), ::CsvStop)
        }

        override fun insert(vararg t: Stop): Boolean {
            return insertCsvRows(path.resolve("stops.txt"), this::getById, stopKey, ::CsvStop, *t)
        }

        override fun update(vararg t: Stop): Boolean {
            return updateCsvRows(path.resolve("stops.txt"), ::CsvStop, ::CsvStop, stopKey, *t)
        }

        override fun delete(vararg t: Stop): Boolean {
            return deleteCsvRows(path.resolve("stops.txt"), ::CsvStop, stopKey, *t)
        }

    }

    /**
     * GTFS directory and stops.txt implementation of the RouteDao.
     * Reads the routes.txt file from a GTFS directory for its methods.
     */
    override val routes = object : RouteDao {

        /**
         * Creates a [Route] object from the parts off a line of a csv file.
         *
         * @param parts The parts of the line of csv. Usually obtained by String.split(",").
         * @return The [Route] object created from the line.
         */
        private fun makeRouteFromCsv(parts: List<String>) =
                Route(parts[0].asRouteId()!!, null, parts[1], parts[2], parts[3], parts[4].toInt(),
                        parts[6], null, null, null)


        override fun getByNumber(number: String): Route? {
            var route: Route? = null
            Files.lines(Paths.get("$path/routes.txt")).use { stream ->
                stream.forEach {
                    val parts = it.split(",")
                    if (parts[1] == number) {
                        route = makeRouteFromCsv(parts)
                    }
                }
            }

            return route
        }

        override fun getById(id: RouteId): Route? {
            var route: Route? = null
            Files.lines(Paths.get("$path/routes.txt")).use { stream ->
                stream.forEach {
                    val parts = it.split(",")
                    if (parts[0] == id.value) {
                        route = makeRouteFromCsv(parts)
                    }
                }
            }

            return route
        }

        override fun getAll(): List<Route> {
            val routes = ArrayList<Route>()
            Files.lines(Paths.get("$path/routes.txt")).use { stream ->
                stream.forEach {
                    routes.add(makeRouteFromCsv(it.split(",")))
                }
            }

            return routes
        }

        override fun insert(vararg t: Route): Boolean {
            TODO("not implemented")
        }

        override fun update(vararg t: Route): Boolean {
            TODO("not implemented")
        }

        override fun delete(vararg t: Route): Boolean {
            TODO("not implemented")
        }

    }

    override val agencies: AgencyDao = object : AgencyDao {
        override fun getById(id: AgencyId): Agency? {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun getAll(): List<Agency> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun insert(vararg t: Agency): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun update(vararg t: Agency): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun delete(vararg t: Agency): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    override val calendars: CalendarDao = object : CalendarDao {
        override fun getByServiceId(serviceId: CalendarServiceId): Calendar? {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun getByDays(monday: Int, tuesday: Int, wednesday: Int, thursday: Int, friday: Int, saturday: Int, sunday: Int): List<Calendar> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun getAll(): List<Calendar> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun insert(vararg t: Calendar): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun update(vararg t: Calendar): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun delete(vararg t: Calendar): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    override val calendarDates: CalendarDateDao = object : CalendarDateDao {
        override fun getByServiceId(serviceId: CalendarServiceId): List<CalendarDate> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun getByDate(date: String): List<CalendarDate> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun getAll(): List<CalendarDate> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun insert(vararg t: CalendarDate): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun update(vararg t: CalendarDate): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun delete(vararg t: CalendarDate): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    override val stopTimes: StopTimeDao = object : StopTimeDao {
        override fun getByTripId(tripId: TripId): List<StopTime> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun getByStopId(stopId: StopId): List<StopTime> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun getAll(): List<StopTime> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun insert(vararg t: StopTime): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun update(vararg t: StopTime): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun delete(vararg t: StopTime): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    override val trips: TripDao = object : TripDao {
        override fun getByRouteId(routeId: RouteId): List<Trip> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun getByRouteId(routeId: RouteId, directionId: Int): List<Trip> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun getByTripId(id: TripId): Trip? {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun getByServiceId(serviceId: CalendarServiceId): List<Trip> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun getAll(): List<Trip> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun insert(vararg t: Trip): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun update(vararg t: Trip): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun delete(vararg t: Trip): Boolean {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

}

/*
private interface CsvObject<T> {
    fun toObject(): T

    fun toCsvRow(): String

    companion object {
        @JvmStatic
        fun partsToCsv(parts: List<String?>): String = parts.joinToString(separator = ",") { it ?: "" }
    }
}

private inline class CsvStop(val parts: List<String?>) : CsvObject<Stop> {

    constructor(string: String) : this(string.split(","))

    constructor(stop: Stop)
            : this(listOf(stop.id.value, stop.code, stop.name, stop.description, stop.latitude.toString(),
            stop.longitude.toString(), stop.zoneId?.toString(), stop.stopUrl, stop.locationType?.toString(),
            stop.parentStation, stop.timeZone, stop.wheelchairBoarding?.toString()))

    // Property accessors for a stop object. Gets the corresponding "part" from the csv.
    inline val id: StopId get() = parts[0].asStopId()!!
    inline val code: String? get() = if (parts[1] == "") null else parts[1]
    inline val name: String get() = parts[2]!!
    inline val description: String? get() = if (parts[3] == "") null else parts[3]
    inline val latitude: Double get() = parts[4]?.toDoubleOrNull() ?: Double.NaN
    inline val longitude: Double get() = parts[5]?.toDoubleOrNull() ?: Double.NaN
    inline val zoneId: Int? get() = parts[6]?.toIntOrNull()
    inline val stopUrl: String? get() = if (parts[7] == "") null else parts[7]
    inline val locationType: Int? get() = parts[8]?.toIntOrNull()
    inline val parentStation: String? get() = parts[9]
    inline val timeZone: String? get() = parts[10]
    inline val wheelchairBoarding: Int? get() = parts[11]?.toIntOrNull()

    override fun toObject() = Stop(id, code, name, description, latitude, longitude,
            zoneId, stopUrl, locationType, parentStation, timeZone, wheelchairBoarding)

    override fun toCsvRow(): String = CsvObject.partsToCsv(parts)
}*/
