/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

@file:Suppress("Duplicates")

package ca.llamabagel.transpo.dao.impl

import ca.llamabagel.transpo.dao.gtfs.*
import ca.llamabagel.transpo.models.gtfs.*
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Types

/**
 * GTFS data source that fetches values from a PostgreSQL database.
 *
 * @property connection The connection to the PostgreSQL database.
 */
class GtfsDatabase(private val connection: Connection) : GtfsSource() {

    override val stops = object : StopDao {
        override fun getById(id: String): Stop? {
            val result = connection.prepareStatement("SELECT * FROM stops WHERE id = ?")
                    .apply {
                        setString(1, id)
                    }
                    .executeQuery()

            return result.use {
                if (it.next()) getStopFromResultSet(it) else null
            }
        }

        override fun getByCode(code: String): List<Stop> {
            val result = connection.prepareStatement("SELECT * FROM stops WHERE code = ?")
                    .apply {
                        setString(1, code)
                    }
                    .executeQuery()

            return result.use {
                generateSequence {
                    if (it.next()) getStopFromResultSet(it) else null
                }.toList()
            }
        }

        override fun getAll(): List<Stop> {
            val result = connection.createStatement()
                    .executeQuery("SELECT * FROM stops")

            return result.use {
                generateSequence {
                    if (it.next()) getStopFromResultSet(it) else null
                }.toList()
            }
        }

        override fun insert(vararg t: Stop): Boolean {
            val statement = connection.prepareStatement("INSERT INTO stops VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")

            return statement.transact {
                for (i in t) {
                    setString(1, i.id)
                    setString(2, i.code)
                    setString(3, i.name)
                    setString(4, i.description)
                    setDouble(5, i.latitude)
                    setDouble(6, i.longitude)
                    setObject(7, i.zoneId, Types.INTEGER)
                    setString(8, i.stopUrl)
                    setObject(9, i.locationType, Types.INTEGER)
                    setString(10, i.parentStation)
                    setString(11, i.timeZone)
                    setObject(12, i.wheelchairBoarding, Types.INTEGER)

                    addBatch()
                }
            }
        }

        override fun update(vararg t: Stop): Boolean {
            val statement = connection.prepareStatement("UPDATE stops SET code = ?, name = ?, description = ?, latitude = ?, longitude = ?, zoneId = ?, stopUrl = ?, locationType = ?, parentStation = ?, timeZone = ?, wheelchairBoarding = ? WHERE id = ?")

            return statement.transact {
                for (i in t) {
                    setString(1, i.code)
                    setString(2, i.name)
                    setString(3, i.description)
                    setDouble(4, i.latitude)
                    setDouble(5, i.longitude)
                    setObject(6, i.zoneId, Types.INTEGER)
                    setString(7, i.stopUrl)
                    setObject(8, i.locationType, Types.INTEGER)
                    setString(9, i.parentStation)
                    setString(10, i.timeZone)
                    setObject(11, i.wheelchairBoarding, Types.INTEGER)
                    setString(12, i.id)

                    addBatch()
                }
            }
        }

        override fun delete(vararg t: Stop): Boolean {
            val statement = connection.prepareStatement("DELETE FROM stops WHERE id = ?")

            return statement.transact {
                for (i in t) {
                    setString(1, i.id)
                    statement.addBatch()
                }
            }
        }


        private fun getStopFromResultSet(resultSet: ResultSet) = Stop(
                id = resultSet.getString("id"),
                code = resultSet.getString("code"),
                name = resultSet.getString("name"),
                description = resultSet.getString("description"),
                latitude = resultSet.getDouble("latitude"),
                longitude = resultSet.getDouble("longitude"),
                zoneId = resultSet.getInt("zoneId"),
                stopUrl = resultSet.getString("stopUrl"),
                locationType = resultSet.getInt("locationType"),
                parentStation = resultSet.getString("parentStation"),
                timeZone = resultSet.getString("timeZone"),
                wheelchairBoarding = resultSet.getInt("wheelchairBoarding")
        )
    }

