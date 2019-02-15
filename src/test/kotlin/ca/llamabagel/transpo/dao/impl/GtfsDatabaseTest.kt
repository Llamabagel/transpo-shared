/*
 * Copyright (c) 2019 Derek Ellis. Subject to the MIT license.
 */

package ca.llamabagel.transpo.dao.impl

import ca.llamabagel.transpo.models.gtfs.*
import com.opentable.db.postgres.embedded.FlywayPreparer
import com.opentable.db.postgres.junit.EmbeddedPostgresRules
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class GtfsDatabaseTest {

    @get:Rule
    val postgres = EmbeddedPostgresRules.preparedDatabase(FlywayPreparer.forClasspathLocation("db/testing"))!!


     // GtfsDatabase.stops tests.
    @Test
    fun testStopGetById() {
        val source = GtfsDatabase(postgres.testDatabase.connection)

        val stop = source.stops.getById(StopId("AF970"))
        assertTrue(stop != null)
        assertTrue(stop?.id == StopId("AF970"))
        assertTrue(stop?.code == "3030")

        assertNull(source.stops.getById(StopId("SomeNonIdValue")))
    }

    @Test
    fun testStopGetByCode() {
        val source = GtfsDatabase(postgres.testDatabase.connection)

        val result = source.stops.getByCode("3031")
        assertTrue(result.size == 2)
        assertNotNull(result.find { stop -> stop.id == StopId("AF980") })
    }

    @Test
    fun testStopGetAll() {
        val source = GtfsDatabase(postgres.testDatabase.connection)

        val result = source.stops.getAll()
        assertTrue(result.size == 5)
        assertNotNull(result.find { stop -> stop.id == StopId("AF980") })
    }

    @Test
    fun testStopInsert() {
        val source = GtfsDatabase(postgres.testDatabase.connection)

        val stop = Stop(StopId("BB200"), "1234", "INSERT / TEST", null, -46.0, 76.0, null, null, null, null, null, null)
        assertTrue(source.stops.insert(stop))
        assertTrue(source.stops.getById(stop.id) == stop)
    }

    @Test
    fun testStopUpdate() {
        val source = GtfsDatabase(postgres.testDatabase.connection)

        val stop = Stop(StopId("BB200"), "1234", "INSERT / TEST", null, -46.0, 76.0, null, null, null, null, null, null)
        source.stops.insert(stop)

        // Update each value except the id.
        val newStop = Stop(StopId("BB200"), "1235", "UPDATE / TEST", "A description", -47.0, 78.0, 0, "url", 7, "AA100", "EST", 1)
        source.stops.update(newStop)
        assertFalse(source.stops.getById(newStop.id) == stop)
        assertTrue(source.stops.getById(newStop.id) == newStop)
    }

    @Test
    fun testStopDelete() {
        val source = GtfsDatabase(postgres.testDatabase.connection)

        val stop = Stop(StopId("BB200"), "1234", "INSERT / TEST", null, -46.0, 76.0, null, null, null, null, null, null)
        source.stops.insert(stop)
        assertTrue(source.stops.delete(stop))
        assertNull(source.stops.getById(stop.id))
    }

    // GtfsDatabase.routes tests
    @Test
    fun testRouteGetByNumber() {
        val source = GtfsDatabase(postgres.testDatabase.connection)

        val route = source.routes.getByNumber("291")
        assertNotNull(route)
        assertTrue(route?.shortName == "291")
    }

    @Test
    fun testRouteGetById() {
        val source = GtfsDatabase(postgres.testDatabase.connection)

        val route = source.routes.getById(RouteId("5-288"))
        assertNotNull(route)
        assertTrue(route?.id == RouteId("5-288"))
    }

    @Test
    fun testRouteGetAll() {
        val source = GtfsDatabase(postgres.testDatabase.connection)

        val routes = source.routes.getAll()
        assertTrue(routes.size == 3)
        assertNotNull(routes.find { route -> route.id == RouteId("5-288") })
    }

    @Test
    fun testRouteInsert() {
        val source = GtfsDatabase(postgres.testDatabase.connection)

        val route = Route(RouteId("1-1"), null, "1", "", null, 3, null, null, null, null)
        assertTrue(source.routes.insert(route))
        assertTrue(source.routes.getById(route.id) == route)
    }

    @Test
    fun testRouteUpdate() {
        val source = GtfsDatabase(postgres.testDatabase.connection)

        val route = Route(RouteId("1-1"), null, "1", "", null, 3, null, null, null, null)
        source.routes.insert(route)

        // Update each value except the id.
        val newRoute = Route(RouteId("1-1"), AgencyId("1"), "1!", "Somewhere", "A Route", 2, "", "", "", 1)
        source.routes.update(newRoute)
        assertFalse(source.routes.getById(newRoute.id) == route)
        assertTrue(source.routes.getById(newRoute.id) == newRoute)
    }

    @Test
    fun testRouteDelete() {
        val source = GtfsDatabase(postgres.testDatabase.connection)

        val route = Route(RouteId("1-1"), null, "1", "", null, 3, null, null, null, null)
        source.routes.insert(route)
        assertTrue(source.routes.delete(route))
        assertNull(source.routes.getById(route.id))
    }

    @Test
    fun testAgencyGetById() {
        val source = GtfsDatabase(postgres.testDatabase.connection)

        val agency = source.agencies.getById(AgencyId("1"))
        assertNotNull(agency)
        assertTrue(agency?.id == AgencyId("1"))
    }

    @Test
    fun testAgencyGetAll() {
        val source = GtfsDatabase(postgres.testDatabase.connection)

        val agencies = source.agencies.getAll()
        assertTrue(agencies.size == 1)
        assertNotNull(agencies.find { agency -> agency.name == "OC Transpo" })
    }

    @Test
    fun testAgencyInsert() {
        val source = GtfsDatabase(postgres.testDatabase.connection)

        val agency = Agency(AgencyId("2"), "New", "https://llamabagel.ca", "EST", null, null, null, null)

        assertTrue(source.agencies.insert(agency))
        assertTrue(source.agencies.getById(agency.id) == agency)
    }

    @Test
    fun testAgencyUpdate() {
        val source = GtfsDatabase(postgres.testDatabase.connection)

        val agency = Agency(AgencyId("2"), "New", "https://llamabagel.ca", "EST", null, null, null, null)
        source.agencies.insert(agency)

        val newAgency = Agency(AgencyId("2"), "New Name", "https://transpo.llamabagel.ca", "PST", "fr", "613-000-0000", "https://", "ccontact@llamabagel.ca")
        assertTrue(source.agencies.update(newAgency))
        assertFalse(source.agencies.getById(agency.id) == agency)
        assertNotNull(source.agencies.getById(agency.id) == newAgency)
    }

    @Test
    fun testAgencyDelete() {
        val source = GtfsDatabase(postgres.testDatabase.connection)

        val agency = Agency(AgencyId("2"), "New", "https://llamabagel.ca", "EST", null, null, null, null)
        source.agencies.insert(agency)

        assertTrue(source.agencies.delete(agency))
        assertNull(source.agencies.getById(agency.id))
    }

    // Calendar tests
    @Test
    fun testCalendarGetByServiceId() {
        val source = GtfsDatabase(postgres.testDatabase.connection)

        val calendar = source.calendars.getByServiceId(CalendarServiceId("JAN19-JANDA19-Weekday-26"))
        assertNotNull(calendar)
        assertTrue(calendar?.serviceId == CalendarServiceId("JAN19-JANDA19-Weekday-26"))
    }

    @Test
    fun testCalendarGetByDays() {
        val source = GtfsDatabase(postgres.testDatabase.connection)

        val weekdays = source.calendars.getByDays(monday = 1, friday = 1)
        assertTrue(weekdays.size == 2)
        assertNotNull(weekdays.find { calendar -> calendar.serviceId == CalendarServiceId("JAN19-JANDA19-Weekday-26") })

        val sundays = source.calendars.getByDays(monday = 0, sunday = 1)
        assertTrue(sundays.size == 1)
        assertNotNull(sundays.find { calendar -> calendar.serviceId == CalendarServiceId("JAN19-JANSU19-Sunday-02") })

        val none = source.calendars.getByDays(monday = 1, sunday = 1)
        assertTrue(none.isEmpty())
    }

    @Test
    fun testCalendarGetAll() {
        val source = GtfsDatabase(postgres.testDatabase.connection)

        val calendars = source.calendars.getAll()

        assertTrue(calendars.size == 4)
        assertNotNull(calendars.find { calendar -> calendar.serviceId == CalendarServiceId("JAN19-JANSU19-Sunday-02") })
    }

    @Test
    fun testCalendarInsert() {
        val source = GtfsDatabase(postgres.testDatabase.connection)

        val calendar = Calendar(CalendarServiceId("SERVICE"), 1, 0, 1, 0, 1, 0, 1, "Today", "Tomorrow")
        assertTrue(source.calendars.insert(calendar))
        assertTrue(source.calendars.getByServiceId(calendar.serviceId) == calendar)
    }

    @Test
    fun testCalendarUpdate() {
        val source = GtfsDatabase(postgres.testDatabase.connection)

        val calendar = Calendar(CalendarServiceId("SERVICE"), 1, 0, 1, 0, 1, 0, 1, "Today", "Tomorrow")
        source.calendars.insert(calendar)

        val newCalendar = Calendar(CalendarServiceId("SERVICE"), 0, 1, 0, 1, 0, 1, 0, "Yesterday", "Today")
        assertTrue(source.calendars.update(newCalendar))
        assertFalse(source.calendars.getByServiceId(calendar.serviceId) == calendar)
        assertTrue(source.calendars.getByServiceId(calendar.serviceId) == newCalendar)
    }

    @Test
    fun testCalendarDelete() {
        val source = GtfsDatabase(postgres.testDatabase.connection)

        val calendar = Calendar(CalendarServiceId("SERVICE"), 1, 0, 1, 0, 1, 0, 1, "Today", "Tomorrow")
        source.calendars.insert(calendar)

        assertTrue(source.calendars.delete(calendar))
        assertNull(source.calendars.getByServiceId(calendar.serviceId))
    }

    // CalendarDate tests
    @Test
    fun testCalendarDateGetByServiceId() {
        val source = GtfsDatabase(postgres.testDatabase.connection)
        val calendarDates = source.calendarDates.getByServiceId(CalendarServiceId("JAN19-d1930LoR-Weekday-01"))

        assertTrue(calendarDates.size == 1)
        assertNotNull(calendarDates.find { calendarDate -> calendarDate.serviceId == CalendarServiceId("JAN19-d1930LoR-Weekday-01")})
    }

    @Test
    fun testCalendarDateGetByDate() {
        val source = GtfsDatabase(postgres.testDatabase.connection)
        val calendarDates = source.calendarDates.getByDate("20190218")

        assertTrue(calendarDates.size == 1)
        assertNotNull(calendarDates.find { calendarDate -> calendarDate.date == "20190218"})
    }

    @Test
    fun testCalendarDateGetAll() {
        val source = GtfsDatabase(postgres.testDatabase.connection)
        val calendarDates = source.calendarDates.getAll()

        assertTrue(calendarDates.size == 3)
        assertNotNull(calendarDates.find { calendarDate -> calendarDate.date == "20190218"})
    }

    @Test
    fun testCalendarDateInsert() {
        val source = GtfsDatabase(postgres.testDatabase.connection)
        val calendarDate = CalendarDate(CalendarServiceId("NewException"), "Today", 1)

        assertTrue(source.calendarDates.insert(calendarDate))
        assertTrue(source.calendarDates.getByServiceId(calendarDate.serviceId).contains(calendarDate))
    }

    @Test
    fun testCalendarDateUpdate() {
        val source = GtfsDatabase(postgres.testDatabase.connection)
        val calendarDate = CalendarDate(CalendarServiceId("NewException"), "Today", 1)

        source.calendarDates.insert(calendarDate)
        val newCalendarDate = CalendarDate(CalendarServiceId("NewException"), "Today", 2)
        assertTrue(source.calendarDates.update(newCalendarDate))
        assertFalse(source.calendarDates.getByServiceId(calendarDate.serviceId).contains(calendarDate))
        assertTrue(source.calendarDates.getByServiceId(calendarDate.serviceId).contains(newCalendarDate))
    }

    @Test
    fun testCalendarDateDelete() {
        val source = GtfsDatabase(postgres.testDatabase.connection)
        val calendarDate = CalendarDate(CalendarServiceId("NewException"), "Today", 1)

        source.calendarDates.insert(calendarDate)
        assertTrue(source.calendarDates.delete(calendarDate))
        assertTrue(source.calendarDates.getByServiceId(calendarDate.serviceId).isEmpty())
    }

    // StopTime tests
    /*@Test
    fun testStopTimeGetByTrip() {
        val source = GtfsDatabase(postgres.testDatabase.connection)


    }

    @Test
    fun testStopTimeGetByStop() {
        val source = GtfsDatabase(postgres.testDatabase.connection)

    }

    @Test
    fun testStopTimeGetAll() {
        val source = GtfsDatabase(postgres.testDatabase.connection)

    }

    @Test
    fun testStopTimeInsert() {
        val source = GtfsDatabase(postgres.testDatabase.connection)

    }

    @Test
    fun testStopTimeUpdate() {
        val source = GtfsDatabase(postgres.testDatabase.connection)

    }

    @Test
    fun testStopTimeDelete() {
        val source = GtfsDatabase(postgres.testDatabase.connection)

    }*/
}