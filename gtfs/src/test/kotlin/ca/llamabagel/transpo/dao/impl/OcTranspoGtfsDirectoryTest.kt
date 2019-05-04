/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.dao.impl

import ca.llamabagel.transpo.models.gtfs.*
import org.apache.commons.io.FileUtils
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import java.nio.file.Paths

class OcTranspoGtfsDirectoryTest {

    @get:Rule
    val testFolder = TemporaryFolder()

    private val testData = Paths.get(ClassLoader.getSystemResource("oc").toURI())

    @Before
    fun setupData() {
        FileUtils.copyDirectory(testData.toFile(), testFolder.root)
    }

    @Test
    fun testStopGetById() {
        val source = OcTranspoGtfsDirectory(testFolder.root.toPath())

        val stop = source.stops.getById(StopId("AA010"))
        Assert.assertTrue(stop != null)
        Assert.assertEquals(StopId("AA010").value, stop?.id)
        Assert.assertEquals("8767", stop?.code)

        Assert.assertNull(source.stops.getById(StopId("SomeNonIdValue")))
    }

    @Test
    fun testStopGetByCode() {
        val source = OcTranspoGtfsDirectory(testFolder.root.toPath())

        val result = source.stops.getByCode("7001")
        assertEquals(1, result.size)
        assertNotNull(result.find { stop -> stop.id == StopId("AA030") })
    }

    @Test
    fun testStopGetAll() {
        val source = OcTranspoGtfsDirectory(testFolder.root.toPath())

        val result = source.stops.getAll()
        assertEquals(22, result.size)
        assertNotNull(result.find { stop -> stop.id == StopId("AA010") })
    }

    @Test
    fun testStopInsert() {
        val source = OcTranspoGtfsDirectory(testFolder.root.toPath())

        val stop = Stop(StopId("BB200"), "1234", "INSERT / TEST", null, -46.0, 76.0, null, null, null, null, null, null)
        assertTrue(source.stops.insert(stop))
        assertEquals(stop, source.stops.getById(stop.id))

        val existing = Stop(StopId("AA010"), null, "", null, -46.0, 76.0, null, null, null, null, null, null)
        assertFalse(source.stops.insert(existing))
    }