    override val routes = object : RouteDao {
        override fun getByNumber(number: String): Route? {
            val result = connection.prepareStatement("SELECT * FROM routes WHERE shortName = ?")
                    .apply {
                        setString(1, number)
                    }
                    .executeQuery()

            return result.use {
                if (it.next()) getRouteFromResultSet(it) else null
            }
        }

        override fun getById(id: String): Route? {
            val result = connection.prepareStatement("SELECT * FROM routes WHERE id = ?")
                    .apply {
                        setString(1, id)
                    }
                    .executeQuery()

            return result.use {
                if (it.next()) getRouteFromResultSet(it) else null
            }
        }

        override fun getAll(): List<Route> {
            val result = connection.prepareStatement("SELECT * FROM routes").executeQuery()

            return result.use {
                generateSequence { if (it.next()) getRouteFromResultSet(it) else null }.toList()
            }
        }

        override fun insert(vararg t: Route): Boolean {
            val statement = connection.prepareStatement("INSERT INTO routes VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")

            return statement.transact {
                for (i in t) {
                    setString(1, i.agencyId)
                    setString(2, i.agencyId)
                    setString(3, i.shortName)
                    setString(4, i.longName)
                    setString(5, i.description)
                    setInt(6, i.type)
                    setString(7, i.url)
                    setString(8, i.color)
                    setString(9, i.textColor)
                    setObject(10, i.sortOrder, Types.INTEGER)
                    addBatch()
                }
            }
        }

        override fun update(vararg t: Route): Boolean {
            val statement = connection.prepareStatement("UPDATE routes SET agencyId = ?, shortName = ?, longName = ?, description = ?, type = ?, url = ?, color = ?, textColor = ?, sortOrder = ? WHERE id = ?")

            return statement.transact {
                for (i in t) {
                    setString(1, i.agencyId)
                    setString(2, i.shortName)
                    setString(3, i.longName)
                    setString(4, i.description)
                    setInt(5, i.type)
                    setString(6, i.url)
                    setString(7, i.color)
                    setString(8, i.textColor)
                    setObject(9, i.sortOrder, Types.INTEGER)
                    setString(10, i.id)
                    addBatch()
                }
            }
        }

        override fun delete(vararg t: Route): Boolean {
            val statement = connection.prepareStatement("DELETE FROM routes WHERE id = ?")

            return statement.transact {
                for (i in t) {
                    setString(1, i.id)
                    statement.addBatch()
                }
            }
        }

        private fun getRouteFromResultSet(resultSet: ResultSet) = Route(
                id = resultSet.getString("id"),
                agencyId = resultSet.getString("agencyId"),
                shortName = resultSet.getString("shortName"),
                longName = resultSet.getString("longName"),
                description = resultSet.getString("description"),
                type = resultSet.getInt("type"),
                url = resultSet.getString("url"),
                color = resultSet.getString("color"),
                textColor = resultSet.getString("textColor"),
                sortOrder = resultSet.getInt("sortOrder")
        )
    }

    override val agencies: AgencyDao = object : AgencyDao {
        override fun getById(id: String): Agency? {
            val result = connection.prepareStatement("SELECT * FROM agencies WHERE id = ?")
                    .apply {
                        setString(1, id)
                    }
                    .executeQuery()

            return result.use {
                if (it.next()) getAgencyFromResultSet(it) else null
            }
        }

        override fun getAll(): List<Agency> {
            val result = connection.prepareStatement("SELECT * FROM agencies").executeQuery()

            return result.use {
                generateSequence { if (it.next()) getAgencyFromResultSet(it) else null }.toList()
            }
        }

        override fun insert(vararg t: Agency): Boolean {
            val statement = connection.prepareStatement("INSERT INTO agencies VALUES (?, ?, ?, ?, ?, ?, ?, ?)")

            return statement.transact {
                for (i in t) {
                    setString(1, i.id)
                    setString(2, i.name)
                    setString(3, i.url)
                    setString(4, i.timeZone)
                    setString(5, i.language)
                    setString(6, i.phone)
                    setString(7, i.fareUrl)
                    setString(8, i.email)
                    addBatch()
                }
            }
        }

        override fun update(vararg t: Agency): Boolean {
            val statement = connection.prepareStatement("UPDATE agencies SET name = ?, url = ?, timeZone = ?, language = ?, phone = ?, fareUrl = ?, email = ? WHERE id = ?")

            return statement.transact {
                for (i in t) {
                    setString(1, i.name)
                    setString(2, i.url)
                    setString(3, i.timeZone)
                    setString(4, i.language)
                    setString(5, i.phone)
                    setString(6, i.fareUrl)
                    setString(7, i.email)
                    setString(8, i.id)
                    addBatch()
                }
            }
        }

        override fun delete(vararg t: Agency): Boolean {
            val statement = connection.prepareStatement("DELETE FROM agencies WHERE id = ?")

            return statement.transact {
                for (i in t) {
                    setString(1, i.id)
                    statement.addBatch()
                }
            }
        }

        private fun getAgencyFromResultSet(resultSet: ResultSet) = Agency(
                id = resultSet.getString("id"),
                url = resultSet.getString("url"),
                name = resultSet.getString("name"),
                timeZone = resultSet.getString("timeZone"),
                language = resultSet.getString("language"),
                phone = resultSet.getString("phone"),
                fareUrl = resultSet.getString("fareUrl"),
                email = resultSet.getString("email")
        )
    }

