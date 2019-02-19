/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.dao.impl

import ca.llamabagel.transpo.models.gtfs.Stop
import ca.llamabagel.transpo.models.gtfs.StopId
import junit.framework.Assert.*
import org.apache.commons.io.FileUtils
import org.junit.*
import org.junit.Assert.assertNotEquals
import org.junit.rules.TemporaryFolder
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

class GtfsDirectoryTest {

    @get:Rule
    val testFolder = TemporaryFolder()

    private val testData = Paths.get(ClassLoader.getSystemResource("gtfs").toURI())

    @Before
    fun setupData() {
        FileUtils.copyDirectory(testData.toFile(), testFolder.root)
    }

    @Test
    fun testStopGetById() {
        val source = GtfsDirectory(testFolder.root.toPath())

        val stop = source.stops.getById(StopId("AF970"))
        Assert.assertTrue(stop != null)
        Assert.assertEquals(StopId("AF970").value, stop?.id)
        Assert.assertEquals("3030", stop?.code)

        Assert.assertNull(source.stops.getById(StopId("SomeNonIdValue")))
    }

    @Test
    fun testStopGetByCode() {
        val source = GtfsDirectory(testFolder.root.toPath())

        val result = source.stops.getByCode("3031")
        assertEquals(2, result.size)
        assertNotNull(result.find { stop -> stop.id == StopId("AF980") })
    }

    @Test
    fun testStopGetAll() {
        val source = GtfsDirectory(testFolder.root.toPath())

        val result = source.stops.getAll()
        assertEquals(5, result.size)
        assertNotNull(result.find { stop -> stop.id == StopId("AF980") })
    }

    @Test
    fun testStopInsert() {
        val source = GtfsDirectory(testFolder.root.toPath())

        val stop = Stop(StopId("BB200"), "1234", "INSERT / TEST", null, -46.0, 76.0, null, null, null, null, null, null)
        assertTrue(source.stops.insert(stop))
        assertEquals(stop, source.stops.getById(stop.id))

        val existing = Stop(StopId("AF980"), null, "", null, -46.0, 76.0, null, null, null, null, null, null)
        assertFalse(source.stops.insert(existing))
    }

    @Test
    fun testStopUpdate() {
        val source = GtfsDirectory(testFolder.root.toPath())

        val stop = Stop(StopId("BB200"), "1234", "INSERT / TEST", null, -46.0, 76.0, null, null, null, null, null, null)
        source.stops.insert(stop)

        // Update each value except the id. parentStation, timeZone, and wheelchairBoarding are not supported for CSV updates.
        val newStop = Stop(StopId("BB200"), "1235", "UPDATE / TEST", "A description", -47.0, 78.0, 0, "url", 7, null, null, null)
        assertTrue(source.stops.update(newStop))
        assertNotEquals(stop, source.stops.getById(newStop.id))
        assertEquals(newStop, source.stops.getById(newStop.id))

        val notAStop = Stop(StopId("NOO"), null, "", null, -47.0, 48.0, null, null, null, null, null, null)
        assertFalse(source.stops.update(notAStop))
    }

}