/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.dao.impl

import ca.llamabagel.transpo.models.gtfs.Stop
import com.opentable.db.postgres.embedded.FlywayPreparer
import com.opentable.db.postgres.junit.EmbeddedPostgresRules
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class GtfsDatabaseTest {

    @get:Rule
    val postgres = EmbeddedPostgresRules.preparedDatabase(FlywayPreparer.forClasspathLocation("db/testing"))

    @Test
    fun testStopGetById() {
        val source = GtfsDatabase(postgres.testDatabase.connection)

        val stop = source.stops.getById("AF970")
        assertTrue(stop != null)
        assertTrue(stop?.id == "AF970")
        assertTrue(stop?.code == "3030")

        assertNull(source.stops.getById("SomeNonIdValue"))
    }

    @Test
    fun testStopGetByCode() {
        val source = GtfsDatabase(postgres.testDatabase.connection)

        val result = source.stops.getByCode("3031")
        assertTrue(result.size == 2)
        assertNotNull(result.find { stop -> stop.id == "AF980" })
    }

    @Test
    fun testStopGetAll() {
        val source = GtfsDatabase(postgres.testDatabase.connection)

        val result = source.stops.getAll()
        assertTrue(result.size == 5)
        assertNotNull(result.find { stop -> stop.id == "AF980" })
    }

    @Test
    fun testStopInsert() {
        val source = GtfsDatabase(postgres.testDatabase.connection)

        val stop = Stop("BB200", "1234", "INSERT / TEST", null, -46.0, 76.0, null, null, null, null, null, null)
        assertTrue(source.stops.insert(stop))
        assertTrue(source.stops.getById(stop.id) == stop)
    }

    @Test
    fun testStopUpdate() {
        val source = GtfsDatabase(postgres.testDatabase.connection)

        val stop = Stop("BB200", "1234", "INSERT / TEST", null, -46.0, 76.0, null, null, null, null, null, null)
        source.stops.insert(stop)

        // Update each value except the id.
        val newStop = Stop("BB200", "1235", "UPDATE / TEST", "A description", -47.0, 78.0, 0, "url", 7, "AA100", "EST", 1)
        source.stops.update(newStop)
        assertFalse(source.stops.getById(newStop.id) == stop)
        assertTrue(source.stops.getById(newStop.id) == newStop)
    }

    @Test
    fun testStopDelete() {
        val source = GtfsDatabase(postgres.testDatabase.connection)

        val stop = Stop("BB200", "1234", "INSERT / TEST", null, -46.0, 76.0, null, null, null, null, null, null)
        source.stops.insert(stop)
        assertTrue(source.stops.delete(stop))
        assertNull(source.stops.getById(stop.id))
    }

}