    override val calendars: CalendarDao = object : CalendarDao {
        override fun getByServiceId(serviceId: String): Calendar? {
            val result = connection.prepareStatement("SELECT * FROM calendars WHERE serviceId = ?")
                    .apply {
                        setString(1, serviceId)
                    }
                    .executeQuery()

            return result.use {
                if (it.next()) getCalendarFromResultSet(it) else null
            }
        }

        override fun getByDays(monday: Int, tuesday: Int, wednesday: Int, thursday: Int, friday: Int, saturday: Int, sunday: Int): List<Calendar> {
            if (monday == -1 && tuesday == -1 && wednesday == -1 && thursday == -1 && friday == -1 && saturday == -1 && sunday == -1) {
                return emptyList()
            }

            // Concatenation to avoid sql syntax check in this case
            var sql = "SELECT * FROM calendars " + "WHERE "
            if (monday != -1) sql += "monday = ? AND "
            if (tuesday != -1) sql += "tuesday = ? AND "
            if (wednesday != -1) sql += "wednesday = ? AND"
            if (thursday != -1) sql += "thursday = ? AND"
            if (friday != -1) sql += "friday = ? AND"
            if (saturday != -1) sql += "saturday = ? AND"
            if (sunday != -1) sql += "sunday = ? AND"
            sql = sql.removeSuffix("AND")

            val result = connection.prepareStatement(sql)
                    .apply {
                        var c = 1 // Current parameter position
                        if (monday != -1) setInt(c++, monday)
                        if (tuesday != -1) setInt(c++, tuesday)
                        if (wednesday != -1) setInt(c++, wednesday)
                        if (thursday != -1) setInt(c++, thursday)
                        if (friday != -1) setInt(c++, friday)
                        if (saturday != -1) setInt(c++, saturday)
                        if (sunday != -1) setInt(c, sunday)
                    }
                    .executeQuery()

            return result.use {
                generateSequence {
                    if (it.next()) getCalendarFromResultSet(it) else null
                }.toList()
            }
        }

        override fun getAll(): List<Calendar> {
            val result = connection.prepareStatement("SELECT * FROM calendars").executeQuery()

            return result.use {
                generateSequence { if (it.next()) getCalendarFromResultSet(it) else null }.toList()
            }
        }

        override fun insert(vararg t: Calendar): Boolean {
            val statement = connection.prepareStatement("INSERT INTO calendars VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")

            return statement.transact {
                for (i in t) {
                    setString(1, i.serviceId)
                    setInt(2, i.monday)
                    setInt(3, i.tuesday)
                    setInt(4, i.wednesday)
                    setInt(5, i.thursday)
                    setInt(6, i.friday)
                    setInt(7, i.saturday)
                    setInt(8, i.sunday)
                    setString(9, i.startDate)
                    setString(10, i.endDate)

                    addBatch()
                }
            }
        }

        override fun update(vararg t: Calendar): Boolean {
            val statement = connection.prepareStatement("UPDATE calendars SET monday = ?, tuesday = ?, wednesday = ?, thursday = ?, friday = ?, friday = ?, saturday = ?, sunday = ?, startDate = ?, endDate = ? WHERE serviceId = ?")

            return statement.transact {
                for (i in t) {
                    setInt(1, i.monday)
                    setInt(2, i.tuesday)
                    setInt(3, i.wednesday)
                    setInt(4, i.thursday)
                    setInt(5, i.friday)
                    setInt(6, i.saturday)
                    setInt(7, i.sunday)
                    setString(8, i.startDate)
                    setString(9, i.endDate)
                    setString(10, i.serviceId)

                    addBatch()
                }
            }
        }

        override fun delete(vararg t: Calendar): Boolean {
            val statement = connection.prepareStatement("DELETE FROM calendars WHERE serviceId = ?")

            return statement.transact {
                for (i in t) {
                    setString(1, i.serviceId)
                    statement.addBatch()
                }
            }
        }

        private fun getCalendarFromResultSet(resultSet: ResultSet) = Calendar(
                serviceId = resultSet.getString("serviceId"),
                monday = resultSet.getInt("monday"),
                tuesday = resultSet.getInt("tuesday"),
                wednesday = resultSet.getInt("wednesday"),
                thursday = resultSet.getInt("thursday"),
                friday = resultSet.getInt("friday"),
                saturday = resultSet.getInt("saturday"),
                sunday = resultSet.getInt("sunday"),
                startDate = resultSet.getString("startDate"),
                endDate =resultSet.getString("endDate")
        )
    }

