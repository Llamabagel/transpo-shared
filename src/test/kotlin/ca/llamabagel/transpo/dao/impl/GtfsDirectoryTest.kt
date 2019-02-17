/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.dao.impl

import ca.llamabagel.transpo.models.gtfs.Stop
import ca.llamabagel.transpo.models.gtfs.StopId
import org.apache.commons.io.FileUtils
import org.junit.*
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
        Assert.assertEquals(2, result.size)
        Assert.assertNotNull(result.find { stop -> stop.id == StopId("AF980") })
    }

    @Test
    fun testStopGetAll() {
        val source = GtfsDirectory(testFolder.root.toPath())

        val result = source.stops.getAll()
        Assert.assertEquals(5, result.size)
        Assert.assertNotNull(result.find { stop -> stop.id == StopId("AF980") })
    }

    /*@Test
    fun testStopInsert() {
        val source = GtfsDirectory(testFolder.root.toPath())

        val stop = Stop(StopId("BB200"), "1234", "INSERT / TEST", null, -46.0, 76.0, null, null, null, null, null, null)
        Assert.assertTrue(source.stops.insert(stop))
        Assert.assertEquals(stop, source.stops.getById(stop.id))
    }*/

}