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
        override fun getByServiceId(serviceId: String): Calendar? {
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
        override fun getByServiceId(serviceId: String): List<CalendarDate> {
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
        override fun getByTrip(trip: Trip): List<StopTime> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun getByStop(stop: Stop): List<StopTime> {
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
        override fun getByRoute(route: Route): List<Trip> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun getByRoute(route: Route, directionId: Int): List<Trip> {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun getTripById(id: String): Trip? {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun getByServiceId(serviceId: String): List<Trip> {
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