    override val calendarDates: CalendarDateDao = object : CalendarDateDao {
        override fun getByServiceId(serviceId: String): List<CalendarDate> {
            val result = connection.prepareStatement("SELECT * FROM calendarDates WHERE serviceId = ?")
                    .apply {
                        setString(1, serviceId)
                    }
                    .executeQuery()

            return result.use {
                generateSequence { if (it.next()) getCalendarDateFromResultSet(it) else null }.toList()
            }
        }

        override fun getByDate(date: String): List<CalendarDate> {
            val result = connection.prepareStatement("SELECT * FROM calendarDates WHERE date = ?")
                    .apply {
                        setString(1, date)
                    }
                    .executeQuery()

            return result.use {
                generateSequence { if (it.next()) getCalendarDateFromResultSet(it) else null }.toList()
            }
        }

        override fun getAll(): List<CalendarDate> {
            val result = connection.prepareStatement("SELECT * FROM calendarDates")
                    .executeQuery()

            return result.use {
                generateSequence { if (it.next()) getCalendarDateFromResultSet(it) else null }.toList()
            }
        }

        override fun insert(vararg t: CalendarDate): Boolean {
            val statement = connection.prepareStatement("INSERT INTO calendarDates VALUES (?, ?, ?)")

            return statement.transact {
                for (i in t) {
                    setString(1, i.serviceId)
                    setString(2, i.date)
                    setInt(3, i.exceptionType)

                    addBatch()
                }
            }
        }

        override fun update(vararg t: CalendarDate): Boolean {
            val statement = connection.prepareStatement("UPDATE calendarDates SET exceptionType = ? WHERE serviceId = ? AND date = ?")

            return statement.transact {
                for (i in t) {
                    setInt(1, i.exceptionType)
                    setString(2, i.serviceId)
                    setString(3, i.date)

                    addBatch()
                }
            }
        }

        override fun delete(vararg t: CalendarDate): Boolean {
            val statement = connection.prepareStatement("DELETE FROM calendarDates WHERE serviceId = ?")

            return statement.transact {
                for (i in t) {
                    setString(1, i.serviceId)
                    statement.addBatch()
                }
            }
        }

        private fun getCalendarDateFromResultSet(resultSet: ResultSet) = CalendarDate(
                serviceId = resultSet.getString("serviceId"),
                date = resultSet.getString("date"),
                exceptionType = resultSet.getInt("exceptionType")
        )
    }