    @Test
    fun testStopUpdate() {
        val source = OcTranspoGtfsDirectory(testFolder.root.toPath())

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

    @Test
    fun testStopDelete() {
        val source = OcTranspoGtfsDirectory(testFolder.root.toPath())

        val stop = Stop(StopId("BB200"), "1234", "INSERT / TEST", null, -46.0, 76.0, null, null, null, null, null, null)
        source.stops.insert(stop)
        assertTrue(source.stops.delete(stop))
        assertNull(source.stops.getById(stop.id))
    }

    // GtfsDatabase.routes tests
    @Test
    fun testRouteGetByNumber() {
        val source = OcTranspoGtfsDirectory(testFolder.root.toPath())

        val route = source.routes.getByNumber("44")
        assertNotNull(route)
        assertTrue(route?.shortName == "44")
    }

    @Test
    fun testRouteGetById() {
        val source = OcTranspoGtfsDirectory(testFolder.root.toPath())

        val route = source.routes.getById(RouteId("5-288"))
        assertNotNull(route)
        assertTrue(route?.id == RouteId("5-288"))
    }

    @Test
    fun testRouteGetAll() {
        val source = OcTranspoGtfsDirectory(testFolder.root.toPath())

        val routes = source.routes.getAll()
        assertEquals(183, routes.size)
        assertNotNull(routes.find { route -> route.id == RouteId("5-288") })
    }

    @Test
    fun testRouteInsert() {
        val source = OcTranspoGtfsDirectory(testFolder.root.toPath())

        val route = Route(RouteId("1-1"), null, "1", "", null, 3, null, null, null, null)
        assertTrue(source.routes.insert(route))
        assertTrue(source.routes.getById(route.id) == route)
    }

    @Test
    fun testRouteUpdate() {
        val source = OcTranspoGtfsDirectory(testFolder.root.toPath())

        val route = Route(RouteId("1-1"), null, "1", "", null, 3, null, null, null, null)
        source.routes.insert(route)

        // Update each value except the id.
        val newRoute = Route(RouteId("1-1"), null, "1!", "Somewhere", "A Route", 2, null, null, null, null)
        source.routes.update(newRoute)
        assertNotEquals(route, source.routes.getById(newRoute.id))
        assertEquals(newRoute, source.routes.getById(newRoute.id))
    }

    @Test
    fun testRouteDelete() {
        val source = OcTranspoGtfsDirectory(testFolder.root.toPath())

        val route = Route(RouteId("1-1"), null, "1", "", null, 3, null, null, null, null)
        source.routes.insert(route)
        assertTrue(source.routes.delete(route))
        assertNull(source.routes.getById(route.id))
    }

    @Test
    fun testAgencyGetAll() {
        val source = OcTranspoGtfsDirectory(testFolder.root.toPath())

        val agencies = source.agencies.getAll()
        assertTrue(agencies.size == 1)
        assertNotNull(agencies.find { agency -> agency.name == "OC Transpo" })
    }

    // Calendar tests
    @Test
    fun testCalendarGetByServiceId() {
        val source = OcTranspoGtfsDirectory(testFolder.root.toPath())

        val calendar = source.calendars.getByServiceId(CalendarServiceId("JAN19-JANDA19-Weekday-26"))
        assertNotNull(calendar)
        assertTrue(calendar?.serviceId == CalendarServiceId("JAN19-JANDA19-Weekday-26"))
    }

    @Test
    fun testCalendarGetByDays() {
        val source = OcTranspoGtfsDirectory(testFolder.root.toPath())

        val weekdays = source.calendars.getByDays(monday = 1, friday = 1)
        assertEquals(11, weekdays.size)
        assertNotNull(weekdays.find { calendar -> calendar.serviceId == CalendarServiceId("JAN19-301Shop-Weekday-01") })

        val sundays = source.calendars.getByDays(monday = 0, sunday = 1)
        assertEquals(3, sundays.size)
        assertNotNull(sundays.find { calendar -> calendar.serviceId == CalendarServiceId("JAN19-WOTRSU-Sunday-01") })

        val none = source.calendars.getByDays(monday = 1, sunday = 1)
        assertTrue(none.isEmpty())

        assertTrue(source.calendars.getByDays().isEmpty())
    }

    @Test
    fun testCalendarGetAll() {
        val source = OcTranspoGtfsDirectory(testFolder.root.toPath())

        val calendars = source.calendars.getAll()

        assertEquals(17, calendars.size)
        assertNotNull(calendars.find { calendar -> calendar.serviceId == CalendarServiceId("JAN19-d1930Sup-Weekday-01") })
    }

    @Test
    fun testCalendarInsert() {
        val source = OcTranspoGtfsDirectory(testFolder.root.toPath())

        val calendar = Calendar(CalendarServiceId("SERVICE"), 1, 0, 1, 0, 1, 0, 1, "Today", "Tomorrow")
        assertTrue(source.calendars.insert(calendar))
        assertEquals(calendar, source.calendars.getByServiceId(calendar.serviceId))
    }

    @Test
    fun testCalendarUpdate() {
        val source = OcTranspoGtfsDirectory(testFolder.root.toPath())

        val calendar = Calendar(CalendarServiceId("SERVICE"), 1, 0, 1, 0, 1, 0, 1, "Today", "Tomorrow")
        source.calendars.insert(calendar)

        val newCalendar = Calendar(CalendarServiceId("SERVICE"), 0, 1, 0, 1, 0, 1, 0, "Yesterday", "Today")
        assertTrue(source.calendars.update(newCalendar))
        assertNotEquals(calendar, source.calendars.getByServiceId(calendar.serviceId))
        assertEquals(newCalendar, source.calendars.getByServiceId(calendar.serviceId))
    }

    @Test
    fun testCalendarDelete() {
        val source = OcTranspoGtfsDirectory(testFolder.root.toPath())

        val calendar = Calendar(CalendarServiceId("SERVICE"), 1, 0, 1, 0, 1, 0, 1, "Today", "Tomorrow")
        source.calendars.insert(calendar)

        assertTrue(source.calendars.delete(calendar))
        assertNull(source.calendars.getByServiceId(calendar.serviceId))
    }

    // CalendarDate tests
    @Test
    fun testCalendarDateGetByServiceId() {
        val source = OcTranspoGtfsDirectory(testFolder.root.toPath())
        val calendarDates = source.calendarDates.getByServiceId(CalendarServiceId("JAN19-d1930LoR-Weekday-01"))

        assertTrue(calendarDates.size == 1)
        assertNotNull(calendarDates.find { calendarDate -> calendarDate.serviceId == CalendarServiceId("JAN19-d1930LoR-Weekday-01") })
    }

    @Test
    fun testCalendarDateGetByDate() {
        val source = OcTranspoGtfsDirectory(testFolder.root.toPath())
        val calendarDates = source.calendarDates.getByDate("20190218")

        assertTrue(calendarDates.size == 1)
        assertNotNull(calendarDates.find { calendarDate -> calendarDate.date == "20190218"})
    }

    @Test
    fun testCalendarDateGetAll() {
        val source = OcTranspoGtfsDirectory(testFolder.root.toPath())
        val calendarDates = source.calendarDates.getAll()

        assertEquals(27, calendarDates.size)
        assertNotNull(calendarDates.find { calendarDate -> calendarDate.date == "20190218"})
    }

    @Test
    fun testCalendarDateInsert() {
        val source = OcTranspoGtfsDirectory(testFolder.root.toPath())
        val calendarDate = CalendarDate(CalendarServiceId("NewException"), "Today", 1)

        assertTrue(source.calendarDates.insert(calendarDate))
        assertTrue(source.calendarDates.getByServiceId(calendarDate.serviceId).contains(calendarDate))
    }

    @Test
    fun testCalendarDateDelete() {
        val source = OcTranspoGtfsDirectory(testFolder.root.toPath())
        val calendarDate = CalendarDate(CalendarServiceId("NewException"), "Today", 1)

        source.calendarDates.insert(calendarDate)
        assertTrue(source.calendarDates.delete(calendarDate))
        assertTrue(source.calendarDates.getByServiceId(calendarDate.serviceId).isEmpty())
    }

    // StopTime tests
    @Test
    fun testStopTimeGetByTripId() {
        val source = OcTranspoGtfsDirectory(testFolder.root.toPath())

        val stopTimes = source.stopTimes.getByTripId("57329111-JAN19-JANDA19-Weekday-26".asTripId()!!)
        assertEquals(65, stopTimes.size)
        assertNotNull(stopTimes.find { stopTime -> stopTime.stopId.value == "CE975" })
    }

    @Test
    fun testStopTimeGetByStopId() {
        val source = OcTranspoGtfsDirectory(testFolder.root.toPath())

        val stopTimes = source.stopTimes.getByStopId("CJ110".asStopId()!!)
        assertTrue(stopTimes.size == 1)
        assertNotNull(stopTimes.find { stopTime -> stopTime.tripId.value == "57329111-JAN19-JANDA19-Weekday-26" })
    }

    @Test
    fun testStopTimeGetAll() {
        val source = OcTranspoGtfsDirectory(testFolder.root.toPath())

        val stopTimes = source.stopTimes.getAll()
        assertTrue(stopTimes.size == 65)
    }

    @Test
    fun testStopTimeInsert() {
        val source = OcTranspoGtfsDirectory(testFolder.root.toPath())

        val stopTime = StopTime(TripId("ATrip"), "1:00", "1:00", StopId("AA100"), 1, null, null, null, null, null)
        assertTrue(source.stopTimes.insert(stopTime))
        assertTrue(source.stopTimes.getByTripId(stopTime.tripId).contains(stopTime))
        assertEquals(stopTime, source.stopTimes.getByTripId(stopTime.tripId)[0])
    }

    @Test
    fun testStopTimeDelete() {
        val source = OcTranspoGtfsDirectory(testFolder.root.toPath())

        val stopTime = StopTime(TripId("ATrip"), "1:00", "1:00", StopId("AA100"), 1, null, null, null, null, null)
        source.stopTimes.insert(stopTime)

        assertTrue(source.stopTimes.delete(stopTime))
        assertFalse(source.stopTimes.getByTripId(stopTime.tripId).contains(stopTime))
    }

    // Trips tests
    @Test
    fun testTripGetByRouteId() {
        val source = OcTranspoGtfsDirectory(testFolder.root.toPath())

        val trips = source.trips.getByRouteId("301-288".asRouteId()!!)
        assertTrue(trips.size == 2)
        assertNotNull(trips.find { trip -> trip.tripId.value == "56994291-JAN19-301Shop-Weekday-01" })
        assertNotNull(trips.find { trip -> trip.tripId.value == "56994293-JAN19-301Shop-Weekday-01" })
    }

    @Test
    fun testTripGetByTripId() {
        val source = OcTranspoGtfsDirectory(testFolder.root.toPath())

        val trip = source.trips.getByTripId("56994291-JAN19-301Shop-Weekday-01".asTripId()!!)
        assertNotNull(trip)
        assertTrue(trip?.tripId?.value == "56994291-JAN19-301Shop-Weekday-01")
    }

    @Test
    fun testTripGetByServiceId() {
        val source = OcTranspoGtfsDirectory(testFolder.root.toPath())

        val trips = source.trips.getByServiceId("JAN19-301Shop-Weekday-01".asCalendarServiceId()!!)
        assertEquals(2, trips.size)
    }

    @Test
    fun testTripGetAll() {
        val source = OcTranspoGtfsDirectory(testFolder.root.toPath())

        val trips = source.trips.getAll()
        assertEquals(10, trips.size)
    }

    @Test
    fun testTripInsert() {
        val source = OcTranspoGtfsDirectory(testFolder.root.toPath())

        val trip = Trip(RouteId("1-1"), CalendarServiceId("TestService"), TripId("Trip1"), "Forward", null, 0, null, null, null, null)
        assertTrue(source.trips.insert(trip))
        assertEquals(source.trips.getByTripId(trip.tripId), trip)
    }

    @Test
    fun testTripUpdate() {
        val source = OcTranspoGtfsDirectory(testFolder.root.toPath())

        val trip = Trip(RouteId("1-1"), CalendarServiceId("TestService"), TripId("Trip1"), "Forward", null, 0, null, null, null, null)
        source.trips.insert(trip)

        val newTrip = Trip(trip.routeId, trip.serviceId, trip.tripId, "Backward",  null, 1, "1", null, null, null)
        assertTrue(source.trips.update(newTrip))
        assertNotEquals(trip, source.trips.getByTripId(trip.tripId))
        assertEquals(newTrip, source.trips.getByTripId(trip.tripId))
    }

    @Test
    fun testTripDelete() {
        val source = OcTranspoGtfsDirectory(testFolder.root.toPath())

        val trip = Trip(RouteId("1-1"), CalendarServiceId("TestService"), TripId("Trip1"), "Forward", null, 0, null, null, null, null)
        source.trips.insert(trip)

        assertTrue(source.trips.delete(trip))
        assertNull(source.trips.getByTripId(trip.tripId))
    }

    
    @Test
    fun testShapes() {
        val source = OcTranspoGtfsDirectory(testFolder.root.toPath())
        assertNull(source.shapes)
    }

}