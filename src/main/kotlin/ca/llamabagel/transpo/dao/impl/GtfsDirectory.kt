package ca.llamabagel.transpo.dao.impl

import ca.llamabagel.transpo.dao.gtfs.*
import ca.llamabagel.transpo.models.gtfs.Route
import ca.llamabagel.transpo.models.gtfs.Stop
import java.nio.file.Files
import java.nio.file.Paths

/**
 * A data source for GTFS data based out of a file directory containing the GTFS files.
 *
 * @property path The path to the directory containing the GTFS files
 */
class GtfsDirectory(val path: String) : GtfsSource() {

    /**
     * GTFS directory and stops.txt implementation of the StopDao.
     * Reads the stops.txt file from a GTFS directory for its methods.
     */
    override val stops = object : StopDao {

        /**
         * Creates a [Stop] object from the parts off a line of a csv file.
         *
         * @param parts The parts of the line of csv. Usually obtained by String.split(",").
         * @return The [Stop] object created from the line.
         */
        private fun makeStopFromCsv(parts: List<String>) =
                Stop(parts[0], parts[1], parts[2], parts[3], parts[4].toDouble(), parts[5].toDouble(),
                        parts[6].toIntOrNull(), parts[7], parts[8].toIntOrNull(), null, null, null)

        override fun getById(id: String): Stop? {
            var stop: Stop? = null
            Files.lines(Paths.get("$path/stops.txt")).use { stream ->
                stream.forEach {
                    val parts = it.split(",")
                    if (parts[0] == id) {
                        stop = makeStopFromCsv(parts)
                    }
                }
            }

            return stop
        }

        override fun getByCode(code: String): List<Stop> {
            val stops = ArrayList<Stop>()
            Files.lines(Paths.get("$path/stops.txt")).use { stream ->
                stream.forEach {
                    val parts = it.split(",")
                    if (parts[1] == code) {
                        stops.add(makeStopFromCsv(parts))
                    }
                }
            }

            return stops
        }

        override fun getAll(): List<Stop> {
            val stops = ArrayList<Stop>()
            Files.lines(Paths.get("$path/stops.txt")).use { stream ->
                stream.forEach {
                    stops.add(makeStopFromCsv(it.split(",")))
                }
            }

            return stops
        }

        override fun insert(vararg t: Stop): Boolean {
            TODO("not implemented")
            /*Files.write(Paths.get("$path/stops.txt"),
                    "${t.id},${t.code},${t.name},${t.description},${t.latitude},${t.longitude},${t.zoneId},${t.stopUrl},${t.locationType}".toByteArray(),
                    StandardOpenOption.APPEND)*/
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
                Route(parts[0], null, parts[1], parts[2], parts[3], parts[4].toInt(),
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

        override fun getById(id: String): Route? {
            var route: Route? = null
            Files.lines(Paths.get("$path/routes.txt")).use { stream ->
                stream.forEach {
                    val parts = it.split(",")
                    if (parts[0] == id) {
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

    override val agencies: AgencyDao = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override val calendars: CalendarDao = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override val calendarDates: CalendarDateDao = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override val stopTimes: StopTimeDao = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override val trips: TripDao = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

}