    override val stopTimes: StopTimeDao = object : StopTimeDao {
        override fun getByTrip(trip: Trip): List<StopTime> {
            val result = connection.prepareStatement("SELECT * FROM stopTimes WHERE tripId = ?")
                    .apply {
                        setString(1, trip.tripId)
                    }
                    .executeQuery()

            return result.use {
                generateSequence { if (it.next()) getStopTimeFromResultSet(it) else null }.toList()
            }
        }

        override fun getByStop(stop: Stop): List<StopTime> {
            val result = connection.prepareStatement("SELECT * FROM stopTimes WHERE stopId = ?")
                    .apply {
                        setString(1, stop.id)
                    }
                    .executeQuery()

            return result.use {
                generateSequence { if (it.next()) getStopTimeFromResultSet(it) else null }.toList()
            }
        }

        override fun getAll(): List<StopTime> {
            val result = connection.prepareStatement("SELECT * FROM stopTimes")
                    .executeQuery()

            return result.use {
                generateSequence { if (it.next()) getStopTimeFromResultSet(it) else null }.toList()
            }
        }

        override fun insert(vararg t: StopTime): Boolean {
            val statement = connection.prepareStatement("INSERT INTO stopTimes VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")

            return statement.transact {
                for (i in t) {
                    setString(1, i.tripId)
                    setString(2, i.arrivalTime)
                    setString(3, i.departureTime)
                    setString(4, i.stopId)
                    setInt(5, i.stopSequence)
                    setString(6, i.stopHeadsign)
                    setObject(7, i.pickupType, Types.INTEGER)
                    setObject(8, i.dropOffType, Types.INTEGER)
                    setObject(9, i.shapeDistanceTraveled, Types.DOUBLE)
                    setObject(10, i.timepoint, Types.INTEGER)

                    addBatch()
                }
            }
        }

        override fun update(vararg t: StopTime): Boolean {
            val statement = connection.prepareStatement("UPDATE stopTimes SET arrivalTime = ?, departureTime = ?, stopSequence = ?, stopHeadsign = ?, pickupType = ?, dropOffType = ?, shapeDistanceTraveled = ?, timepoint = ? WHERE tripId = ? AND stopId = ?")

            return statement.transact {
                for (i in t) {
                    setString(1, i.arrivalTime)
                    setString(2, i.departureTime)
                    setInt(3, i.stopSequence)
                    setString(4, i.stopHeadsign)
                    setObject(5, i.pickupType, Types.INTEGER)
                    setObject(6, i.dropOffType, Types.INTEGER)
                    setObject(7, i.shapeDistanceTraveled, Types.DOUBLE)
                    setObject(8, i.timepoint, Types.INTEGER)
                    setString(9, i.tripId)
                    setString(10, i.stopId)

                    addBatch()
                }
            }
        }

        override fun delete(vararg t: StopTime): Boolean {
            val statement = connection.prepareStatement("DELETE FROM stopTimes WHERE tripId = ? AND stopId = ?")

            return statement.transact {
                for (i in t) {
                    setString(1, i.tripId)
                    setString(2, i.stopId)
                    statement.addBatch()
                }
            }
        }

        private fun getStopTimeFromResultSet(resultSet: ResultSet) = StopTime(
                tripId = resultSet.getString("tripId"),
                arrivalTime = resultSet.getString("arrivalTime"),
                departureTime = resultSet.getString("departureTime"),
                stopId = resultSet.getString("stopId"),
                stopSequence = resultSet.getInt("stopSequence"),
                stopHeadsign = resultSet.getString("stopHeadsign"),
                pickupType = resultSet.getInt("pickupType"),
                dropOffType = resultSet.getInt("dropOffType"),
                shapeDistanceTraveled = resultSet.getDouble("shapeDistanceTraveled"),
                timepoint = resultSet.getInt("timepoint")
        )
    }

