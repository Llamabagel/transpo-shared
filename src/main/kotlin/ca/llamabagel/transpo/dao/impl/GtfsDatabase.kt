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
        override fun getById(id: StopId): Stop? {
            val result = connection.prepareStatement("SELECT * FROM stops WHERE id = ?")
                    .apply {
                        setString(1, id.value)
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
                    setString(1, i.id.value)
                    setString(2, i.code)
                    setString(3, i.name)
                    setString(4, i.description)
                    setDouble(5, i.latitude)
                    setDouble(6, i.longitude)
                    setObject(7, i.zoneId, Types.INTEGER)
                    setString(8, i.stopUrl)
                    setObject(9, i.locationType, Types.INTEGER)
                    setString(10, i.parentStation?.value)
                    setString(11, i.timeZone)
                    setObject(12, i.wheelchairBoarding, Types.INTEGER)

                    addBatch()
                }
            }
        }

        override fun update(vararg t: Stop): Boolean {
            val statement = connection.prepareStatement("UPDATE stops SET code = ?, name = ?, description = ?, latitude = ?, longitude = ?, zone_id = ?, stop_url = ?, location_type = ?, parent_station = ?, time_zone = ?, wheelchair_boarding = ? WHERE id = ?")

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
                    setString(9, i.parentStation?.value)
                    setString(10, i.timeZone)
                    setObject(11, i.wheelchairBoarding, Types.INTEGER)
                    setString(12, i.id.value)

                    addBatch()
                }
            }
        }

        override fun delete(vararg t: Stop): Boolean {
            val statement = connection.prepareStatement("DELETE FROM stops WHERE id = ?")

            return statement.transact {
                for (i in t) {
                    setString(1, i.id.value)
                    statement.addBatch()
                }
            }
        }


        private fun getStopFromResultSet(resultSet: ResultSet): Stop {
            // Handle nullable Integers
            var zoneId: Int? = resultSet.getInt("zone_id")
            if (resultSet.wasNull()) zoneId = null

            var locationType: Int? = resultSet.getInt("location_type")
            if (resultSet.wasNull()) locationType = null

            var wheelchairBoarding: Int? = resultSet.getInt("wheelchair_boarding")
            if (resultSet.wasNull()) wheelchairBoarding = null

            return Stop(
                    id = StopId(resultSet.getString("id")),
                    code = resultSet.getString("code"),
                    name = resultSet.getString("name"),
                    description = resultSet.getString("description"),
                    latitude = resultSet.getDouble("latitude"),
                    longitude = resultSet.getDouble("longitude"),
                    zoneId = zoneId,
                    stopUrl = resultSet.getString("stop_url"),
                    locationType = locationType,
                    parentStation = resultSet.getString("parent_station")?.asStopId(),
                    timeZone = resultSet.getString("time_zone"),
                    wheelchairBoarding = wheelchairBoarding
            )
        }
    }

    override val routes = object : RouteDao {
        override fun getByNumber(number: String): Route? {
            val result = connection.prepareStatement("SELECT * FROM routes WHERE short_name = ?")
                    .apply {
                        setString(1, number)
                    }
                    .executeQuery()

            return result.use {
                if (it.next()) getRouteFromResultSet(it) else null
            }
        }

        override fun getById(id: RouteId): Route? {
            val result = connection.prepareStatement("SELECT * FROM routes WHERE id = ?")
                    .apply {
                        setString(1, id.value)
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
                    setString(1, i.id.value)
                    setString(2, i.agencyId?.value)
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
            val statement = connection.prepareStatement("UPDATE routes SET agency_id = ?, short_name = ?, long_name = ?, description = ?, type = ?, url = ?, color = ?, text_color = ?, sort_order = ? WHERE id = ?")

            return statement.transact {
                for (i in t) {
                    setString(1, i.agencyId?.value)
                    setString(2, i.shortName)
                    setString(3, i.longName)
                    setString(4, i.description)
                    setInt(5, i.type)
                    setString(6, i.url)
                    setString(7, i.color)
                    setString(8, i.textColor)
                    setObject(9, i.sortOrder, Types.INTEGER)
                    setString(10, i.id.value)
                    addBatch()
                }
            }
        }

        override fun delete(vararg t: Route): Boolean {
            val statement = connection.prepareStatement("DELETE FROM routes WHERE id = ?")

            return statement.transact {
                for (i in t) {
                    setString(1, i.id.value)
                    statement.addBatch()
                }
            }
        }

        private fun getRouteFromResultSet(resultSet: ResultSet): Route {
            var sortOrder: Int? = resultSet.getInt("sort_order")
            if (resultSet.wasNull()) sortOrder = null

            return Route(
                    id = resultSet.getString("id").asRouteId()!!,
                    agencyId = resultSet.getString("agency_id").asAgencyId(),
                    shortName = resultSet.getString("short_name"),
                    longName = resultSet.getString("long_name"),
                    description = resultSet.getString("description"),
                    type = resultSet.getInt("type"),
                    url = resultSet.getString("url"),
                    color = resultSet.getString("color"),
                    textColor = resultSet.getString("text_color"),
                    sortOrder = sortOrder
            )
        }
    }

    override val agencies: AgencyDao = object : AgencyDao {
        override fun getById(id: AgencyId): Agency? {
            val result = connection.prepareStatement("SELECT * FROM agencies WHERE id = ?")
                    .apply {
                        setString(1, id.value)
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
                    setString(1, i.id.value)
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
            val statement = connection.prepareStatement("UPDATE agencies SET name = ?, url = ?, time_zone = ?, language = ?, phone = ?, fare_url = ?, email = ? WHERE id = ?")

            return statement.transact {
                for (i in t) {
                    setString(1, i.name)
                    setString(2, i.url)
                    setString(3, i.timeZone)
                    setString(4, i.language)
                    setString(5, i.phone)
                    setString(6, i.fareUrl)
                    setString(7, i.email)
                    setString(8, i.id.value)
                    addBatch()
                }
            }
        }

        override fun delete(vararg t: Agency): Boolean {
            val statement = connection.prepareStatement("DELETE FROM agencies WHERE id = ?")

            return statement.transact {
                for (i in t) {
                    setString(1, i.id.value)
                    statement.addBatch()
                }
            }
        }

        private fun getAgencyFromResultSet(resultSet: ResultSet) = Agency(
                id = resultSet.getString("id").asAgencyId()!!,
                url = resultSet.getString("url"),
                name = resultSet.getString("name"),
                timeZone = resultSet.getString("time_zone"),
                language = resultSet.getString("language"),
                phone = resultSet.getString("phone"),
                fareUrl = resultSet.getString("fare_url"),
                email = resultSet.getString("email")
        )
    }

    override val calendars: CalendarDao = object : CalendarDao {
        override fun getByServiceId(serviceId: CalendarServiceId): Calendar? {
            val result = connection.prepareStatement("SELECT * FROM calendars WHERE service_id = ?")
                    .apply {
                        setString(1, serviceId.value)
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
                    setString(1, i.serviceId.value)
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
            val statement = connection.prepareStatement("UPDATE calendars SET monday = ?, tuesday = ?, wednesday = ?, thursday = ?, friday = ?, saturday = ?, sunday = ?, start_date = ?, end_date = ? WHERE service_id = ?")

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
                    setString(10, i.serviceId.value)

                    addBatch()
                }
            }
        }

        override fun delete(vararg t: Calendar): Boolean {
            val statement = connection.prepareStatement("DELETE FROM calendars WHERE service_id = ?")

            return statement.transact {
                for (i in t) {
                    setString(1, i.serviceId.value)
                    statement.addBatch()
                }
            }
        }

        private fun getCalendarFromResultSet(resultSet: ResultSet) = Calendar(
                serviceId = resultSet.getString("service_id").asCalendarServiceId()!!,
                monday = resultSet.getInt("monday"),
                tuesday = resultSet.getInt("tuesday"),
                wednesday = resultSet.getInt("wednesday"),
                thursday = resultSet.getInt("thursday"),
                friday = resultSet.getInt("friday"),
                saturday = resultSet.getInt("saturday"),
                sunday = resultSet.getInt("sunday"),
                startDate = resultSet.getString("start_date"),
                endDate =resultSet.getString("end_date")
        )
    }

    override val calendarDates: CalendarDateDao = object : CalendarDateDao {
        override fun getByServiceId(serviceId: CalendarServiceId): List<CalendarDate> {
            val result = connection.prepareStatement("SELECT * FROM calendar_dates WHERE service_id = ?")
                    .apply {
                        setString(1, serviceId.value)
                    }
                    .executeQuery()

            return result.use {
                generateSequence { if (it.next()) getCalendarDateFromResultSet(it) else null }.toList()
            }
        }

        override fun getByDate(date: String): List<CalendarDate> {
            val result = connection.prepareStatement("SELECT * FROM calendar_dates WHERE date = ?")
                    .apply {
                        setString(1, date)
                    }
                    .executeQuery()

            return result.use {
                generateSequence { if (it.next()) getCalendarDateFromResultSet(it) else null }.toList()
            }
        }

        override fun getAll(): List<CalendarDate> {
            val result = connection.prepareStatement("SELECT * FROM calendar_dates")
                    .executeQuery()

            return result.use {
                generateSequence { if (it.next()) getCalendarDateFromResultSet(it) else null }.toList()
            }
        }

        override fun insert(vararg t: CalendarDate): Boolean {
            val statement = connection.prepareStatement("INSERT INTO calendar_dates VALUES (?, ?, ?)")

            return statement.transact {
                for (i in t) {
                    setString(1, i.serviceId.value)
                    setString(2, i.date)
                    setInt(3, i.exceptionType)

                    addBatch()
                }
            }
        }

        override fun delete(vararg t: CalendarDate): Boolean {
            val statement = connection.prepareStatement("DELETE FROM calendar_dates WHERE service_id = ?")

            return statement.transact {
                for (i in t) {
                    setString(1, i.serviceId.value)
                    statement.addBatch()
                }
            }
        }

        private fun getCalendarDateFromResultSet(resultSet: ResultSet) = CalendarDate(
                serviceId = resultSet.getString("service_id").asCalendarServiceId()!!,
                date = resultSet.getString("date"),
                exceptionType = resultSet.getInt("exception_type")
        )
    }

    override val stopTimes: StopTimeDao = object : StopTimeDao {
        override fun getByTripId(tripId: TripId): List<StopTime> {
            val result = connection.prepareStatement("SELECT * FROM stop_times WHERE trip_id = ?")
                    .apply {
                        setString(1, tripId.value)
                    }
                    .executeQuery()

            return result.use {
                generateSequence { if (it.next()) getStopTimeFromResultSet(it) else null }.toList()
            }
        }

        override fun getByStopId(stopId: StopId): List<StopTime> {
            val result = connection.prepareStatement("SELECT * FROM stop_times WHERE stop_id = ?")
                    .apply {
                        setString(1, stopId.value)
                    }
                    .executeQuery()

            return result.use {
                generateSequence { if (it.next()) getStopTimeFromResultSet(it) else null }.toList()
            }
        }

        override fun getAll(): List<StopTime> {
            val result = connection.prepareStatement("SELECT * FROM stop_times")
                    .executeQuery()

            return result.use {
                generateSequence { if (it.next()) getStopTimeFromResultSet(it) else null }.toList()
            }
        }

        override fun insert(vararg t: StopTime): Boolean {
            val statement = connection.prepareStatement("INSERT INTO stop_times VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")

            return statement.transact {
                for (i in t) {
                    setString(1, i.tripId.value)
                    setString(2, i.arrivalTime)
                    setString(3, i.departureTime)
                    setString(4, i.stopId.value)
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

        override fun delete(vararg t: StopTime): Boolean {
            val statement = connection.prepareStatement("DELETE FROM stop_times WHERE trip_id = ? AND stop_id = ?")

            return statement.transact {
                for (i in t) {
                    setString(1, i.tripId.value)
                    setString(2, i.stopId.value)
                    statement.addBatch()
                }
            }
        }

        private fun getStopTimeFromResultSet(resultSet: ResultSet): StopTime {
            var pickupType: Int? = resultSet.getInt("pickup_type")
            if (resultSet.wasNull()) pickupType = null

            var dropOffType: Int? = resultSet.getInt("drop_off_type")
            if (resultSet.wasNull()) dropOffType = null

            var shapeDistanceTraveled: Double? = resultSet.getDouble("shape_distance_traveled")
            if (resultSet.wasNull()) shapeDistanceTraveled = null

            var timepoint: Int? = resultSet.getInt("timepoint")
            if (resultSet.wasNull()) timepoint = null

            return StopTime(
                    tripId = resultSet.getString("trip_id").asTripId()!!,
                    arrivalTime = resultSet.getString("arrival_time"),
                    departureTime = resultSet.getString("departure_time"),
                    stopId = resultSet.getString("stop_id").asStopId()!!,
                    stopSequence = resultSet.getInt("stop_sequence"),
                    stopHeadsign = resultSet.getString("stop_headsign"),
                    pickupType = pickupType,
                    dropOffType = dropOffType,
                    shapeDistanceTraveled = shapeDistanceTraveled,
                    timepoint = timepoint
            )
        }
    }

    override val trips: TripDao = object : TripDao {
        override fun getByRouteId(routeId: RouteId): List<Trip> {
            val result = connection.prepareStatement("SELECT * FROM trips WHERE route_id = ?")
                    .apply {
                        setString(1, routeId.value)
                    }
                    .executeQuery()

            return result.use {
                generateSequence { if (it.next()) getTripFromResultSet(it) else null }.toList()
            }
        }

        override fun getByRouteId(routeId: RouteId, directionId: Int): List<Trip> {
            val result = connection.prepareStatement("SELECT * FROM trips WHERE route_id = ? AND direction_id = ?")
                    .apply {
                        setString(1, routeId.value)
                        setInt(2, directionId)
                    }
                    .executeQuery()

            return result.use {
                generateSequence { if (it.next()) getTripFromResultSet(it) else null }.toList()
            }
        }

        override fun getByTripId(id: TripId): Trip? {
            val result = connection.prepareStatement("SELECT * FROM trips WHERE trip_id = ?")
                    .apply {
                        setString(1, id.value)
                    }
                    .executeQuery()

            return result.use {
                if (it.next()) getTripFromResultSet(it) else null
            }
        }

        override fun getByServiceId(serviceId: CalendarServiceId): List<Trip> {
            val result = connection.prepareStatement("SELECT * FROM trips WHERE service_id = ?")
                    .apply {
                        setString(1, serviceId.value)
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
                    setString(1, i.routeId.value)
                    setString(2, i.serviceId.value)
                    setString(3, i.tripId.value)
                    setString(4, i.headsign)
                    setString(5, i.shortName)
                    setObject(6, i.directionId, Types.INTEGER)
                    setObject(7, i.blockId, Types.INTEGER)
                    setString(8, i.shapeId?.value)
                    setObject(9, i.wheelchairAccessible, Types.INTEGER)
                    setObject(10, i.bikesAllowed, Types.INTEGER)

                    addBatch()
                }
            }
        }

        override fun update(vararg t: Trip): Boolean {
            val statement = connection.prepareStatement("UPDATE trips SET route_id = ?, service_id = ?, headsign = ?, short_name = ?, direction_id = ?, block_id = ?, shape_id = ?, wheelchair_accessible =  ?, bikes_allowed = ? WHERE trip_id = ?")

            return statement.transact {
                for (i in t) {
                    setString(1, i.routeId.value)
                    setString(2, i.serviceId.value)
                    setString(3, i.headsign)
                    setString(4, i.shortName)
                    setObject(5, i.directionId, Types.INTEGER)
                    setObject(6, i.blockId, Types.INTEGER)
                    setString(7, i.shapeId?.value)
                    setObject(8, i.wheelchairAccessible, Types.INTEGER)
                    setObject(9, i.bikesAllowed, Types.INTEGER)
                    setString(10, i.tripId.value)

                    addBatch()
                }
            }
        }

        override fun delete(vararg t: Trip): Boolean {
            val statement = connection.prepareStatement("DELETE FROM trips WHERE trip_id = ?")

            return statement.transact {
                for (i in t) {
                    setString(1, i.tripId.value)
                    statement.addBatch()
                }
            }
        }

        private fun getTripFromResultSet(resultSet: ResultSet): Trip {
            var directionId: Int? = resultSet.getInt("direction_id")
            if (resultSet.wasNull()) directionId = null

            var wheelchairAccessible: Int? = resultSet.getInt("wheelchair_accessible")
            if (resultSet.wasNull()) wheelchairAccessible = null

            var bikesAllowed: Int? = resultSet.getInt("bikes_allowed")
            if (resultSet.wasNull()) bikesAllowed = null

            val shapeId = resultSet.getString("shape_id")

            return Trip(
                    routeId = resultSet.getString("route_id").asRouteId()!!,
                    serviceId = resultSet.getString("service_id").asCalendarServiceId()!!,
                    tripId = resultSet.getString("trip_id").asTripId()!!,
                    headsign = resultSet.getString("headsign"),
                    shortName = resultSet.getString("short_name"),
                    directionId = directionId,
                    blockId = resultSet.getString("block_id"),
                    shapeId = if (shapeId != null) ShapeId(shapeId) else null,
                    wheelchairAccessible = wheelchairAccessible,
                    bikesAllowed = bikesAllowed
            )
        }
    }

    override val shapes: ShapeDao? = object : ShapeDao {
        override fun getById(id: ShapeId): List<Shape> {
            val result = connection.prepareStatement("SELECT * FROM shapes WHERE id = ?")
                    .apply {
                        setString(1, id.value)
                    }
                    .executeQuery()

            return result.use {
                generateSequence { if (it.next()) getShapeFromResultSet(it) else null }.toList()
            }
        }

        override fun getAll(): List<Shape> {
            val result = connection.prepareStatement("SELECT * FROM shapes")
                    .executeQuery()

            return result.use {
                generateSequence { if (it.next()) getShapeFromResultSet(it) else null }.toList()
            }
        }

        override fun insert(vararg t: Shape): Boolean {
            val statement = connection.prepareStatement("INSERT INTO shapes VALUES (?, ?, ?, ?, ?)")

            return statement.transact {
                for (i in t) {
                    setString(1, i.id.value)
                    setDouble(2, i.latitude)
                    setDouble(3, i.longitude)
                    setInt(4, i.sequence)
                    setObject(5, i.distanceTraveled, Types.DOUBLE)
                    addBatch()
                }
            }
        }

        override fun delete(vararg t: Shape): Boolean {
            val statement = connection.prepareStatement("DELETE FROM shapes WHERE id = ? AND sequence = ?")

            return statement.transact {
                for (i in t) {
                    setString(1, i.id.value)
                    setInt(2, i.sequence)
                    statement.addBatch()
                }
            }
        }

        private fun getShapeFromResultSet(resultSet: ResultSet): Shape {
            var distanceTraveled: Double? = resultSet.getDouble("distance_traveled")
            if (resultSet.wasNull()) distanceTraveled = null

            return Shape(
                    id = ShapeId(resultSet.getString("id")),
                    latitude = resultSet.getDouble("latitude"),
                    longitude = resultSet.getDouble("longitude"),
                    sequence = resultSet.getInt("sequence"),
                    distanceTraveled = distanceTraveled
            )
        }
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