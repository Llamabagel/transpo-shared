package ca.llamabagel.transpo.dao.impl

import ca.llamabagel.transpo.dao.gtfs.StopDao
import ca.llamabagel.transpo.models.gtfs.Stop
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Types

/**
 * GTFS data source that fetches values from a PostgreSQL database.
 *
 * @property connection The connection to the PostgreSQL database.
 */
class GtfsDatabase(private val connection: Connection) {

    val stops = object : StopDao {

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
            connection.autoCommit = false
            val statement = connection.prepareStatement("INSERT INTO stops VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")

            for (i in t) {
                statement.apply {
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
                }
                statement.addBatch()
            }

            val result = statement.use { it.execute() }
            if (result) {
                connection.commit()
            }

            connection.autoCommit = true
            return result
        }

        override fun update(vararg t: Stop): Boolean {
            connection.autoCommit = false
            val statement = connection.prepareStatement("UPDATE stops SET code = ?, name = ?, description = ?, latitude = ?, longitude = ?, zoneId = ?, stopUrl = ?, locationType = ?, parentStation = ?, timeZone = ?, wheelchairBoarding = ? WHERE id = ?")

            for (i in t) {
                statement.apply {
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
                }
                statement.addBatch()
            }

            val result = statement.use { it.execute() }
            if (result) {
                connection.commit()
            }

            connection.autoCommit = true
            return result
        }

        override fun delete(vararg t: Stop): Boolean {
            connection.autoCommit = false
            val statement = connection.prepareStatement("DELETE FROM stops WHERE id = ?")

            for (i in t) {
                statement.apply {
                    setString(1, i.id)
                }
                statement.addBatch()
            }

            val result = statement.use { it.execute() }
            if (result) {
                connection.commit()
            }

            connection.autoCommit = true
            return result
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

}