    override val trips: TripDao = object : TripDao {
        override fun getByRoute(route: Route): List<Trip> {
            val result = connection.prepareStatement("SELECT * FROM trips WHERE routeId = ?")
                    .apply {
                        setString(1, route.id)
                    }
                    .executeQuery()

            return result.use {
                generateSequence { if (it.next()) getTripFromResultSet(it) else null }.toList()
            }
        }

        override fun getByRoute(route: Route, directionId: Int): List<Trip> {
            val result = connection.prepareStatement("SELECT * FROM trips WHERE routeId = ? AND directionId = ?")
                    .apply {
                        setString(1, route.id)
                        setInt(2, directionId)
                    }
                    .executeQuery()

            return result.use {
                generateSequence { if (it.next()) getTripFromResultSet(it) else null }.toList()
            }
        }

        override fun getTripById(id: String): Trip? {
            val result = connection.prepareStatement("SELECT * FROM trips WHERE tripId = ?")
                    .apply {
                        setString(1, id)
                    }
                    .executeQuery()

            return result.use {
                if (it.next()) getTripFromResultSet(it) else null
            }
        }

        override fun getByServiceId(serviceId: String): List<Trip> {
            val result = connection.prepareStatement("SELECT * FROM trips WHERE serviceId = ?")
                    .apply {
                        setString(1, serviceId)
                    }
                    .executeQuery()

            return result.use {
                generateSequence { if (it.next()) getTripFromResultSet(it) else null }.toList()
            }
        }

        override fun getAll(): List<Trip> {
            val result = connection.prepareStatement("SELECT * FROM trips")
                    .executeQuery()

            return result.use {
                generateSequence { if (it.next()) getTripFromResultSet(it) else null }.toList()
            }
        }

        override fun insert(vararg t: Trip): Boolean {
            val statement = connection.prepareStatement("INSERT INTO trips VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")

            return statement.transact {
                for (i in t) {
                    setString(1, i.routeId)
                    setString(2, i.serviceId)
                    setString(3, i.tripId)
                    setString(4, i.headsign)
                    setString(5, i.shortName)
                    setObject(6, i.directionId, Types.INTEGER)
                    setObject(7, i.blockId, Types.INTEGER)
                    setString(8, i.shapeId)
                    setObject(9, i.wheelchairAccessible, Types.INTEGER)
                    setObject(10, i.bikesAllowed, Types.INTEGER)

                    addBatch()
                }
            }
        }

        override fun update(vararg t: Trip): Boolean {
            val statement = connection.prepareStatement("UPDATE trips SET routeId = ?, serviceId = ?, headsign = ?, shortName = ?, directionId = ?, blockId = ?, shapeId = ?, wheelchairAccessible =  ?, bikesAllowed = ? WHERE tripId = ?")

            return statement.transact {
                for (i in t) {
                    setString(1, i.routeId)
                    setString(2, i.serviceId)
                    setString(3, i.headsign)
                    setString(4, i.shortName)
                    setObject(5, i.directionId, Types.INTEGER)
                    setObject(6, i.blockId, Types.INTEGER)
                    setString(7, i.shapeId)
                    setObject(8, i.wheelchairAccessible, Types.INTEGER)
                    setObject(9, i.bikesAllowed, Types.INTEGER)
                    setString(10, i.tripId)

                    addBatch()
                }
            }
        }

        override fun delete(vararg t: Trip): Boolean {
            val statement = connection.prepareStatement("DELETE FROM trips WHERE tripId = ?")

            return statement.transact {
                for (i in t) {
                    setString(1, i.tripId)
                    statement.addBatch()
                }
            }
        }

        private fun getTripFromResultSet(resultSet: ResultSet) = Trip(
                routeId = resultSet.getString("routeId"),
                serviceId = resultSet.getString("serviceId"),
                tripId = resultSet.getString("tripId"),
                headsign = resultSet.getString("headsign"),
                shortName = resultSet.getString("shortName"),
                directionId = resultSet.getInt("directionId"),
                blockId = resultSet.getString("blockId"),
                shapeId = resultSet.getString("shapeId"),
                wheelchairAccessible = resultSet.getInt("wheelchairAccessible"),
                bikesAllowed = resultSet.getInt("bikesAllowed")
        )
    }

    /**
     * Performs a specified set of database operations using a [PreparedStatement] as a transaction.
     * Auto-commit is disabled before a block of operations is run on the PreparedStatement.
     * All operations are committed and then auto-commit is reset to true.
     *
     * @param block Operations to perform on the PreparedStatement
     * @return Whether all of the operations completed successfully
     */
    private fun PreparedStatement.transact(block: PreparedStatement.() -> Unit): Boolean {
        // Do not auto-commit operations inside the block
        connection.autoCommit = false

        // Run the block (inserts, updates, etc)
        this.block()

        // Execute
        val result = this.use { it.executeBatch() }
        val success = !result.contains(PreparedStatement.EXECUTE_FAILED)

        if (success) { connection.commit() }

        connection.autoCommit = true
        return success
    }

}