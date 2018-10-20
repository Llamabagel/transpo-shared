package ca.llamabagel.transpo.models.gtfs

/**
 * [https://developers.google.com/transit/gtfs/reference/#calendar_datestxt]
 */
data class CalendarDate(val serviceId: String,
                        val date: String,
                        val exceptionType: Int)