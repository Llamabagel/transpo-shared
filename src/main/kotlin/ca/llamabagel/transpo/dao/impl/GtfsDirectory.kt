/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.dao.impl

import ca.llamabagel.transpo.dao.gtfs.*
import ca.llamabagel.transpo.models.gtfs.*
import java.lang.NumberFormatException
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
            return Files.lines(path.resolve("stops.txt")).use { stream ->
                var stop: Stop? = null

                stream.skip(1).forEach {
                    val csv = CsvStop(it.split(","))
                    if (csv.id == id) {
                        stop = csv.toStop()
                    }
                }

                return@use stop
            }
        }

        override fun getByCode(code: String): List<Stop> {
            val stops = ArrayList<Stop>()
            Files.lines(path.resolve("stops.txt")).use { stream ->
                stream.skip(1).forEach {
                    val csv = CsvStop(it.split(","))
                    if (csv.code == code) {
                        stops.add(csv.toStop())
                    }
                }
            }

            return stops
        }

        override fun getAll(): List<Stop> {
            val stops = ArrayList<Stop>()
            Files.lines(path.resolve("stops.txt")).use { stream ->
                stream.skip(1).forEach {
                    stops.add(CsvStop(it.split(",")).toStop())
                }
            }

            return stops
        }

        override fun insert(vararg t: Stop): Boolean {
            var values = "\n"

            t.forEach {
                if (getById(it.id) != null) {
                    return false
                } else {
                    values += "${CsvStop(it).toCsvRow()}\n"
                }
            }

            Files.write(path.resolve("stops.txt"), values.toByteArray(), StandardOpenOption.APPEND)

            return true
        }

        override fun update(vararg t: Stop): Boolean {
            TODO("not implemented")
        }

        override fun delete(vararg t: Stop): Boolean {
            TODO("not implemented")
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

private inline class CsvStop(val parts: List<String?>) {
    constructor(stop: Stop)
            : this(listOf(stop.id.value, stop.code, stop.name, stop.description, stop.latitude.toString(), stop.longitude.toString(), stop.zoneId?.toString(), stop.stopUrl, stop.locationType?.toString()))

    inline val id: StopId get() = parts[0].asStopId()!!
    inline val code: String? get() = parts[1]
    inline val name: String get() = parts[2]!!
    inline val description: String? get() = parts[3]
    inline val latitude: Double get() = try { parts[4]!!.toDouble() } catch (e: NumberFormatException) { Double.NaN }
    inline val longitude: Double get() = try { parts[5]!!.toDouble() } catch (e: NumberFormatException) { Double.NaN }
    inline val zoneId: Int? get() = parts[6]?.toIntOrNull()
    inline val stopUrl: String? get() = parts[7]
    inline val locationType: Int? get() = parts[8]?.toIntOrNull()

    fun toStop() = Stop(id, code, name, description, latitude, longitude,
            zoneId, stopUrl, locationType, null, null, null)

    fun toCsvRow() = "${id.value},$code,$name,${description ?: "null"},$latitude,$longitude,$zoneId,${stopUrl ?: "null"},$locationType"
}