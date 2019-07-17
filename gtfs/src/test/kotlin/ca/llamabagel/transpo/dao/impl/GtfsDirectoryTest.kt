/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.dao.impl

import ca.llamabagel.transpo.dao.gtfs.*
import ca.llamabagel.transpo.dao.listAll
import ca.llamabagel.transpo.models.gtfs.*
import org.apache.commons.io.FileUtils
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
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
        assertTrue(stop != null)
        assertEquals(StopId("AF970").value, stop?.id)
        assertEquals("3030", stop?.code)

        assertNull(source.stops.getById(StopId("SomeNonIdValue")))
    }

    @Test
    fun testStopGetByCode() {
        val source = GtfsDirectory(testFolder.root.toPath())

        val result = source.stops.listByCode("3031")
        assertEquals(2, result.size)
        assertNotNull(result.find { stop -> stop.id == StopId("AF980") })
    }

    @Test
    fun testStopGetAll() {
        val source = GtfsDirectory(testFolder.root.toPath())

        val result = source.stops.listAll()
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

    @Test
    fun testStopDelete() {
        val source = GtfsDirectory(testFolder.root.toPath())

        val stop = Stop(StopId("BB200"), "1234", "INSERT / TEST", null, -46.0, 76.0, null, null, null, null, null, null)
        source.stops.insert(stop)
        assertTrue(source.stops.delete(stop))
        assertNull(source.stops.getById(stop.id))
    }

    // GtfsDatabase.routes tests
    @Test
    fun testRouteGetByNumber() {
        val source = GtfsDirectory(testFolder.root.toPath())

        val route = source.routes.getByNumber("291")
        assertNotNull(route)
        assertTrue(route?.shortName == "291")
    }

    @Test
    fun testRouteGetById() {
        val source = GtfsDirectory(testFolder.root.toPath())

        val route = source.routes.getById(RouteId("5-288"))
        assertNotNull(route)
        assertTrue(route?.id == RouteId("5-288"))
    }

    @Test
    fun testRouteGetAll() {
        val source = GtfsDirectory(testFolder.root.toPath())

        val routes = source.routes.listAll()
        assertTrue(routes.size == 3)
        assertNotNull(routes.find { route -> route.id == RouteId("5-288") })
    }

    @Test
    fun testRouteInsert() {
        val source = GtfsDirectory(testFolder.root.toPath())

        val route = Route(RouteId("1-1"), null, "1", "", null, 3, null, null, null, null)
        assertTrue(source.routes.insert(route))
        assertTrue(source.routes.getById(route.id) == route)
    }

    @Test
    fun testRouteUpdate() {
        val source = GtfsDirectory(testFolder.root.toPath())

        val route = Route(RouteId("1-1"), null, "1", "", null, 3, null, null, null, null)
        source.routes.insert(route)

        // Update each value except the id.
        val newRoute = Route(RouteId("1-1"), AgencyId("1"), "1!", "Somewhere", "A Route", 2, "url", "color", "textColor", 1)
        source.routes.update(newRoute)
        assertFalse(source.routes.getById(newRoute.id) == route)
        assertTrue(source.routes.getById(newRoute.id) == newRoute)
    }

    @Test
    fun testRouteDelete() {
        val source = GtfsDirectory(testFolder.root.toPath())

        val route = Route(RouteId("1-1"), null, "1", "", null, 3, null, null, null, null)
        source.routes.insert(route)
        assertTrue(source.routes.delete(route))
        assertNull(source.routes.getById(route.id))
    }

    @Test
    fun testAgencyGetById() {
        val source = GtfsDirectory(testFolder.root.toPath())

        val agency = source.agencies.getById(AgencyId("1"))
        assertNotNull(agency)
        assertTrue(agency?.id == AgencyId("1"))
    }

    @Test
    fun testAgencyGetAll() {
        val source = GtfsDirectory(testFolder.root.toPath())

        val agencies = source.agencies.listAll()
        assertTrue(agencies.size == 1)
        assertNotNull(agencies.find { agency -> agency.name == "OC Transpo" })
    }

    @Test
    fun testAgencyInsert() {
        val source = GtfsDirectory(testFolder.root.toPath())

        val agency = Agency(AgencyId("2"), "New", "https://llamabagel.ca", "EST", null, null, null, null)

        assertTrue(source.agencies.insert(agency))
        assertTrue(source.agencies.getById(agency.id) == agency)
    }

    @Test
    fun testAgencyUpdate() {
        val source = GtfsDirectory(testFolder.root.toPath())

        val agency = Agency(AgencyId("2"), "New", "https://llamabagel.ca", "EST", null, null, null, null)
        source.agencies.insert(agency)

        val newAgency = Agency(AgencyId("2"), "New Name", "https://transpo.llamabagel.ca", "PST", "fr", "613-000-0000", "https://", "ccontact@llamabagel.ca")
        assertTrue(source.agencies.update(newAgency))
        assertFalse(source.agencies.getById(agency.id) == agency)
        assertNotNull(source.agencies.getById(agency.id) == newAgency)
    }

    @Test
    fun testAgencyDelete() {
        val source = GtfsDirectory(testFolder.root.toPath())

        val agency = Agency(AgencyId("2"), "New", "https://llamabagel.ca", "EST", null, null, null, null)
        source.agencies.insert(agency)

        assertTrue(source.agencies.delete(agency))
        assertNull(source.agencies.getById(agency.id))
    }

    // Calendar tests
    @Test
    fun testCalendarGetByServiceId() {
        val source = GtfsDirectory(testFolder.root.toPath())

        val calendar = source.calendars.getByServiceId(CalendarServiceId("JAN19-JANDA19-Weekday-26"))
        assertNotNull(calendar)
        assertTrue(calendar?.serviceId == CalendarServiceId("JAN19-JANDA19-Weekday-26"))
    }

    @Test
    fun testCalendarGetByDays() {
        val source = GtfsDirectory(testFolder.root.toPath())

        val weekdays = source.calendars.listByDays(monday = 1, friday = 1)
        assertTrue(weekdays.size == 2)
        assertNotNull(weekdays.find { calendar -> calendar.serviceId == CalendarServiceId("JAN19-JANDA19-Weekday-26") })

        val sundays = source.calendars.listByDays(monday = 0, sunday = 1)
        assertTrue(sundays.size == 1)
        assertNotNull(sundays.find { calendar -> calendar.serviceId == CalendarServiceId("JAN19-JANSU19-Sunday-02") })

        val none = source.calendars.listByDays(monday = 1, sunday = 1)
        assertTrue(none.isEmpty())

        assertTrue(source.calendars.listByDays().isEmpty())
    }

    @Test
    fun testCalendarGetAll() {
        val source = GtfsDirectory(testFolder.root.toPath())

        val calendars = source.calendars.listAll()

        assertTrue(calendars.size == 4)
        assertNotNull(calendars.find { calendar -> calendar.serviceId == CalendarServiceId("JAN19-JANSU19-Sunday-02") })
    }

    @Test
    fun testCalendarInsert() {
        val source = GtfsDirectory(testFolder.root.toPath())

        val calendar = Calendar(CalendarServiceId("SERVICE"), 1, 0, 1, 0, 1, 0, 1, "Today", "Tomorrow")
        assertTrue(source.calendars.insert(calendar))
        assertEquals(calendar, source.calendars.getByServiceId(calendar.serviceId))
    }

    @Test
    fun testCalendarUpdate() {
        val source = GtfsDirectory(testFolder.root.toPath())

        val calendar = Calendar(CalendarServiceId("SERVICE"), 1, 0, 1, 0, 1, 0, 1, "Today", "Tomorrow")
        source.calendars.insert(calendar)

        val newCalendar = Calendar(CalendarServiceId("SERVICE"), 0, 1, 0, 1, 0, 1, 0, "Yesterday", "Today")
        assertTrue(source.calendars.update(newCalendar))
        assertNotEquals(calendar, source.calendars.getByServiceId(calendar.serviceId))
        assertEquals(newCalendar, source.calendars.getByServiceId(calendar.serviceId))
    }

    @Test
    fun testCalendarDelete() {
        val source = GtfsDirectory(testFolder.root.toPath())

        val calendar = Calendar(CalendarServiceId("SERVICE"), 1, 0, 1, 0, 1, 0, 1, "Today", "Tomorrow")
        source.calendars.insert(calendar)

        assertTrue(source.calendars.delete(calendar))
        assertNull(source.calendars.getByServiceId(calendar.serviceId))
    }

    // CalendarDate tests
    @Test
    fun testCalendarDateGetByServiceId() {
        val source = GtfsDirectory(testFolder.root.toPath())
        val calendarDates = source.calendarDates.listByServiceId(CalendarServiceId("JAN19-d1930LoR-Weekday-01"))

        assertTrue(calendarDates.size == 1)
        assertNotNull(calendarDates.find { calendarDate -> calendarDate.serviceId == CalendarServiceId("JAN19-d1930LoR-Weekday-01") })
    }

    @Test
    fun testCalendarDateGetByDate() {
        val source = GtfsDirectory(testFolder.root.toPath())
        val calendarDates = source.calendarDates.listByDate("20190218")

        assertTrue(calendarDates.size == 1)
        assertNotNull(calendarDates.find { calendarDate -> calendarDate.date == "20190218"})
    }

    @Test
    fun testCalendarDateGetAll() {
        val source = GtfsDirectory(testFolder.root.toPath())
        val calendarDates = source.calendarDates.listAll()

        assertTrue(calendarDates.size == 3)
        assertNotNull(calendarDates.find { calendarDate -> calendarDate.date == "20190218"})
    }

    @Test
    fun testCalendarDateInsert() {
        val source = GtfsDirectory(testFolder.root.toPath())
        val calendarDate = CalendarDate(CalendarServiceId("NewException"), "Today", 1)

        assertTrue(source.calendarDates.insert(calendarDate))
        assertTrue(source.calendarDates.getByServiceId(calendarDate.serviceId).contains(calendarDate))
    }

    @Test
    fun testCalendarDateDelete() {
        val source = GtfsDirectory(testFolder.root.toPath())
        val calendarDate = CalendarDate(CalendarServiceId("NewException"), "Today", 1)

        source.calendarDates.insert(calendarDate)
        assertTrue(source.calendarDates.delete(calendarDate))
        assertTrue(source.calendarDates.listByServiceId(calendarDate.serviceId).isEmpty())
    }

    // StopTime tests
    @Test
    fun testStopTimeGetByTripId() {
        val source = GtfsDirectory(testFolder.root.toPath())

        val stopTimes = source.stopTimes.listByTripId("56994291-JAN19-301Shop-Weekday-01".asTripId()!!)
        assertTrue(stopTimes.size == 5)
        assertNotNull(stopTimes.find { stopTime -> stopTime.stopId.value == "WR285" })
    }

    @Test
    fun testStopTimeGetByStopId() {
        val source = GtfsDirectory(testFolder.root.toPath())

        val stopTimes = source.stopTimes.listByStopId("CK110".asStopId()!!)
        assertTrue(stopTimes.size == 2)
        assertNotNull(stopTimes.find { stopTime -> stopTime.tripId.value == "59528499-JAN19-Reduced-Weekday-02" })
    }

    @Test
    fun testStopTimeGetAll() {
        val source = GtfsDirectory(testFolder.root.toPath())

        val stopTimes = source.stopTimes.listAll()
        assertTrue(stopTimes.size == 9)
    }

    @Test
    fun testStopTimeInsert() {
        val source = GtfsDirectory(testFolder.root.toPath())

        val stopTime = StopTime(TripId("ATrip"), "1:00", "1:00", StopId("AA100"), 1, null, null, null, null, null)
        assertTrue(source.stopTimes.insert(stopTime))
        assertTrue(source.stopTimes.getByTripId(stopTime.tripId).contains(stopTime))
        assertEquals(stopTime, source.stopTimes.listByTripId(stopTime.tripId)[0])
    }

    @Test
    fun testStopTimeDelete() {
        val source = GtfsDirectory(testFolder.root.toPath())

        val stopTime = StopTime(TripId("ATrip"), "1:00", "1:00", StopId("AA100"), 1, null, null, null, null, null)
        source.stopTimes.insert(stopTime)

        assertTrue(source.stopTimes.delete(stopTime))
        assertFalse(source.stopTimes.getByTripId(stopTime.tripId).contains(stopTime))
    }

    // Trips tests
    @Test
    fun testTripGetByRouteId() {
        val source = GtfsDirectory(testFolder.root.toPath())

        val trips = source.trips.listByRouteId("91-288".asRouteId()!!)
        assertTrue(trips.size == 2)
        assertNotNull(trips.find { trip -> trip.tripId.value == "57328740-JAN19-JANDA19-Weekday-26" })
        assertNotNull(trips.find { trip -> trip.tripId.value == "57328743-JAN19-JANDA19-Weekday-26" })

        val directionTrips = source.trips.listByRouteId("91-288".asRouteId()!!, 1)
        assertTrue(directionTrips.size == 1)
        assertNotNull(directionTrips.find { trip -> trip.tripId.value == "57328740-JAN19-JANDA19-Weekday-26" })
        assertNull(directionTrips.find { trip -> trip.tripId.value == "57328743-JAN19-JANDA19-Weekday-26" })
    }

    @Test
    fun testTripGetByTripId() {
        val source = GtfsDirectory(testFolder.root.toPath())

        val trip = source.trips.getByTripId("57328738-JAN19-JANDA19-Weekday-26".asTripId()!!)
        assertNotNull(trip)
        assertTrue(trip?.tripId?.value == "57328738-JAN19-JANDA19-Weekday-26")
    }

    @Test
    fun testTripGetByServiceId() {
        val source = GtfsDirectory(testFolder.root.toPath())

        val trips = source.trips.listByServiceId("JAN19-JANDA19-Weekday-26".asCalendarServiceId()!!)
        assertEquals(4, trips.size)
    }

    @Test
    fun testTripGetAll() {
        val source = GtfsDirectory(testFolder.root.toPath())

        val trips = source.trips.listAll()
        assertEquals(4, trips.size)
    }

    @Test
    fun testTripInsert() {
        val source = GtfsDirectory(testFolder.root.toPath())

        val trip = Trip(RouteId("1-1"), CalendarServiceId("TestService"), TripId("Trip1"), "Forward", null, 0, null, null, null, null)
        assertTrue(source.trips.insert(trip))
        assertEquals(source.trips.getByTripId(trip.tripId), trip)
    }

    @Test
    fun testTripUpdate() {
        val source = GtfsDirectory(testFolder.root.toPath())

        val trip = Trip(RouteId("1-1"), CalendarServiceId("TestService"), TripId("Trip1"), "Forward", null, 0, null, null, null, null)
        source.trips.insert(trip)

        val newTrip = Trip(trip.routeId, trip.serviceId, trip.tripId, "Backward", "No", 1, "1", "None".asShapeId(), 1, 1)
        assertTrue(source.trips.update(newTrip))
        assertNotEquals(trip, source.trips.getByTripId(trip.tripId))
        assertEquals(newTrip, source.trips.getByTripId(trip.tripId))
    }

    @Test
    fun testTripDelete() {
        val source = GtfsDirectory(testFolder.root.toPath())

        val trip = Trip(RouteId("1-1"), CalendarServiceId("TestService"), TripId("Trip1"), "Forward", null, 0, null, null, null, null)
        source.trips.insert(trip)

        assertTrue(source.trips.delete(trip))
        assertNull(source.trips.getByTripId(trip.tripId))
    }

    @Test
    fun testShapeGetById() {
        val source = GtfsDirectory(testFolder.root.toPath())

        val shape = source.shapes?.listById("A_shp".asShapeId()!!)
        assertNotNull(shape)
        assertEquals(3, shape?.size)
    }

    @Test
    fun testShapeGetAll() {
        val source = GtfsDirectory(testFolder.root.toPath())

        val shape = source.shapes?.listAll()
        assertNotNull(shape)
        assertEquals(4, shape?.size)
    }

    @Test
    fun testShapeInsert() {
        val source = GtfsDirectory(testFolder.root.toPath())

        val shape = Shape("Test".asShapeId()!!, 12.0, 12.0, 1, 0.0)
        assertTrue(source.shapes?.insert(shape)!!)
        assertEquals(shape, source.shapes?.listById(shape.id)!![0])
    }

    @Test
    fun testShapeDelete() {
        val source = GtfsDirectory(testFolder.root.toPath())

        val shape = Shape("Test".asShapeId()!!, 12.0, 12.0, 1, 0.0)
        assertTrue(source.shapes?.insert(shape)!!)
        assertTrue(source.shapes?.delete(shape)!!)
        assertEquals(0, source.shapes?.listById(shape.id)!!.size)
    }

}